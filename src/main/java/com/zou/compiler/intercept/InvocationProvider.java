package com.zou.compiler.intercept;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InvocationProvider implements Invocation {


    private final Method method;

    private final Object[] arguments;

    private final Object proxy;

    private final Object target;

    public InvocationProvider(Object target, Object proxy, Method method, Object[] arguments) {
        this.method = method;
        this.arguments = arguments;
        this.proxy = proxy;
        this.target = target;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object getProxy() {
        return proxy;
    }

    @Override
    public Object proceed() throws Throwable {
        try {
            return method.invoke(target, arguments);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
