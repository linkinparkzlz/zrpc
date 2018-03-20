package com.zou.async;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class AsynchronousFuture<V> extends FutureTask<V> {


    private Thread callerThread;
    private Thread runnerThread;
    private long startTime = 0L;
    private long endTime = 0L;


    public AsynchronousFuture(Callable<V> callable) {
        super(callable);
        callerThread = Thread.currentThread();
    }

    @Override
    protected void done() {
        endTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        runnerThread = Thread.currentThread();
        super.run();
    }


    public Thread getCallerThread() {
        return callerThread;
    }

    public void setCallerThread(Thread callerThread) {
        this.callerThread = callerThread;
    }

    public Thread getRunnerThread() {
        return runnerThread;
    }

    public void setRunnerThread(Thread runnerThread) {
        this.runnerThread = runnerThread;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}














































