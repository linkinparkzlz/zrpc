package com.zou.event;

import org.springframework.context.ApplicationEvent;

public class ServerStartEvent extends ApplicationEvent {

    public ServerStartEvent(Object source) {
        super(source);
    }


}
