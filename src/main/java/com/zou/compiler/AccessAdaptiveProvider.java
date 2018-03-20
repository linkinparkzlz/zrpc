package com.zou.compiler;

import com.google.common.io.Files;
import com.zou.compiler.intercept.SimpleMethodInterceptor;
import com.zou.core.reflection.ReflectionHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

public class AccessAdaptiveProvider extends AbstractAccessAdaptive implements AccessAdaptive {


    @Override
    protected Class<?> doCompile(String className, String javaCode) throws Throwable {


        File tempFileLocation = Files.createTempDir();

        compiler = new NativeCompiler(tempFileLocation);

        Class type = compiler.compile(className, javaCode);

        tempFileLocation.deleteOnExit();

        return type;

    }

    @Override
    public Object invoke(String code, String method, Object[] args) {

        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(method)) {
            return null;
        } else {

            try {

                Class type = compile(code, Thread.currentThread().getContextClassLoader());

                Object object = ReflectionHelper.newInstance(type);
                Thread.currentThread().getContextClassLoader().loadClass(type.getName());

                Object proxy = getFactory().createProxy(object, new SimpleMethodInterceptor(), new Class[]{type});

                return MethodUtils.invokeMethod(proxy, method, args);

            } catch (NoSuchMethodException e) {

                e.printStackTrace();

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}


































































