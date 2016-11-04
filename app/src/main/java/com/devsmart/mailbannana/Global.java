package com.devsmart.mailbannana;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class Global {


    private static int mIOThread = 0;

    private static final ThreadFactory IOThreadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread retval = new Thread(null, r, "IOThread " + mIOThread++);
            return retval;
        }
    };

    public static final ScheduledExecutorService IOThreads = Executors.newScheduledThreadPool(5, IOThreadFactory);
}
