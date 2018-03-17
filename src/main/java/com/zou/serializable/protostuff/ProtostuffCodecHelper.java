package com.zou.serializable.protostuff;

import com.google.common.io.Closer;
import com.zou.serializable.message.MessageCodeHelper;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProtostuffCodecHelper implements MessageCodeHelper {

    private static Closer closer = Closer.create();

    private ProtostuffSerializablePool pool = ProtostuffSerializablePool.getProtostuffInstance();

    private boolean rpcDirect = false;

    public boolean isRpcDirect() {
        return rpcDirect;
    }

    public void setRpcDirect(boolean rpcDirect) {
        this.rpcDirect = rpcDirect;
    }

    //编码
    @Override
    public void encode(ByteBuf out, Object message) throws IOException {

        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            //注册好需要关闭的资源
            closer.register(byteArrayOutputStream);

            ProtostuffSerializable protostuffSerializable = pool.borrow();
            protostuffSerializable.serialize(byteArrayOutputStream, message);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            int length = bytes.length;
            out.writeInt(length);
            out.writeBytes(bytes);

            pool.restore(protostuffSerializable);

        } finally {
            closer.close();
        }
    }

    //解码，返回一个对象
    @Override
    public Object decode(byte[] bytes) throws Exception {

        try {

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            closer.register(byteArrayInputStream);

            ProtostuffSerializable protostuffSerializable = pool.borrow();
            protostuffSerializable.setRpcDirect(rpcDirect);

            Object object = protostuffSerializable.deserialize(byteArrayInputStream);
            pool.restore(protostuffSerializable);
            return object;

        } finally {

            closer.close();

        }

    }
}




























































