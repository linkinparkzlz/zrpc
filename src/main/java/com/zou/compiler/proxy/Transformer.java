package com.zou.compiler.proxy;

public interface Transformer {

    Class<?> transform(ClassLoader classLoader, Class<?>... proxyClasses);


}
