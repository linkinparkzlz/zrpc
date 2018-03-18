package com.zou.parallel.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 *拒绝策略
 *
 * 直接拒绝执行，抛出异常
 */
public class RpcAbortPolicy extends ThreadPoolExecutor.AbortPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcAbortPolicy.class);

    private String threadName;

    public RpcAbortPolicy(String threadName) {
        this.threadName = threadName;
    }

    public RpcAbortPolicy() {
        this(null);
    }


    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {

        if (threadName != null) {
            LOGGER.error("RPC Thread pool [{}] is exhausted, executor={}", threadName, threadPoolExecutor.toString());
        }


        String message = String.format("RpcServer["
                        + " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d),"
                        + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)]",
                threadName, threadPoolExecutor.getPoolSize(), threadPoolExecutor.getActiveCount(), threadPoolExecutor.getCorePoolSize(),
                threadPoolExecutor.getMaximumPoolSize(), threadPoolExecutor.getLargestPoolSize(),
                threadPoolExecutor.getTaskCount(), threadPoolExecutor.getCompletedTaskCount(), threadPoolExecutor.isShutdown(),
                threadPoolExecutor.isTerminated(), threadPoolExecutor.isTerminating());
        System.out.println(message);


        super.rejectedExecution(runnable, threadPoolExecutor);
    }
}























































