package com.zou.serializable.hessian;

import com.zou.config.SystemConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author zoulvzhou
 * <p>
 * 借助Hessian进行序列化
 */
public class HessianSerializablePool {

    //通用对象池
    private GenericObjectPool<HessianSerializable> hessianSerializableGenericObjectPool;

    private static volatile HessianSerializablePool poolFactory = null;

    private HessianSerializablePool() {

        hessianSerializableGenericObjectPool = new GenericObjectPool<HessianSerializable>(new HessianSerializableFactory());
    }

    //构造方法

    public HessianSerializablePool(final int serializablePoolMaxTotal, final int serializablePoolMinIdle, final long serializablePoolMaxWaitMillis, final long minEvictableIdleTimeMillis) {

        hessianSerializableGenericObjectPool = new GenericObjectPool<HessianSerializable>(new HessianSerializableFactory());

        //对象池的配置信息
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        //连接池中的最大连接数
        config.setMaxTotal(serializablePoolMaxTotal);
        //连接池中的最大空闲连接数
        config.setMinIdle(serializablePoolMinIdle);
        //等待时间，超过这个时间就会抛异常
        config.setMaxWaitMillis(serializablePoolMaxWaitMillis);
        //空闲连接的最小时间，超出这个时间连接将会移除
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        hessianSerializableGenericObjectPool.setConfig(config);

    }

    //线程安全的单例模式，双重锁检查机制

    public static HessianSerializablePool getHessianPoolInstance() {

        if (poolFactory == null) {

            synchronized (HessianSerializablePool.class) {

                if (poolFactory == null) {
                    poolFactory = new HessianSerializablePool(SystemConfig.SERIALIZABLE_POOL_MAX_TOTAL, SystemConfig.SERIALIZABLE_POOL_MIN_IDLE,
                            SystemConfig.SERIALIZABLE_POOL_MAX_WAIT_MILLIS, SystemConfig.SERIALIZABLE_POOL_MIN_EVICTABLE_TIME_MILLIS);
                }
            }
        }

        return poolFactory;
    }

    /**
     * 获取池中的对象的方法
     *
     * @return
     */
    public HessianSerializable borrow() {

        try {

            return getHessianSerializableGenericObjectPool().borrowObject();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }
    }

    /**
     *将对象还回池中
     * @param hessianSerializable
     */

    public void restore(final HessianSerializable hessianSerializable) {
        getHessianSerializableGenericObjectPool().returnObject(hessianSerializable);
    }

    /**
     * 获取对象池的方法
     * @return
     */
    public GenericObjectPool<HessianSerializable> getHessianSerializableGenericObjectPool() {
        return hessianSerializableGenericObjectPool;
    }
}
































