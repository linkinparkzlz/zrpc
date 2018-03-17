package com.zou.serializable.hessian;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;


/**
 * 对象池
 * BasePooledObjectFactory
 *
 * Hessian序列化工厂
 */
public class HessianSerializableFactory extends BasePooledObjectFactory<HessianSerializable> {

    @Override
    public HessianSerializable create() throws Exception {

        return createHessian();

    }

    @Override
    public PooledObject<HessianSerializable> wrap(HessianSerializable hessianSerializable) {
        return new DefaultPooledObject<HessianSerializable>(hessianSerializable);
    }

    private HessianSerializable createHessian() {
        return new HessianSerializable();
    }
}



































































