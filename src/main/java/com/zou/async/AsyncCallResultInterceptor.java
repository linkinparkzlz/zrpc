package com.zou.async;

import net.sf.cglib.proxy.LazyLoader;

public class AsyncCallResultInterceptor implements LazyLoader {


    private AsynchronousCallResult result;

    public AsyncCallResultInterceptor(AsynchronousCallResult result) {
        this.result = result;
    }

    @Override
    public Object loadObject() throws Exception {


        return result.loadFuture();
    }
}
