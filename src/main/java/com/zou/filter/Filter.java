package com.zou.filter;

import java.lang.reflect.Method;

public interface Filter {

    boolean before(Method method, Object processor, Object[] requestObject);

    void after(Method method, Object processor, Object[] requestObject);
}
