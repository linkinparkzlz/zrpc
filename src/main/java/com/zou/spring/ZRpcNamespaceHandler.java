package com.zou.spring;

import com.google.common.io.CharStreams;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class ZRpcNamespaceHandler extends NamespaceHandlerSupport {

    static {

        Resource resource = new ClassPathResource("ZRpc-logo.txt");

        if (resource.exists()) {

            try {
                Reader reader = new InputStreamReader(resource.getInputStream(), "UTF-8");
                String text = CharStreams.toString(reader);
                System.out.println(text);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            System.out.println("---------      ---------------        -----------------       ---------------");
            System.out.println("------- /      ---------------        -----------------       ---------------");
            System.out.println("     / /       | |         / /        | |            | |    / /");
            System.out.println("    / /        | |        / /         | |            | |   / /");
            System.out.println("   / /         | |       / /          | |            | |  / /");
            System.out.println("--/-------     | |      / /           | |            | | / /");
            System.out.println("----------     | |     / /            | |--------------- \\ \\");
            System.out.println("               | |    / /             | |---------------  \\ \\");
            System.out.println("               | |   / /              | |                  \\ \\");
            System.out.println("               | |   \\ \\            | |                   \\ \\");
            System.out.println("               | |    \\ \\           | |                    \\ \\---------------");
            System.out.println("               | |     \\ \\          | |                    -------------------");
            System.out.println("               | |      \\ \\         | |");
            System.out.println("");
        }

    }

    @Override
    public void init() {

        registerBeanDefinitionParser("service", new ZRpcServiceParser());
        registerBeanDefinitionParser("registry", new ZRpcRegisteryParser());
        registerBeanDefinitionParser("reference", new ZRpcReferenceParser());


    }
}










































































