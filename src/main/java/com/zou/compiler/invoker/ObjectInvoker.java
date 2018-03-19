package com.zou.compiler.invoker;

import java.lang.reflect.Method;

public interface ObjectInvoker {

    Object invoke(Object proxy, Method method, Object... arguments) throws Throwable;


}








































