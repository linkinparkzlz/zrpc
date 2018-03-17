package com.zou.serializable.kryo;


import com.esotericsoftware.kryo.pool.KryoPool;
import com.google.common.io.Closer;
import com.zou.serializable.message.MessageCodeHelper;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class KryoCodecHelper implements MessageCodeHelper {

    private KryoPool pool;

    //guava提供的帮助进行资源关闭的包
    private static Closer closer = Closer.create();


    public KryoCodecHelper(KryoPool pool) {
        this.pool = pool;
    }


    //编码
    @Override
    public void encode(ByteBuf out, Object message) throws IOException {


        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //注册需要关闭的资源
            closer.register(byteArrayOutputStream);

            KryoSerializable kryoSerializable = new KryoSerializable(pool);
            kryoSerializable.serialize(byteArrayOutputStream, message);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            int length = bytes.length;
            out.writeInt(length);
            out.writeBytes(bytes);

        } finally {

            closer.close();

        }


    }

    //解码器构造对象返回
    @Override
    public Object decode(byte[] bytes) throws Exception {

        try {

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            //注册要关闭的资源
            closer.register(byteArrayInputStream);

            KryoSerializable kryoSerializable = new KryoSerializable(pool);

            Object object = kryoSerializable.deserialize(byteArrayInputStream);
            return object;

        } finally {

            closer.close();
        }
    }
}



















































