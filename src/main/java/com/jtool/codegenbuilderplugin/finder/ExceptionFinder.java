package com.jtool.codegenbuilderplugin.finder;

import com.jtool.codegenannotation.CodeGenExceptionDefine;
import com.jtool.codegenannotation.CodeGenExceptionTypeEnum;
import com.jtool.codegenbuilderplugin.BuilderMojo;
import com.jtool.codegenbuilderplugin.model.ExceptionModel;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExceptionFinder {

    public static List<ExceptionModel> findAllCodeGenException(BuilderMojo builderMojo, List<File> files) {

        List<ExceptionModel> result = new ArrayList<>();

        for (File file : files) {
            result.addAll(findCodeGenExceptionFromFile(builderMojo, file));
        }

        return result;
    }

    private static List<ExceptionModel> findCodeGenExceptionFromFile(BuilderMojo builderMojo, File file) {

        List<ExceptionModel> result = new ArrayList<>();

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

        CodeGenExceptionTypeEnum codeGenExceptionTypeEnum = clazz.getAnnotation(CodeGenExceptionTypeEnum.class);
        if(codeGenExceptionTypeEnum != null) {
            try {
                Method getCode = clazz.getMethod("getCode");
                Method getDesc = clazz.getMethod("getDesc");
                //得到enum的所有实例
                for (Object obj : clazz.getEnumConstants()) {

                    ExceptionModel exceptionModel = new ExceptionModel();
                    exceptionModel.setCode(Integer.valueOf((String)getCode.invoke(obj)));
                    exceptionModel.setDesc((String)getDesc.invoke(obj));

                    result.add(exceptionModel);

                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }



        }

        return result;
    }

}
