package com.zou.start;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcServerStarter {

    public static void main(String[] args) {

        new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-server.xml");

    }

}
