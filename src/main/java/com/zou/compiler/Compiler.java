package com.zou.compiler;

public interface Compiler {

    Class<?> compile(String code, ClassLoader classLoader);
}
