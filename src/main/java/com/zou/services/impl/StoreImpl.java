package com.zou.services.impl;

import com.zou.services.Store;

public class StoreImpl implements Store {

    @Override
    public void save(String object) {

        System.out.println("StoreImpl ## save string:[" + object + "]");
    }

    @Override
    public void save(int x) {

        System.out.println("StoreImpl  ## save int:[" + x + "]");
    }
}
