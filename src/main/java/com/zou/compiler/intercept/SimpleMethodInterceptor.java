package com.zou.compiler.intercept;

import org.apache.commons.lang3.StringUtils;

public class SimpleMethodInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        System.out.println(StringUtils.center("[SimpleMethodInterceptor##intercept]", 48, "*"));

        return invocation.proceed();

    }
}






































