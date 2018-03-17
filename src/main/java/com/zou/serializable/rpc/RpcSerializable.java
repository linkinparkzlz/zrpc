package com.zou.serializable.rpc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface RpcSerializable {

    //序列化方法
    void serialize(OutputStream outputStream, Object object) throws IOException;

    //反序列化方法
    Object deserialize(InputStream inputStream) throws IOException;


}




























































