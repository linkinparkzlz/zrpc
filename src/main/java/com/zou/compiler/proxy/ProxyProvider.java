package com.zou.compiler.proxy;

import com.zou.compiler.intercept.Interceptor;
import com.zou.compiler.invoker.InterceptorInvoker;
import com.zou.compiler.invoker.ObjectInvoker;

public class ProxyProvider extends AbstractProxyProvider {

    private static final ClassCache CLASS_CACHE = new ClassCache(new ByteCodeClassTransformer());

    @Override
    public <T> T createProxy(ClassLoader classLoader, Object object, Interceptor interceptor, Class<?>... proxyClasses) {

        return createProxy(classLoader, new InterceptorInvoker(object, interceptor), proxyClasses);
    }

    private <T> T createProxy(ClassLoader classLoader, ObjectInvoker objectInvoker, final Class<?>... proxyClasses) {
        Class<?> proxyClass = CLASS_CACHE.getProxyClass(classLoader, proxyClasses);
        try {
            T result = (T) proxyClass.getConstructor(ObjectInvoker.class).newInstance(objectInvoker);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

































