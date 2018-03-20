package com.zou.async;

public class AsynchronousCallStatus {

    private long startTime;
    private long elapseTime;
    private CallStatus status;


    public AsynchronousCallStatus(long startTime, long elapseTime, CallStatus status) {
        this.startTime = startTime;
        this.elapseTime = elapseTime;
        this.status = status;
    }


    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getElapseTime() {
        return elapseTime;
    }

    public void setElapseTime(long elapseTime) {
        this.elapseTime = elapseTime;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AsyncLoadStatus [ststus=" + status + ", startTime=" + startTime + ", elapseTime=" + elapseTime + "]";
    }
}
