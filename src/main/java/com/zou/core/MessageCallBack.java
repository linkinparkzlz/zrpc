package com.zou.core;

import com.zou.bean.MessageRequest;
import com.zou.bean.MessageResponse;
import com.zou.config.SystemConfig;
import com.zou.exception.InvokeModuleException;
import com.zou.exception.InvokeTimeoutException;
import com.zou.exception.RejectResponseException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageCallBack {

    private MessageRequest request;
    private MessageResponse response;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();


    public MessageCallBack(MessageRequest request) {
        this.request = request;
    }

    public Object start() {

        try {

            lock.lock();
            await();

            if (this.response != null) {
                boolean isInvokeSuccess = getInvokeResult();

                if (isInvokeSuccess) {

                    if (this.response.getError().isEmpty()) {
                        return this.response.getResult();
                    } else {
                        throw new InvokeModuleException(this.response.getError());
                    }
                } else {
                    throw new RejectResponseException(SystemConfig.FILTER_RESPONSE_MSG);
                }
            } else {
                return null;
            }

        } finally {

            lock.unlock();
        }
    }


    public void over(MessageResponse response) {
        try {

            lock.lock();
            condition.signal();
            this.response = response;

        } finally {

            lock.unlock();

        }
    }


    private void await() {

        boolean timeout = false;

        try {

            timeout = condition.await(SystemConfig.SYSTEM_PROPERTY_MESSAGE_CALLBACK_TIMEOUT, TimeUnit.MICROSECONDS);

        } catch (Exception e) {
            e.printStackTrace();

        }

        if (!timeout) {
            throw new InvokeTimeoutException(SystemConfig.TIMEOUT_RESPONSE_MSG);
        }
    }


    private boolean getInvokeResult() {

        return (!this.response.getError().equals(SystemConfig.FILTER_RESPONSE_MSG) &&
                (!this.response.isReturnNotNull() || (this.response.isReturnNotNull() && this.response.getResult() != null)));
    }


}








































































