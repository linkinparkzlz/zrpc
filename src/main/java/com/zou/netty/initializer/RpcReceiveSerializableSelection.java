package com.zou.netty.initializer;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.zou.netty.handlerImpl.hessian.HessianReceiveHandler;
import com.zou.netty.handlerImpl.jdkPrimitive.JdkPrimitiveReceiveHandler;
import com.zou.netty.handlerImpl.kryo.KryoReceiveHandler;
import com.zou.netty.handlerImpl.protostuff.ProtostuffReceiveHandler;
import com.zou.netty.handlerInterface.NettyRpcReceiveHandler;
import com.zou.serializable.message.RpcSerializableSelection;
import com.zou.serializable.rpc.RpcSerializableProtocol;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

public class RpcReceiveSerializableSelection implements RpcSerializableSelection {

    private Map<String, Object> map = null;

    public RpcReceiveSerializableSelection(Map<String, Object> map) {
        this.map = map;
    }

    private static ClassToInstanceMap<NettyRpcReceiveHandler> handler = MutableClassToInstanceMap.create();

    static {

        handler.putInstance(JdkPrimitiveReceiveHandler.class, new JdkPrimitiveReceiveHandler());
        handler.putInstance(KryoReceiveHandler.class, new KryoReceiveHandler());
        handler.putInstance(HessianReceiveHandler.class, new HessianReceiveHandler());
        handler.putInstance(ProtostuffReceiveHandler.class, new ProtostuffReceiveHandler());
    }


    @Override
    public void select(RpcSerializableProtocol protocol, ChannelPipeline pipeline) {


        switch (protocol) {

            case JDK_SERIALIZABLE: {
                handler.getInstance(JdkPrimitiveReceiveHandler.class).handle(map, pipeline);
                break;
            }

            case KRYO_SERIALIZABLE: {
                handler.getInstance(KryoReceiveHandler.class).handle(map, pipeline);
                break;
            }

            case HESSIAN_SERIALIZABLE: {
                handler.getInstance(HessianReceiveHandler.class).handle(map, pipeline);
                break;
            }

            case PROTOBUF_SERIALIZABLE: {
                handler.getInstance(ProtostuffReceiveHandler.class).handle(map, pipeline);
                break;
            }
            default: {
                break;
            }


        }

    }
}
































































































