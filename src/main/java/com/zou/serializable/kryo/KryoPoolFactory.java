package com.zou.serializable.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.zou.bean.MessageRequest;
import com.zou.bean.MessageResponse;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * @author zoulvzhou
 * <p>
 * 这里仍然使用单例模式
 * 为了保证线程安全，使用双检查锁机制（DCL）
 */
public class KryoPoolFactory {

    //使用volatile关键字保证可见性
    private static volatile KryoPoolFactory poolFactory = null;

    private KryoFactory factory = (KryoFactory) () -> {

        //设置属性
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(MessageRequest.class);
        kryo.register(MessageResponse.class);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

        return kryo;
    };


    private KryoPool pool = new KryoPool.Builder(factory).build();

    public KryoPoolFactory() {

    }


    //获取Kryo对象池实例的方法
    public static KryoPool getKryoPoolInstance() {

        if (poolFactory == null) {
            synchronized (KryoPoolFactory.class) {

                if (poolFactory == null) {
                    poolFactory = new KryoPoolFactory();
                }
            }
        }

        return poolFactory.getPool();
    }


    public KryoPool getPool() {
        return pool;
    }
}



















































































