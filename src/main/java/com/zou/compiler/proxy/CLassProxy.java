package com.zou.compiler.proxy;

import com.zou.compiler.intercept.Interceptor;

public interface CLassProxy {

    <T> T createProxy(Object object, Interceptor interceptor, Class<?>... proxyClasses);

    <T> T createProxy(ClassLoader classLoader, Object object, Interceptor interceptor, Class<?>... proxyClasses);




}












