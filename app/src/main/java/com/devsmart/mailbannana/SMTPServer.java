package com.devsmart.mailbannana;


import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SMTPServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMTPServer.class);


    public static class Builder {

        private int mPort = 25;
        private int mMaxConnections = 10;

        public Builder port(int port) {
            Preconditions.checkArgument(port > 0);
            mPort = port;
            return this;
        }

        public Builder maxConnections(int numConnections) {
            Preconditions.checkArgument(numConnections >= 1);
            mMaxConnections = numConnections;
            return this;
        }

        public SMTPServer create() {
            SMTPServer retval = new SMTPServer();
            retval.mPort = mPort;
            retval.mMaxNumConnections = mMaxConnections;

            return retval;
        }

    }

    private boolean mRunning = false;
    private int mPort;
    private ServerSocket mServerSocket;
    private Thread mSocketThread;
    private final HashSet<SMTPConnection> mConnections = new HashSet<SMTPConnection>();
    private final ExecutorService mIOThreads = Executors.newCachedThreadPool();
    private int mMaxNumConnections;


    private SMTPServer() {

    }

    public synchronized void start() throws IOException {
        LOGGER.info("Starting SMTP server on port: {}", mPort);
        if(mRunning) {
            LOGGER.warn("already running");
        } else {
            mServerSocket = new ServerSocket(mPort);
            mSocketThread = new Thread(mSocketListener);
            mSocketThread.start();
            mRunning = true;
        }

    }

    public synchronized void stop() {
        try {
            if(!mRunning) {
                LOGGER.warn("already stopped");
                return;
            }
            mRunning = false;
            mSocketThread.join();
            while(getNumConnections() > 0) {
                synchronized (mConnections) {
                    mConnections.wait();
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error("", e);
            Throwables.propagate(e);
        }
    }

    private final Runnable mSocketListener = new Runnable() {

        @Override
        public void run() {
            try {
                mServerSocket.setSoTimeout(1000);

                while (mRunning) {
                    try {
                        Socket socket = mServerSocket.accept();
                        startConnection(new SMTPConnection(socket, SMTPServer.this));

                    } catch (SocketTimeoutException e) {
                    }

                }

            } catch (Exception e) {
                LOGGER.error("Unhandled exception socket listener thread shutting down...", e);
            }

            mRunning = false;
        }
    };

    private void startConnection(SMTPConnection smtpConnection) {
        synchronized (mConnections) {
            mConnections.add(smtpConnection);
            mConnections.notifyAll();
        }

        mIOThreads.execute(smtpConnection.runable());
    }

    public int getNumConnections() {
        synchronized (mConnections) {
            return mConnections.size();
        }
    }

    public boolean hasTooManyConnections() {
        return getNumConnections() > mMaxNumConnections;
    }

    public void connectionFinished(SMTPConnection smtpConnection) {
        synchronized (mConnections) {
            mConnections.remove(smtpConnection);
            mConnections.notifyAll();
        }

    }

}
