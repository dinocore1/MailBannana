package com.devsmart.mailbannana;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static final class Config {
        Integer SMTP_port;
        String hostname;
    }

    private static boolean mRunning;
    private static SMTPServer mSMPTServer;

    private static SMTPServer buildServer(Config config) {
        SMTPServer.Builder smtpBuilder = new SMTPServer.Builder();
        if(config.SMTP_port != null) {
            smtpBuilder.port(config.SMTP_port);
        }

        if(config.hostname != null) {
            smtpBuilder.hostname(config.hostname);
        }

        return smtpBuilder.create();
    }

    public static void main(String[] args) {

        Config config = new Config();
        config.SMTP_port = 9005;

        mSMPTServer = buildServer(config);

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
