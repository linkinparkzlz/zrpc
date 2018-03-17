package com.zou.netty.initializer;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.zou.netty.handlerImpl.hessian.HessianSendHandler;
import com.zou.netty.handlerImpl.jdkPrimitive.JdkPrimitiveSendHandler;
import com.zou.netty.handlerImpl.kryo.KryoSendHandler;
import com.zou.netty.handlerImpl.protostuff.ProtostuffSendHandler;
import com.zou.netty.handlerInterface.NettyRpcSendHandler;
import com.zou.serializable.message.RpcSerializableSelection;
import com.zou.serializable.rpc.RpcSerializableProtocol;
import io.netty.channel.ChannelPipeline;

public class RpcSendSerializableSelection implements RpcSerializableSelection {


    private static ClassToInstanceMap<NettyRpcSendHandler> handler = MutableClassToInstanceMap.create();

    static {

        handler.putInstance(JdkPrimitiveSendHandler.class, new JdkPrimitiveSendHandler());
        handler.putInstance(KryoSendHandler.class, new KryoSendHandler());
        handler.putInstance(HessianSendHandler.class, new HessianSendHandler());
        handler.putInstance(ProtostuffSendHandler.class, new ProtostuffSendHandler());

    }

    @Override
    public void select(RpcSerializableProtocol protocol, ChannelPipeline pipeline) {

        switch (protocol) {

            case JDK_SERIALIZABLE: {
                handler.getInstance(JdkPrimitiveSendHandler.class).handle(pipeline);
                break;
            }

            case KRYO_SERIALIZABLE: {
                handler.getInstance(KryoSendHandler.class).handle(pipeline);
                break;
            }

            case HESSIAN_SERIALIZABLE: {
                handler.getInstance(HessianSendHandler.class).handle(pipeline);
                break;
            }
            case PROTOBUF_SERIALIZABLE: {
                handler.getInstance(ProtostuffSendHandler.class).handle(pipeline);
                break;
            }
            default: {
                break;
            }

        }

    }
}


































