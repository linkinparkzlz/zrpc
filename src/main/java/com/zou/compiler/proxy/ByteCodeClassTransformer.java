package com.zou.compiler.proxy;


import com.zou.compiler.invoker.ObjectInvoker;
import com.zou.core.reflection.ReflectionHelper;
import com.zou.exception.CreateProxyException;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

public class ByteCodeClassTransformer extends AbstractClassTransformer implements Opcodes {


    private static final AtomicLong CLASS_NUMBER = new AtomicLong(0);

    private static final String CLASSNAME_PREFIX = "ASMPROXY_";

    private static final String HANDLER_NAME = "_handler";

    private static final Type INVOKER_TYPE = Type.getType(ObjectInvoker.class);


    @Override
    public Class<?> transform(ClassLoader classLoader, Class<?>... proxyClasses) {
        Class<?> superClass = ReflectionHelper.getParentClass(proxyClasses);
        String proxyName = CLASSNAME_PREFIX + CLASS_NUMBER.incrementAndGet();

        Method[] implementionMethods = super.findImplementationMethods(proxyClasses);

        Class<?>[] interfaces = ReflectionHelper.filterInterfaces(proxyClasses);
        String classFileName = proxyName.replace('.', '/');

        try {
            byte[] proxyBytes = generate(superClass, classFileName, implementionMethods, interfaces);
            return loadClass(classLoader, proxyName, proxyBytes);

        } catch (final Exception e) {
            throw new CreateProxyException(e);

        }
    }


    private byte[] generate(Class<?> classProxy, String proxyName, Method[] methods, Class<?>... interfaces) throws CreateProxyException {

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        Type proxyType = Type.getObjectType(proxyName);

        String[] interfaceNames = new String[interfaces.length];

        for (int i = 0; i < interfaces.length; i++) {
            interfaceNames[i] = Type.getType(interfaces[i]).getInternalName();
        }


        Type superType = Type.getType(classProxy);

        classWriter.visit(V1_6, ACC_PUBLIC + ACC_SUPER, proxyType.getInternalName(), null, superType.getInternalName(), interfaceNames);

        classWriter.visitField(ACC_FINAL + ACC_PRIVATE, HANDLER_NAME, INVOKER_TYPE.getDescriptor(), null, null).visitEnd();

        initialize(classWriter, proxyType, superType);

        for (final Method method : methods) {
            transformMethod(classWriter, method, proxyType, HANDLER_NAME);
        }

        return classWriter.toByteArray();


    }


    private void initialize(ClassWriter classWriter, Type proxyType, Type superType) {

        GeneratorAdapter adapter = new GeneratorAdapter(ACC_PUBLIC, new org.objectweb.asm.commons.Method("<init>", Type.VOID_TYPE,
                new Type[]{INVOKER_TYPE}), null, null, classWriter);
        adapter.loadThis();
        adapter.invokeConstructor(superType, org.objectweb.asm.commons.Method.getMethod("void <init> ()"));
        adapter.loadThis();
        adapter.loadArg(0);
        adapter.putField(proxyType, HANDLER_NAME, INVOKER_TYPE);
        adapter.returnValue();
        adapter.endMethod();

    }


    private void transformMethod(ClassWriter classWriter, Method method, Type proxyType, String handlerName) throws CreateProxyException {

        int access = (ACC_PUBLIC | ACC_PROTECTED) & method.getModifiers();

        org.objectweb.asm.commons.Method method1 = org.objectweb.asm.commons.Method.getMethod(method);

        GeneratorAdapter adapter = new GeneratorAdapter(access, method1, null, getTypes(method.getExceptionTypes()), classWriter);


        adapter.push(Type.getType(method.getDeclaringClass()));
        adapter.push(method.getName());
        adapter.push(Type.getArgumentTypes(method).length);


        //创建Class对象
        Type classType = Type.getType(Class.class);
        adapter.newArray(classType);

        //获取方法参数列表
        for (int i = 0; i < Type.getArgumentTypes(method).length; i++) {

            adapter.dup();

            adapter.push(i);
            adapter.push(Type.getArgumentTypes(method)[i]);
            adapter.arrayStore(classType);
        }

        adapter.invokeVirtual(classType, org.objectweb.asm.commons.Method.getMethod("java.lang.reflect.Method getDeclaredMethod(String,Class[])"));

        adapter.loadThis();
        adapter.getField(proxyType, handlerName, INVOKER_TYPE);


        adapter.swap();

        adapter.loadThis();

        adapter.swap();


        //获取方法的参数值列表
        adapter.push(Type.getArgumentTypes(method).length);
        Type objectType = Type.getType(Object.class);
        adapter.newArray(objectType);

        for (int i = 0; i < Type.getArgumentTypes(method).length; i++) {
            adapter.dup();

            adapter.push(i);

            adapter.loadArg(i);

            adapter.valueOf(Type.getArgumentTypes(method)[i]);
            adapter.arrayStore(objectType);
        }


        //
        adapter.invokeInterface(INVOKER_TYPE, org.objectweb.asm.commons.Method.getMethod("Object invoke(Object,java.lang.reflect.Method,Object[])"));

        //
        adapter.unbox(Type.getReturnType(method));

        adapter.returnValue();

        adapter.endMethod();


    }


    private Type[] getTypes(Class<?>... src) {
        Type[] types = new Type[src.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = Type.getType(src[i]);
        }

        return types;
    }


    //类加载

    private Class<?> loadClass(ClassLoader classLoader, String className, byte[] bytes) {

        try {

            Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);

            boolean accessible = method.isAccessible();
            if (!accessible) {
                method.setAccessible(true);
            }


            try {
                return (Class<?>) method.invoke(classLoader, className, bytes, Integer.valueOf(0), Integer.valueOf(bytes.length));
            } finally {

                if (!accessible) {
                    method.setAccessible(false);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException(e);

        }

    }


}























































































































