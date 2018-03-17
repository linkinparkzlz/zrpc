package com.zou.serializable.hessian;

import com.google.common.io.Closer;
import com.zou.serializable.message.MessageCodeHelper;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianCodecHelper implements MessageCodeHelper {

    private HessianSerializablePool pool = HessianSerializablePool.getHessianPoolInstance();

    /*
    使用google的guava ，进行资源的关闭
     */
    private static Closer closer = Closer.create();

    public HessianCodecHelper() {

    }

    //编码
    @Override
    public void encode(ByteBuf out, Object message) throws IOException {

        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            //将流对象注册到Closer对象上，资源使用完后，调用close()方法，就可以把资源关闭掉
            closer.register(byteArrayOutputStream);

            HessianSerializable hessianSerializable = pool.borrow();
            hessianSerializable.serialize(byteArrayOutputStream, message);

            byte[] bytes = byteArrayOutputStream.toByteArray();

            int length = bytes.length;

            out.writeInt(length);
            out.writeBytes(bytes);

            pool.restore(hessianSerializable);

        } finally {
            closer.close();
        }


    }

    //解码
    @Override
    public Object decode(byte[] bytes) throws Exception {

        try {

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            closer.register(byteArrayInputStream);

            HessianSerializable hessianSerializable = pool.borrow();

            Object object = hessianSerializable.deserialize(byteArrayInputStream);

            pool.restore(hessianSerializable);

            return object;

        } finally {
            closer.close();
        }


    }
}


































































