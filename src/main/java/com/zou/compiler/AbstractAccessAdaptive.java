package com.zou.compiler;

import com.zou.compiler.proxy.CLassProxy;
import com.zou.compiler.proxy.ProxyProvider;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractAccessAdaptive implements Compiler {

    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([$_a-zA-Z][$_a-zA-Z0-9\\.]*);");

    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s+");

    private static final String CLASS_END_FLAG = "}";

    protected CLassProxy factory = new ProxyProvider();

    protected NativeCompiler compiler = null;


    protected ClassLoader overrideThreadContextClassLoader(ClassLoader classLoader) {

        Thread currentThread = Thread.currentThread();
        ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();

        if (classLoader != null && !classLoader.equals(threadContextClassLoader)) {
            currentThread.setContextClassLoader(classLoader);
            return threadContextClassLoader;
        } else {
            return null;
        }

    }


    protected ClassLoader getClassLoader() {

        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();

        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (classLoader == null) {
            classLoader = AbstractAccessAdaptive.class.getClassLoader();

            if (classLoader == null) {
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        return classLoader;
    }


    private String report(Throwable e) {

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.print(e.getClass().getName() + ": ");

        if (e.getMessage() != null) {
            printWriter.print(e.getMessage() + "\n");
        }
        printWriter.println();

        try {
            e.printStackTrace(printWriter);
            return stringWriter.toString();
        } finally {
            printWriter.close();
        }

    }


    @Override
    public Class<?> compile(String code, ClassLoader classLoader) {

        code = code.trim();

        Matcher matcher = PACKAGE_PATTERN.matcher(code);

        String pkg;

        if (matcher.find()) {
            pkg = matcher.group(1);
        } else {
            pkg = "";
        }

        matcher = CLASS_PATTERN.matcher(code);

        String cls;
        if (matcher.find()) {

            cls = matcher.group(1);
        } else {
            throw new IllegalArgumentException("no class name " + code);
        }

        String className = pkg != null && pkg.length() > 0 ? pkg + "." + cls : cls;

        try {
            return Class.forName(className, true, (classLoader != null ? classLoader : getClassLoader()));

        } catch (ClassNotFoundException e) {

            if (!code.endsWith(CLASS_END_FLAG)) {
                throw new IllegalStateException("the java code not ends with \"}\".code:\n " + code + "\n");
            }

            try {
                return doCompile(className, code);
            } catch (RuntimeException t) {
                throw t;

            } catch (Throwable t) {

                throw new IllegalStateException("failed to compile class,cause :" + t.getMessage() + ", class" + className + ", code: \n" + code + "\n,stack: " + report(t));
            } finally {
                overrideThreadContextClassLoader(compiler.getClassLoader());

                compiler.close();
            }

        }


    }


    protected abstract Class<?> doCompile(String className, String javaCode) throws Throwable;

    public CLassProxy getFactory() {
        return factory;
    }
}


































