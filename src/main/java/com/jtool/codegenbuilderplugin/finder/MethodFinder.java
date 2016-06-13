package com.jtool.codegenbuilderplugin.finder;

import com.jtool.codegenannotation.CodeGenApi;
import com.jtool.codegenbuilderplugin.BuilderMojo;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodFinder {

    public static List<Method> findAllCodeGenApiMethod(BuilderMojo builderMojo, List<File> files) {

        List<Method> methodList = new ArrayList<>();

        for (File file : files) {
            methodList.addAll(findMethodWithCodeGenApiFromFile(builderMojo, file));
        }

        return methodList;
    }

    private static List<Method> findMethodWithCodeGenApiFromFile(BuilderMojo builderMojo, File file) {

        List<Method> result = new ArrayList<>();

        if (!file.getAbsolutePath().endsWith(".java")) {
            return result;
        }

        builderMojo.getLog().debug("准备分析的文件：" + file.getAbsolutePath());

        String className = file.getAbsolutePath().replace(builderMojo.getBasedir().getAbsolutePath() + builderMojo.getScanSource(), "").replace(".java", "").replaceAll(File.separator, ".");

        Class<?> clazz;
        try {
            clazz = builderMojo.getClassLoaderInterface().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("没有找到类:" + className);
        }

        //遍历源文件的method
        for (Method method : clazz.getDeclaredMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                // 只保留有@CodeGenApi注解的方法
                if (annotation instanceof CodeGenApi) {
                    result.add(method);
                }
            }
        }

        return result;
    }

}
