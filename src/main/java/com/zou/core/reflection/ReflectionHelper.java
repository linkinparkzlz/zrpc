package com.zou.core.reflection;

import com.google.common.collect.ImmutableMap;
import com.zou.exception.CreateProxyException;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReflectionHelper {


    private static ImmutableMap.Builder<Class, Object> builder = ImmutableMap.builder();

    private StringBuilder provider = new StringBuilder();

    public StringBuilder getProvider() {
        return provider;
    }


    public void clearProvider() {
        provider.delete(0, provider.length());
    }


    static {
        builder.put(Boolean.class, Boolean.FALSE);
        builder.put(Byte.class, Byte.valueOf((byte) 0));
        builder.put(Character.class, Character.valueOf((char) 0));
        builder.put(Short.class, Short.valueOf((short) 0));
        builder.put(Double.class, Double.valueOf(0));
        builder.put(Float.class, Float.valueOf(0));
        builder.put(Integer.class, Integer.valueOf(0));
        builder.put(Long.class, Long.valueOf(0));
        builder.put(boolean.class, Boolean.FALSE);
        builder.put(byte.class, Byte.valueOf((byte) 0));
        builder.put(char.class, Character.valueOf((char) 0));
        builder.put(short.class, Short.valueOf((short) 0));
        builder.put(double.class, Double.valueOf(0));
        builder.put(float.class, Float.valueOf(0));
        builder.put(int.class, Integer.valueOf(0));
        builder.put(long.class, Long.valueOf(0));
    }


    public static Class<?>[] filterInterfaces(Class[] proxyClasses) {

        Set<Class<?>> interfaces = new HashSet<>();

        for (Class<?> proxyClass : proxyClasses) {

            if (!proxyClass.isInterface()) {
                interfaces.add(proxyClass);
            }
        }

        interfaces.add(Serializable.class);

        return interfaces.toArray(new Class[interfaces.size()]);
    }

    public static Class<?>[] filterNonInterfaces(Class<?>[] proxyClasses) {
        Set<Class<?>> superClasses = new HashSet<Class<?>>();

        for (Class<?> proxyClass : proxyClasses) {
            if (proxyClass.isInterface()) {
                superClasses.add(proxyClass);
            }
        }

        return superClasses.toArray(new Class[superClasses.size()]);

    }


    public static boolean existDefaultConstructor(Class<?> superClass) {


        final Constructor<?>[] declaredConstructors = superClass.getDeclaredConstructors();

        for (int i = 0; i < declaredConstructors.length; i++) {

            Constructor<?> constructor = declaredConstructors[i];

            //是不是默认的构造方法
            boolean exist = (constructor.getParameterTypes().length == 0
                    && (Modifier.isPublic(constructor.getModifiers())
                    || Modifier.isProtected(constructor.getModifiers())));


            if (exist) {
                return true;
            }
        }

        return false;

    }


    public static Class<?> getParentClass(Class<?>[] proxyClasses) {


        final Class<?>[] parent = filterNonInterfaces(proxyClasses);

        switch (parent.length) {

            case 0:
                return Object.class;

            case 1:
                Class<?> superClass = parent[0];
                if (Modifier.isFinal(superClass.getModifiers())) {
                    throw new CreateProxyException("proxy not build " + superClass.getName() + "  is   final ");
                }
                if (!existDefaultConstructor(superClass)) {
                    throw new CreateProxyException("not build  " + superClass.getName() + "  has  no  default constructor");
                }
                return superClass;
            default:
                StringBuilder errorMessage = new StringBuilder("proxy class not build");
                for (int i = 0; i < parent.length; i++) {

                    Class<?> clazz = parent[i];

                    errorMessage.append(clazz.getName());

                    if (i != parent.length - 1) {
                        errorMessage.append(", ");
                    }
                }

                errorMessage.append(";  multiple  implement not allowed");
                throw new CreateProxyException(errorMessage.toString());

        }


    }


    public static boolean isHashCodeMethod(Method method) {

        return "hashCode".equals(method.getName()) && Integer.TYPE.equals(method.getReturnType())
                && method.getParameterTypes().length == 0;
    }


    public static boolean isEqualsMethod(Method method) {

        return "equals".equals(method.getName()) && Boolean.TYPE.equals(method.getReturnType())
                && method.getParameterTypes().length == 1 && Object.class.equals(method.getParameterTypes()[0]);
    }


    public static Object newInstance(Class type) {

        Constructor constructor = null;
        Object[] args = new Object[0];

        try {
            constructor = type.getConstructor(new Class[]{});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        if (constructor == null) {

            Constructor[] constructors = type.getConstructors();

            if (constructors.length == 0) {
                return null;
            }

            constructor = constructors[0];
            Class[] params = constructor.getParameterTypes();
            args = new Object[params.length];

            for (int i = 0; i < params.length; i++) {
                args[i] = getDefaultValue(params[i]);
            }
        }

        try {
            return constructor.newInstance(args);
        } catch (InstantiationException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }


    public static Object getDefaultValue(Class clazz) {

        if (clazz.isArray()) {
            return Array.newInstance(clazz.getComponentType(), 0);
        } else if (clazz.isPrimitive() || builder.build().containsKey(clazz)) {
            return builder.build().get(clazz);
        } else {
            return newInstance(clazz);
        }

    }


    public static Class<?> getGenericClass(ParameterizedType parameterizedType, int i) {

        Object genericClass = parameterizedType.getActualTypeArguments()[i];

        if (genericClass instanceof GenericArrayType) {

            return (Class<?>) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) genericClass).getRawType();
        } else {
            return (Class<?>) genericClass;
        }

    }


    private String modifiers(int m) {
        return m != 0 ? Modifier.toString(m) + "  " : "";
    }


    private String getType(Class<?> clazz) {

        String brackets = "";
        while (clazz.isArray()) {
            brackets += "[]";
            clazz = clazz.getComponentType();
        }

        return clazz.getName() + brackets;
    }


    public void listTypes(Class<?>[] types) {

        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                provider.append(", ");
            }
            provider.append(getType(types[i]));
        }
    }


    private void listField(Field field, boolean html) {
        provider.append((html ? "&nbsp&nbsp" : "  ") + modifiers(field.getModifiers()) +
                getType(field.getType()) + " " +
                field.getName() + (html ? ";<br>" : ";\n"));

    }


    public void listMethod(Executable executable, boolean html) {

        provider.append(html ? "<br>&nbsp&nbsp" : "\n " + modifiers(executable.getModifiers() & (~Modifier.FINAL)));
        if (executable instanceof Method) {

            provider.append(getType(((Method) executable).getReturnType()) + " ");

        }
        provider.append(executable.getName() + "(");
        listTypes(executable.getParameterTypes());
        provider.append(")");
        Class<?>[] exceptions = executable.getExceptionTypes();
        if (exceptions.length > 0) {
            provider.append(" throws ");
        }
        listTypes(exceptions);
        provider.append(";");

    }


    public void listRpcProviderDetail(Class<?> clazz, boolean html) {
        if (!clazz.isInterface()) {
            return;
        } else {

            provider.append(Modifier.toString(clazz.getModifiers()) + " " + clazz.getName());
            provider.append(html ? " {<br>" : " {\n");

            boolean hasFields = false;

            Field[] fields = clazz.getDeclaredFields();

            if (fields.length != 0) {
                provider.append(html ? "&nbsp&nbsp//&nbspFields<br>" : "  // Fields\n");
                hasFields = true;
                for (Field field : fields) {
                    listField(field, html);
                }
            }

            provider.append(hasFields ? (html ? "<br>&nbsp&nbsp//&nbspMethods" : "\n  // Methods") : (html ? "&nbsp&nbsp//&nbspMethods" : "  // Methods"));

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                listMethod(method, html);
            }

            provider.append(html ? "<br>}<p>" : "\n}\n\n");


        }


    }


    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {

        Method method = null;

        Class<?> searchType = clazz;
        while (searchType != null) {

            method = findDeclaredMethod(searchType, methodName, parameterTypes);
            if (method != null) {
                return method;
            }
            searchType = searchType.getSuperclass();
        }
        return method;
    }


    public static Method findDeclaredMethod(final Class<?> clazz, String methodName, final Class<?>... parameterTypes) {
        Method method = null;

        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
            return method;
        } catch (NoSuchMethodException e) {

            for (Method m : clazz.getDeclaredMethods()) {

                if (m.getName().equals(methodName)) {

                    boolean find = true;
                    Class[] paramType = m.getParameterTypes();
                    if (paramType.length != parameterTypes.length) {
                        continue;
                    }

                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (!paramType[i].isAssignableFrom(parameterTypes[i])) {
                            find = false;
                            break;
                        }
                    }

                    if (find) {
                        method = m;
                        break;
                    }
                }
            }


        }

        return method;


    }


    private String getClassType(Class<?>[] types) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(getType(types[i]));
        }
        return stringBuilder.toString();
    }


    public List<String> getClassMethodSingature(Class<?> clazz) {

        List<String> list = new ArrayList<>();
        if (clazz.isInterface()) {

            Method[] methods = clazz.getDeclaredMethods();
            StringBuilder stringBuilder = new StringBuilder();
            for (Method method : methods) {

                int modifiers = method.getModifiers();

                if (Modifier.isAbstract(modifiers) && Modifier.isPublic(modifiers)) {

                    stringBuilder.append(modifiers(Modifier.PUBLIC));

                    if (Modifier.isFinal(modifiers)) {
                        stringBuilder.append(modifiers(Modifier.FINAL));
                    }


                } else {

                    stringBuilder.append(modifiers);
                }

                if (method instanceof Method) {
                    stringBuilder.append(getType(((Method) method).getReturnType()) + " ");
                }

                stringBuilder.append(method.getName() + "(");
                stringBuilder.append(getClassType(method.getParameterTypes()));
                stringBuilder.append(")");
                Class<?>[] exceptions = method.getExceptionTypes();
                if (exceptions.length > 0) {
                    stringBuilder.append(" throws ");
                }

                listTypes(exceptions);
                stringBuilder.append(";");
                list.add(stringBuilder.toString());
                stringBuilder.delete(0, stringBuilder.length());

            }


        }

        return list;

    }


}
















































