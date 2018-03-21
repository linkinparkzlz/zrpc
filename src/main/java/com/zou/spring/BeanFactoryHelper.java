package com.zou.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class BeanFactoryHelper implements BeanFactoryAware {

    private static BeanFactory beanFactory;


    private static boolean isContains(String[] values, String value) {
        if (value != null && value.length() > 0 && values != null && values.length > 0) {
            for (String v : values) {
                if (value.equals(v)) {
                    return true;
                }
            }
        }

        return false;
    }


    public static <T> T getBean(String name) {

        if (beanFactory == null) {
            return null;
        }

        try {
            return (T) beanFactory.getBean(name);

        } catch (NoSuchBeanDefinitionException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}












































































