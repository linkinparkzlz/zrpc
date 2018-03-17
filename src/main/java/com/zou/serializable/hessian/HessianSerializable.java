package com.zou.serializable.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.zou.serializable.rpc.RpcSerializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 使用Hessian帮助实现序列化
 */
public class HessianSerializable implements RpcSerializable {


    /**
     * 使用Hessian实现序列化
     *
     * @param outputStream
     * @param object
     * @throws IOException
     */
    @Override
    public void serialize(OutputStream outputStream, Object object) {

        Hessian2Output hessian2Output = new Hessian2Output(outputStream);

        try {

            //将对象变为字节流
            hessian2Output.startMessage();
            hessian2Output.writeObject(object);
            hessian2Output.completeMessage();
            hessian2Output.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 使用Hessian实现反序列化
     *
     * @param inputStream
     * @return
     * @throws IOException
     */

    @Override
    public Object deserialize(InputStream inputStream) {

        Object object = null;

        try {

            //将字节流反序列化为对象
            Hessian2Input hessian2Input = new Hessian2Input(inputStream);
            hessian2Input.startMessage();
            object = hessian2Input.readObject();
            hessian2Input.completeMessage();
            hessian2Input.close();
            inputStream.close();


        } catch (IOException e) {

            e.printStackTrace();

        }

        return object;
    }
}
























































