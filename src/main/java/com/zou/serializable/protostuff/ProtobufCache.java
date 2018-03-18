package com.zou.serializable.protostuff;

import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


/**
 * 缓存的作用
 * 主要是为了提高序列化的性能
 */
public class ProtobufCache {
    private static class SchemaCacheHolder {
        private static ProtobufCache cache = new ProtobufCache();
    }

    public static ProtobufCache getInstance() {
        return SchemaCacheHolder.cache;
    }

    private Cache<Class<?>, Schema<?>> cache = CacheBuilder.newBuilder()
            .maximumSize(1024).expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    private Schema<?> get(final Class<?> clazz, Cache<Class<?>, Schema<?>> cache) {
        try {
            return cache.get(clazz, (Callable<RuntimeSchema<?>>) () -> RuntimeSchema.createFrom(clazz));
        } catch (ExecutionException e) {
            return null;
        }
    }

    public Schema<?> get(final Class<?> cls) {
        return get(cls, cache);
    }
}



































































