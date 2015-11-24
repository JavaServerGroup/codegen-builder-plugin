package com.jtool.codegenbuilderplugin.test.util;

import com.jtool.codegenbuilderplugin.classLoader.ClassLoaderInterface;

/**
 * Created by jialechan on 15/6/24.
 */
public class TestContextClassLoader implements ClassLoaderInterface {

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

}
