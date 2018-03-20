package com.zou.compiler.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public abstract class AbstractClassTransformer implements Transformer {


    @Override
    public Class<?> transform(ClassLoader classLoader, Class<?>... proxyClasses) {
        return null;
    }

    protected Method[] findImplementationMethods(Class<?>[] proxyClasses) {


        Map<MethodDescriptor, Method> map = new HashMap<>(1024);

        Set<MethodDescriptor> set = new HashSet<>();

        for (int i = 0; i < proxyClasses.length; i++) {

            Class<?> clazz = proxyClasses[i];

            Method[] methods = clazz.getMethods();

            for (int j = 0; j < methods.length; j++) {

                MethodDescriptor methodDescriptor = new MethodDescriptor(methods[j]);

                if (Modifier.isFinal(methods[j].getModifiers())) {
                    set.add(methodDescriptor);
                } else if (!map.containsKey(methodDescriptor)) {
                    map.put(methodDescriptor, methods[j]);
                }
            }
        }

        Collection<Method> collection = map.values();

        for (MethodDescriptor descriptor : set) {
            collection.remove(map.get(descriptor));
        }

        return collection.toArray(new Method[collection.size()]);


    }


}








































