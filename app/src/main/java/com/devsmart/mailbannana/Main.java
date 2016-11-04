package com.devsmart.mailbannana;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static boolean mRunning;
    private static SMTPServer mSMPTServer;

    public static void main(String[] args) {
        mSMPTServer = new SMTPServer.Builder()
                .port(9005)
                .create();

        try {
            mSMPTServer.start();

            mRunning = true;
            Runtime.getRuntime().addShutdownHook(new ShutdownThread());

            while (mRunning) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        mSMPTServer.stop();

    }

    private static class ShutdownThread extends Thread {

        @Override
        public void run() {
            LOGGER.info("got shutdown signal");
            mRunning = false;
        }
    }
}
