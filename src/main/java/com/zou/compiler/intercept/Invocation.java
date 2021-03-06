package com.zou.compiler.intercept;

import java.lang.reflect.Method;

public interface Invocation {


    Object[] getArguments();

    Method getMethod();

    Object getProxy();

    Object proceed() throws Throwable;

}
