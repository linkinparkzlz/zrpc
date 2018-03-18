package com.zou.bean;


import java.io.Serializable;

/**
 * @author zlz
 * <p>
 * 此类的作用是定义RPC调用中响应消息的结构
 */


//此类是需要能够进行序列化的
public class MessageResponse implements Serializable {

    //响应消息的字段

    //消息ID
    private String messageId;

    private String error;

    private Object result;

    private boolean returnNotNull;


    //生成get和set方法


    public boolean isReturnNotNull() {
        return returnNotNull;
    }

    public void setReturnNotNull(boolean returnNotNull) {
        this.returnNotNull = returnNotNull;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


    //生成toString方法

    @Override
    public String toString() {
        return "MessageResponse{" +
                "messageId='" + messageId + '\'' +
                ", error='" + error + '\'' +
                ", result=" + result +
                '}';
    }
}
