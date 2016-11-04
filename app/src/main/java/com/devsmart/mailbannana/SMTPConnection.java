package com.devsmart.mailbannana;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SMTPConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMTPConnection.class);
    private static final char[] NEWLINE = new char[] {'\r', '\n'};

    private final Socket mSocket;
    private final SMTPServer mServer;
    private OutputStreamWriter mWriter;
    private CRLFReader mReader;

    public SMTPConnection(Socket socket, SMTPServer smtpServer) {
        mSocket = socket;
        mServer = smtpServer;
    }

    public void run() throws IOException {
        try {
            LOGGER.info("new connections from: {}", mSocket.getRemoteSocketAddress());

            mReader = new CRLFReader(new InputStreamReader(mSocket.getInputStream()));
            mWriter = new OutputStreamWriter(mSocket.getOutputStream());


            if (mServer.hasTooManyConnections()) {
                LOGGER.warn("Too many connections");
                send("421 Too many connections, try again later");

            } else {
                send("220 " + mServer.getHostName() + " ESMTP");

                commandLoop();

            }

        } catch (SocketTimeoutException e) {
            send("421 Timeout waiting for data from client.");

        } finally {
            try {
                mWriter.close();
                mReader.close();
            } catch (Exception e) {
                LOGGER.error("", e);
            }
            mServer.connectionFinished(this);
        }
    }

    private void commandLoop() throws IOException {

        String line = mReader.readLine();



    }

    private void send(String msg) throws IOException {
        mWriter.write(msg, 0, msg.length());
        mWriter.write(NEWLINE);
        mWriter.flush();
    }


    public Runnable runable() {
        return new Runnable() {

            @Override
            public void run() {
                run();
            }
        };
    }
}
