package com.zou.compiler.proxy;

import com.google.common.collect.ImmutableMap;

import java.lang.reflect.Method;
import java.util.Map;

public class MethodDescriptor {


    private static final Map<Class<?>, Character> BUILDER = new ImmutableMap.Builder<Class<?>, Character>()
            .put(Boolean.TYPE, Character.valueOf('Z'))
            .put(Byte.TYPE, Character.valueOf('B'))
            .put(Short.TYPE, Character.valueOf('S'))
            .put(Integer.TYPE, Character.valueOf('I'))
            .put(Character.TYPE, Character.valueOf('C'))
            .put(Long.TYPE, Character.valueOf('J'))
            .put(Float.TYPE, Character.valueOf('F'))
            .put(Double.TYPE, Character.valueOf('D'))
            .put(Void.TYPE, Character.valueOf('V'))
            .build();


    private final String internal;


    public MethodDescriptor(Method method) {

        final StringBuilder stringBuilder = new StringBuilder(method.getName()).append('(');

        for (Class<?> c : method.getParameterTypes()) {

            appendTo(stringBuilder, c);
        }

        stringBuilder.append(')');
        this.internal = stringBuilder.toString();
    }


    private static void appendTo(StringBuilder stringBuilder, Class<?> type) {

        if (type.isPrimitive()) {
            stringBuilder.append(BUILDER.get(type));
        } else if (type.isArray()) {
            stringBuilder.append('[');
        } else {
            stringBuilder.append('L').append(type.getName().replace('.', '/')).append(';');
        }
    }


    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (o.getClass() != getClass()) {
            return false;
        }

        MethodDescriptor methodDescriptor = (MethodDescriptor) o;
        return methodDescriptor.internal.equalsIgnoreCase(internal);

    }


    @Override
    public int hashCode() {
        return internal.hashCode();
    }

    @Override
    public String toString() {
        return internal;
    }
}













































