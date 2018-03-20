package com.zou.netty.executor;

import com.google.common.reflect.Reflection;
import com.google.common.reflect.Reflection;
import com.zou.netty.initializer.MessageSendProxy;
import com.zou.netty.task.RpcServerLoader;
import com.zou.serializable.rpc.RpcSerializableProtocol;

public class MessageSendExecutor {

    private static class MessageSendExecutorHolder {

        private static final MessageSendExecutor INSTANCE = new MessageSendExecutor();

    }

    public static MessageSendExecutor getInstance() {

        return MessageSendExecutorHolder.INSTANCE;
    }

    private RpcServerLoader rpcServerLoader = RpcServerLoader.getInstance();

    private MessageSendExecutor() {

    }


    public MessageSendExecutor(String serverAddress, RpcSerializableProtocol rpcSerializableProtocol) {
        rpcServerLoader.load(serverAddress, rpcSerializableProtocol);
    }

    public void setRpcServerLoader(String serverAddress, RpcSerializableProtocol rpcSerializableProtocol) {

        rpcServerLoader.load(serverAddress, rpcSerializableProtocol);
    }

    public void stop() {

        rpcServerLoader.unLoad();
    }


    public <T> T execute(Class<T> rpcInterface) throws Exception {

        return (T) Reflection.newProxy(rpcInterface, new MessageSendProxy<T>());
    }


}











































