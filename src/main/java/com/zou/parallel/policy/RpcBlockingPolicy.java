package com.zou.parallel.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 *
 */
public class RpcBlockingPolicy implements RejectedExecutionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcBlockingPolicy.class);

    private String threadName;

    public RpcBlockingPolicy() {
        this(null);
    }

    public RpcBlockingPolicy(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        if (threadName != null) {
            LOGGER.error("Thread pool [{}] is exhausted,executor= {}", threadName, executor.toString());
        }

        if (!executor.isShutdown()) {
            try {
                executor.getQueue().put(r);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}

















































