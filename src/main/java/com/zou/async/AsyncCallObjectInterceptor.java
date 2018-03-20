package com.zou.async;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

public class AsyncCallObjectInterceptor implements MethodInterceptor {

    private static final String ZRPCSTATUS = "_getStatus";
    private Future future;

    public AsyncCallObjectInterceptor(Future future) {
        this.future = future;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        if (ZRPCSTATUS.equals(method.getName())) {

            return getStatus();
        }

        return null;

    }


    private Object getStatus() {

        long startTime = 0L;
        long endTime = 0L;

        if (future instanceof AsynchronousFuture) {

            startTime = ((AsynchronousFuture) future).getStartTime();
            endTime = ((AsynchronousFuture) future).getEndTime();
        }

        CallStatus status = null;


        if (future.isCancelled()) {
            status = CallStatus.TIMEOUT;
        } else if (future.isDone()) {
            status = CallStatus.DONE;
        } else {
            status = CallStatus.RUN;

            if (endTime == 0) {
                endTime = System.currentTimeMillis();
            }
        }

        return new AsynchronousCallStatus(startTime, (endTime - startTime), status);


    }
}


















































































































