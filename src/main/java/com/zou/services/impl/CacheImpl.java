package com.zou.services.impl;

import com.zou.services.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheImpl implements Cache {

    private final Map<Object, Object> store;

    public CacheImpl() {

        final int max = 256;
        this.store = new LinkedHashMap<Object, Object>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > max;
            }
        };
    }


    @Override
    public void put(Object key, Object value) {

        store.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return store.get(key);
    }
}







































































