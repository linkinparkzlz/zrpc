package com.zou.parallel.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 调用者来决定的执行策略
 */
public class RpcCallerRunPolicy extends ThreadPoolExecutor.CallerRunsPolicy {


    private static final Logger LOGGER = LoggerFactory.getLogger(RpcCallerRunPolicy.class);

    private String threadName;

    public RpcCallerRunPolicy(String threadName) {
        this.threadName = threadName;
    }

    public RpcCallerRunPolicy() {
        this(null);
    }


    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {

        if (threadName != null) {
            LOGGER.error("Thread pool [{{}] is exhausted,excutor={}", threadName, threadPoolExecutor);
        }

        super.rejectedExecution(runnable, threadPoolExecutor);

    }
}












































