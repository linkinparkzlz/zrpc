package com.zou.serializable.protostuff;

import com.zou.config.SystemConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class ProtostuffSerializablePool {


    private GenericObjectPool<ProtostuffSerializable> protostuffSerializableGenericObjectPool;

    private static volatile ProtostuffSerializablePool poolFactory = null;


    private ProtostuffSerializablePool() {
        protostuffSerializableGenericObjectPool = new GenericObjectPool<ProtostuffSerializable>(new ProtostuffSerializableFactory());
    }


    public ProtostuffSerializablePool(final int serializablePoolMaxTotal, final int serializablePoolMinIdle, final long serializablePoolMaxWaitMillis, final long minEvictableIdleTimeMillis) {

        protostuffSerializableGenericObjectPool = new GenericObjectPool<ProtostuffSerializable>(new ProtostuffSerializableFactory());

        //用来设置构造对象池时各个属性
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        //对象池中最多能有多少对象
        config.setMaxTotal(serializablePoolMaxTotal);
        //对象池中最少能保留多少空闲对象
        config.setMinIdle(serializablePoolMinIdle);

        config.setMaxWaitMillis(serializablePoolMaxWaitMillis);
        config.setSoftMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        protostuffSerializableGenericObjectPool.setConfig(config);
    }


    public static ProtostuffSerializablePool getProtostuffInstance() {

        if (poolFactory == null) {
            synchronized (ProtostuffSerializablePool.class) {
                if (poolFactory == null) {
                    poolFactory = new ProtostuffSerializablePool(SystemConfig.SERIALIZABLE_POOL_MAX_TOTAL, SystemConfig.SERIALIZABLE_POOL_MIN_IDLE,
                            SystemConfig.SERIALIZABLE_POOL_MAX_WAIT_MILLIS, SystemConfig.SERIALIZABLE_POOL_MIN_EVICTABLE_TIME_MILLIS);
                }
            }
        }
        return poolFactory;
    }

    public ProtostuffSerializable borrow() {
        try {

            return getProtostuffSerializableGenericObjectPool().borrowObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void restore(final ProtostuffSerializable protostuffSerializable) {

        getProtostuffSerializableGenericObjectPool().returnObject(protostuffSerializable);
    }

    public GenericObjectPool<ProtostuffSerializable> getProtostuffSerializableGenericObjectPool() {
        return protostuffSerializableGenericObjectPool;
    }
}











































































