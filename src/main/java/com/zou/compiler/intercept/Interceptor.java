package com.zou.compiler.intercept;

public interface Interceptor {


    Object intercept(Invocation invocation) throws Throwable;

}
