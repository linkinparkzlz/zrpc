package com.zou.compiler.invoker;

import com.zou.core.reflection.ReflectionHelper;

import java.lang.reflect.Method;

public abstract class AbstractInvoker implements ObjectInvoker {

    @Override
    public Object invoke(Object proxy, Method method, Object... arguments) throws Throwable {

        if (ReflectionHelper.isHashCodeMethod(method)) {
            return Integer.valueOf(System.identityHashCode(proxy));
        }

        if (ReflectionHelper.isEqualsMethod(method)) {
            return Boolean.valueOf(proxy == arguments[0]);
        }
        return invokeImpl(proxy, method, arguments);
    }


    public abstract Object invokeImpl(Object proxy, Method method, Object[] args) throws Throwable;
}
