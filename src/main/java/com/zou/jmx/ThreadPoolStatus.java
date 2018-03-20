package com.zou.jmx;

public class ThreadPoolStatus {

    private int poolSize;
    private int activeCount;
    private int corePoolSize;
    private int maxinumPoolSize;
    private int largestPoolsize;
    private long taskCount;
    private long completedTaskCount;

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxinumPoolSize() {
        return maxinumPoolSize;
    }

    public void setMaxinumPoolSize(int maxinumPoolSize) {
        this.maxinumPoolSize = maxinumPoolSize;
    }

    public int getLargestPoolsize() {
        return largestPoolsize;
    }

    public void setLargestPoolsize(int largestPoolsize) {
        this.largestPoolsize = largestPoolsize;
    }

    public long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(long taskCount) {
        this.taskCount = taskCount;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }
}
