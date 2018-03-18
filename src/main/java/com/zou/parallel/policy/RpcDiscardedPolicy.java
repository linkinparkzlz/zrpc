package com.zou.parallel.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author zoulvzhou
 * 丢弃策略:
 * 直接丢弃队列中的元素
 *
 */
public class RpcDiscardedPolicy implements RejectedExecutionHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(RpcDiscardedPolicy.class);


    private String threadName;

    public RpcDiscardedPolicy(String threadName) {
        this.threadName = threadName;
    }

    public RpcDiscardedPolicy() {
        this(null);
    }


    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        if (threadName != null) {

            LOGGER.error("Thread pool [{}] is exhausted ,executor={}", threadName, executor.toString());
        }

        if (!executor.isShutdown()) {

            BlockingQueue<Runnable> queue = executor.getQueue();
            int discardSize = queue.size() >> 1;

            for (int i = 0; i < discardSize; i++) {
                queue.poll();
            }

            queue.offer(r);
        }


    }
}



























