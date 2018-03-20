package com.zou.async;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

public class AsynchronousCallFilter implements CallbackFilter {

    @Override
    public int accept(Method method) {
        return AsynchronousCallObject.class.isAssignableFrom(method.getDeclaringClass()) ? 1 : 0;
    }
}
