package com.zou.filter.support;

import com.zou.filter.Filter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

public class SimpleFilter implements Filter {

    @Override
    public boolean before(Method method, Object processor, Object[] requestObject) {
        System.out.println(StringUtils.center("[SimpleFilter ## before]", 48, "*"));
        return true;
    }

    @Override
    public void after(Method method, Object processor, Object[] requestObject) {

        System.out.println(StringUtils.center("[SimpleFilter##after]", 48, "*"));
    }
}





































