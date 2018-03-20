package com.zou.async;

import com.zou.config.SystemConfig;
import com.zou.core.reflection.ReflectionHelper;
import com.zou.exception.AsyncCallException;
import com.zou.exception.InvokeTimeoutException;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsynchronousCallResult {


    private Class returnClass;
    private Future future;
    private Long timeout;

    public AsynchronousCallResult(Class returnClass, Future future, Long timeout) {
        this.returnClass = returnClass;
        this.future = future;
        this.timeout = timeout;
    }


    public Object loadFuture() throws AsyncCallException {

        try {
            if (timeout <= 0L) {
                return future.get();
            } else {
                return future.get(timeout, TimeUnit.MILLISECONDS);
            }

        } catch (TimeoutException e) {
            future.cancel(true);
            throw new AsyncCallException(e);

        } catch (InterruptedException e) {
            throw new AsyncCallException(e);
        } catch (Exception e) {
            throw new AsyncCallException(e);
        }


    }


    private void translateTimeoutException(Throwable t) {


        int index = t.getMessage().indexOf(SystemConfig.TIMEOUT_RESPONSE_MSG);
        if (index != -1) {
            throw new InvokeTimeoutException(t);
        }

    }


    public Object getResult() {

        Class proxyClass = AsynchronousProxyCache.get(returnClass.getName());

        if (proxyClass == null) {

            Enhancer enhancer = new Enhancer();
            if (returnClass.isInterface()) {
                enhancer.setInterfaces(new Class[]{AsynchronousCallObject.class, returnClass});
            } else {
                enhancer.setInterfaces(new Class[]{AsynchronousCallObject.class});
                enhancer.setSuperclass(returnClass);
            }

            enhancer.setCallbackFilter(new AsynchronousCallFilter());
            enhancer.setCallbackTypes(new Class[]{AsyncCallResultInterceptor.class, AsyncCallObjectInterceptor.class});

            proxyClass = enhancer.createClass();

            AsynchronousProxyCache.save(returnClass.getName(), proxyClass);
        }

        Enhancer.registerCallbacks(proxyClass, new Callback[]{new AsyncCallResultInterceptor(this), new AsyncCallObjectInterceptor(future)});

        try {
            return ReflectionHelper.newInstance(proxyClass);
        } finally {
            Enhancer.registerStaticCallbacks(proxyClass, null);

        }


    }


}




































































