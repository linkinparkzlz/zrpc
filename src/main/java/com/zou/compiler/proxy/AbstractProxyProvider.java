package com.zou.compiler.proxy;

import com.zou.compiler.intercept.Interceptor;

public abstract class AbstractProxyProvider implements CLassProxy {

    @Override
    public <T> T createProxy(Object object, Interceptor interceptor, Class<?>... proxyClasses) {
        return createProxy(Thread.currentThread().getContextClassLoader(), object, interceptor, proxyClasses);
    }
}







