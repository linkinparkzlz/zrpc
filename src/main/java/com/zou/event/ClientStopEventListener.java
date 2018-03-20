package com.zou.event;

import com.google.common.eventbus.Subscribe;
import com.zou.netty.executor.MessageSendExecutor;

public class ClientStopEventListener {

    public int lastMessage = 0;

    @Subscribe
    public void listen(ClientStopEvent event) {

        lastMessage = event.getMessage();
        MessageSendExecutor.getInstance().stop();
    }


    public int getLastMessage() {
        return lastMessage;
    }
}
