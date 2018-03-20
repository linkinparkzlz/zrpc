package com.test.zou;

import com.google.common.io.CharStreams;
import com.zou.compiler.AccessAdaptive;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class RpcServerAccessTest {


    public static void main(String[] args) {

        try {
            DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();

            Reader input = new InputStreamReader(defaultResourceLoader.getResource("Accessprovider.tpl").getInputStream(), "UTF-8");

            String javaSource = CharStreams.toString(input);

            ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");

            AccessAdaptive accessAdaptive = (AccessAdaptive) classPathXmlApplicationContext.getBean("access");

            String result = (String) accessAdaptive.invoke(javaSource, "getRpcServerTime", new Object[]{new String("ZZZZ")});


            System.out.println(result);


            accessAdaptive.invoke(javaSource, "sayHello", new Object[0]);

            input.close();

            classPathXmlApplicationContext.destroy();

        } catch (IOException e) {


            e.printStackTrace();
        }
    }


}








































