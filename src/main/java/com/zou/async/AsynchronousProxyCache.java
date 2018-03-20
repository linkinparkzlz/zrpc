package com.zou.async;

import com.google.common.collect.Maps;

import java.util.Map;

public class AsynchronousProxyCache {

    private static Map<String, Class> cache = Maps.newConcurrentMap();

    public static Class get(String key) {
        return cache.get(key);
    }

    public static void save(String key, Class proxyClass) {

        if (!cache.containsKey(key)) {
            synchronized (cache) {
                if (!cache.containsKey(key)) {
                    cache.put(key, proxyClass);
                }
            }
        }
    }


}
























