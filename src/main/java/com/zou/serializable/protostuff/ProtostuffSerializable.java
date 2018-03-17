package com.zou.serializable.protostuff;


import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Schema;
import com.zou.bean.MessageRequest;
import com.zou.bean.MessageResponse;
import com.zou.serializable.rpc.RpcSerializable;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProtostuffSerializable implements RpcSerializable {

    private static SchemaCache schemaCache = SchemaCache.getInstance();
    private static Objenesis objenesis = new ObjenesisStd(true);
    private boolean rpcDirect = false;


    public boolean isRpcDirect() {
        return rpcDirect;
    }

    public void setRpcDirect(boolean rpcDirect) {
        this.rpcDirect = rpcDirect;
    }

    private static <T> Schema<T> getSchema(Class<T> clazz) {
        return (Schema<T>) schemaCache.get(clazz);
    }


    //序列化
    @Override
    public void serialize(OutputStream outputStream, Object object) {

        Class clazz = (Class) object.getClass();

        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

        try {

            Schema schema = getSchema(clazz);
            ProtostuffIOUtil.writeTo(outputStream, object, schema, buffer);

        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);

        } finally {
            buffer.clear();
        }


    }


    //反序列化
    @Override
    public Object deserialize(InputStream inputStream) {
        try {
            Class clazz = isRpcDirect() ? MessageRequest.class : MessageResponse.class;
            Object object = (Object) objenesis.newInstance(clazz);
            Schema<Object> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(inputStream, object, schema);
            return object;

        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
































































