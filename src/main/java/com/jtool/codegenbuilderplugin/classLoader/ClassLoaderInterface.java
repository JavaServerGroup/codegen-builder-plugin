package com.jtool.codegenbuilderplugin.classLoader;

public interface ClassLoaderInterface {
    public Class<?> loadClass(String name) throws ClassNotFoundException;
}
