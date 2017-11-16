package com.jtool.codegenbuilderplugin.finder;

import com.jtool.codegenannotation.CodeGenExceptionDefine;
import com.jtool.codegenbuilderplugin.BuilderMojo;
import com.jtool.codegenbuilderplugin.model.ExceptionModel;

import java.io.File;
import java.util.ArrayList;
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

        CodeGenExceptionDefine codeGenExceptionDefine = clazz.getAnnotation(CodeGenExceptionDefine.class);
        if(codeGenExceptionDefine != null) {
            ExceptionModel exceptionModel = new ExceptionModel();
            exceptionModel.setCode(Integer.parseInt(codeGenExceptionDefine.code()));
            exceptionModel.setDesc(codeGenExceptionDefine.desc());

            result.add(exceptionModel);
        }

        return result;
    }

}
