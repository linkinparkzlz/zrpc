package com.zou.parallel;

public abstract class AbstractDaemonThread implements Runnable {


    protected final Thread thread;
    private static final long JOIN_TIME = 90 * 1000L;
    protected volatile boolean hasNotified = false;
    protected volatile boolean stoped = false;

    public AbstractDaemonThread() {

        this.thread = new Thread(this, this.getDeamonThreadName());
    }


    public abstract String getDeamonThreadName();

    public void start() {
        this.thread.start();
    }

    public void shutdown() {
        this.shutdown();
    }

    @Override
    public void run() {


    }


    public void shutdowm(final boolean interrupt) {
        this.stoped = true;
        synchronized (this) {
            if (!this.hasNotified) {
                this.hasNotified = true;
                this.notify();
            }
        }

        try {
            if (interrupt) {
                this.thread.interrupt();
            }

            if (!this.thread.isDaemon()) {
                this.thread.join(this.getJoinTime());
            }
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

    }


    public void wakeup() {
        synchronized (this) {
            if (!this.hasNotified) {
                this.hasNotified = true;
                this.notify();
            }
        }
    }


    public void onWaitEnd() {

    }

    public boolean isStoped() {
        return stoped;
    }

    public static long getJoinTime() {
        return JOIN_TIME;
    }
}
































































