package com.devsmart.mailbannana;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class SMTPConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMTPConnection.class);

    private final Socket mSocket;
    private final SMTPServer mServer;

    public SMTPConnection(Socket socket, SMTPServer smtpServer) {
        mSocket = socket;
        mServer = smtpServer;
    }

    public void run() throws IOException {
        try {
            LOGGER.info("new connections from: {}", mSocket.getRemoteSocketAddress());


        } finally {
            mServer.connectionFinished(this);
        }
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
