package com.zou.serializable.rpc;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author zlz
 * <p>
 * 序列化协议的选择
 */
public enum RpcSerializableProtocol {

    JDK_SERIALIZABLE("jdk_serializable"), KRYO_SERIALIZABLE("kryo"), HESSIAN_SERIALIZABLE("hessian"), PROTOBUF_SERIALIZABLE("protobuf");

    private String serialiableProtocol;

    RpcSerializableProtocol(String serialiableProtocol) {
        this.serialiableProtocol = serialiableProtocol;
    }

    //使用 commons.lang3 包下提供的字符串处理工具类
    @Override
    public String toString() {
        ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toString(this);
    }

    //获取序列化协议的方法
    public String getProtocol() {
        return serialiableProtocol;
    }
}










































































































