package com.zou.serializable.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.zou.serializable.rpc.RpcSerializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class KryoSerializable implements RpcSerializable {


    private KryoPool pool = null;

    public KryoSerializable(KryoPool pool) {
        this.pool = pool;
    }

    //序列化
    @Override
    public void serialize(OutputStream outputStream, Object object) throws IOException {

        Kryo kryo = pool.borrow();
        Output output = new Output(outputStream);
        kryo.writeClassAndObject(output, object);
        output.close();
        outputStream.close();
        pool.release(kryo);
    }


    //反序列化
    @Override
    public Object deserialize(InputStream inputStream) throws IOException {
        Kryo kryo = pool.borrow();

        Input input = new Input(inputStream);
        Object object = kryo.readClassAndObject(input);
        input.canReadInt();
        inputStream.close();
        pool.release(kryo);
        return object;
    }
}













































