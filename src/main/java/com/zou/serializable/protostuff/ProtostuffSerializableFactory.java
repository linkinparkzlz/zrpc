package com.zou.serializable.protostuff;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * 使用commons.pool2对象缓存池
 *
 * 对于需要大量使用的或者频繁使用的对象，我们需要使用对象缓存池将其缓存起来
 *
 * 使用完后的对象不直接销毁，而是将其放入缓存池中，下次使用的时候
 */
public class ProtostuffSerializableFactory extends BasePooledObjectFactory<ProtostuffSerializable> {

    @Override
    public ProtostuffSerializable create() throws Exception {
        return createProtostuff();
    }

    @Override
    public PooledObject<ProtostuffSerializable> wrap(ProtostuffSerializable protostuffSerializable) {
        return new DefaultPooledObject<ProtostuffSerializable>(protostuffSerializable);
    }


    private ProtostuffSerializable createProtostuff() {
        return new ProtostuffSerializable();
    }
}
