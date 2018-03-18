package com.zou.parallel.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;


/**
 *
 * 拒绝策略类
 * 实现JUC实现的接口。
 *
 */
public class RpcRejectedPolicy implements RejectedExecutionHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(RpcRejectedPolicy.class);

    private final String threadName;

    public RpcRejectedPolicy() {
        this(null);
    }

    public RpcRejectedPolicy(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        if (threadName != null) {
            LOGGER.error("RPC Thread pool [{}] is exhausted, executor={}", threadName, executor.toString());
        }

        if (r instanceof RpcRejectedRunnable) {
            ((RpcRejectedRunnable) r).rejected();
        } else {

            if (!executor.isShutdown()) {

                BlockingQueue<Runnable> queue = executor.getQueue();

                int discardSize = queue.size() >> 1;

                for (int i = 0; i < discardSize; i++) {
                    queue.poll();
                }

                try {
                    queue.put(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}






































































