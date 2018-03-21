package com.zou.netty.thread;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger threadNumbers = new AtomicInteger(1);

    private final AtomicInteger maxThreadNumbers = new AtomicInteger(1);

    private final String prefix;

    private final boolean daemoThread;

    private final ThreadGroup threadGroup;


    public NamedThreadFactory() {
        this("rpc-sever-pool:" + threadNumbers.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemo) {

        this.prefix = StringUtils.isEmpty(prefix) ? prefix + "-thread-" : "";
        daemoThread = daemo;
        SecurityManager securityManager = System.getSecurityManager();
        threadGroup = (securityManager == null) ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();

    }


    @Override
    public Thread newThread(Runnable r) {


        String name = prefix + maxThreadNumbers.getAndIncrement();

        Thread thread = new Thread(threadGroup, r, name, 0);

        thread.setDaemon(daemoThread);

        return thread;


    }


    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}

























































































