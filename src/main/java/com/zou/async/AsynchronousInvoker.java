package com.zou.async;

import com.zou.config.SystemConfig;
import com.zou.core.reflection.ReflectionHelper;
import com.zou.exception.AsyncCallException;
import com.zou.parallel.pool.RpcThreadPool;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class AsynchronousInvoker {


    private ThreadPoolExecutor executor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(SystemConfig.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS, SystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS);

    public <R> R submit(final AsynchronousCallback<R> callback) {

        Type type = callback.getClass().getGenericInterfaces()[0];

        if (type instanceof ParameterizedType) {
            Class returnClass = (Class) ReflectionHelper.getGenericClass((ParameterizedType) type, 0);
            return intercept(callback, returnClass);
        } else {
            throw new AsyncCallException("zrpc  AsynchronousCallback must be paramaterized type");
        }
    }


    private <T> AsynchronousFuture submit(Callable<T> task) {

        AsynchronousFuture asyncFuture = new AsynchronousFuture<T>(task);
        executor.submit(asyncFuture);
        return asyncFuture;

    }


    private <R> R intercept(final AsynchronousCallback<R> callback, Class<?> returnClass) {

        if (!Modifier.isPublic(returnClass.getModifiers())) {
            return callback.call();
        } else if (Modifier.isFinal(returnClass.getModifiers())) {
            return callback.call();
        } else if (Void.TYPE.isAssignableFrom(returnClass)) {
            return callback.call();
        } else if (returnClass.isPrimitive() | returnClass.isArray()) {
            return callback.call();
        } else {
            return submit(callback, returnClass);
        }
    }

    private <R> R submit(final AsynchronousCallback<R> callback, Class<?> returnClass) {

        Future future = submit(new Callable() {

            @Override
            public Object call() throws Exception {
                return callback.call();
            }
        });

        AsynchronousCallResult result = new AsynchronousCallResult(returnClass, future, SystemConfig.SYSTEM_PROPERTY_MESSAGE_CALLBACK_TIMEOUT);

        R asyncProxy = (R) result.getResult();

        return asyncProxy;
    }

}





















































































































