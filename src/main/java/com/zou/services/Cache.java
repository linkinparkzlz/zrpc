package com.zou.services;

public interface Cache {

    void put(Object key, Object value);

    Object get(Object key);
}
