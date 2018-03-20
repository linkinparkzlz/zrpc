package com.zou.parallel.pool;

import com.zou.bean.ThreadPoolMonitorProvider;
import com.zou.jmx.ThreadPoolStatus;
import com.zou.config.SystemConfig;
import com.zou.parallel.BlockingQueueType;
import com.zou.parallel.policy.*;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class RpcThreadPool {

    private static final Timer TIMER = new Timer("ThraedPoolMonitor", true);

    private static long monitorDelay = 100L;

    private static long monitorPeriod = 300L;

    private static RejectedExecutionHandler createRejectPolicy() {

        RpcRejectedPolicyType rpcRejectedPolicyType = RpcRejectedPolicyType.fromString(System.getProperty(SystemConfig.SYSTEM_PROPRYTY_THREADPOOL_REJECTEDPOOL_POLICY_ATTRIBUATE, "RpcAbortPolicy"));

        switch (rpcRejectedPolicyType) {
            case BLOCKING_POLICY:
                return new RpcBlockingPolicy();
            case CALLER_RUNS_POLICY:
                return new RpcCallerRunPolicy();
            case ABORT_POLICY:
                return new RpcAbortPolicy();
            case REJECTED_POLICY:
                return new RpcRejectedPolicy();
            case DISCARDED_POLICY:
                return new RpcDiscardedPolicy();
            default: {
                break;
            }
        }

        return null;

    }

    private static BlockingQueue<Runnable> createBlockingQueue(int queues) {

        BlockingQueueType blockingQueueType = BlockingQueueType.fromString(System.getProperty(SystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NAME_ATTRBUATE, "LinkedBlockingQueue"));

        switch (blockingQueueType) {
            case LINKED_BLOCKING_QUEUE:
                return new LinkedBlockingQueue<Runnable>();
            case ARRAY_BLOCKING_QUEUE:
                return new ArrayBlockingQueue<Runnable>(SystemConfig.SYSTEM_PROPERTY_PARALLEL * queues);
            case SYNCHRONOUS_QUEUE:
                return new SynchronousQueue<Runnable>();

            default: {
                break;
            }
        }

        return null;
    }


    public static Executor getExecutor(int threads, int queues) {

        System.out.println("Thread pool core threads :" + threads + "queues: " + queues);

        String name = "RpcThreadPool";

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS, createBlockingQueue(queues));
        return threadPoolExecutor;
    }


    public static Executor getExecutorWithJmx(int threads, int queues) {

        final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) getExecutor(threads, queues);

        TIMER.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                ThreadPoolStatus status = new ThreadPoolStatus();
                status.setPoolSize(threadPoolExecutor.getPoolSize());
                status.setActiveCount(threadPoolExecutor.getActiveCount());
                status.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
                status.setMaxinumPoolSize(threadPoolExecutor.getMaximumPoolSize());
                status.setLargestPoolsize(threadPoolExecutor.getLargestPoolSize());
                status.setTaskCount(threadPoolExecutor.getTaskCount());
                status.setCompletedTaskCount(threadPoolExecutor.getCompletedTaskCount());

                try {
                    ThreadPoolMonitorProvider.monitor(status);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MalformedObjectNameException e) {
                    e.printStackTrace();
                } catch (ReflectionException e) {
                    e.printStackTrace();
                } catch (MBeanException e) {
                    e.printStackTrace();
                } catch (InstanceNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, monitorDelay, monitorDelay);

        return threadPoolExecutor;
    }


}



















































































