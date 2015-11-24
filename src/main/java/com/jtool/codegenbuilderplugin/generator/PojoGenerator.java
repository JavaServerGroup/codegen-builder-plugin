package com.jtool.codegenbuilderplugin.generator;

import com.jtool.codegenbuilderplugin.BuilderMojo;
import com.jtool.codegenbuilderplugin.model.CodeGenModel;
import com.jtool.codegenbuilderplugin.model.ParamModel;
import com.jtool.codegenbuilderplugin.model.RequestParamModel;
import com.jtool.codegenbuilderplugin.model.ResponseParamModel;
import com.squareup.javapoet.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.lang.model.element.Modifier;

public class PojoGenerator {

    private static final String requestPackageName = "com.jtool.codegen.api.request";
    private static final String responsePackageName = "com.jtool.codegen.api.response";

    public static void genPojo(BuilderMojo builderMojo, List<CodeGenModel> apiModelList) throws IOException, ClassNotFoundException {
        for (CodeGenModel codeGenModel : apiModelList) {
            if(codeGenModel.isGenSDK()) {
                //生成请求pojo
                genRequestPojo(builderMojo, codeGenModel.getRequestPojoName(), codeGenModel.getRequestParamModelList());
                //生成返回pojo
                genResponsePojo(builderMojo, codeGenModel.getResponsePojoName(), codeGenModel.getResponseParamModelList());
            }
        }
    }

    private static void genRequestPojo(BuilderMojo builderMojo, String pojoName, List<RequestParamModel> requestParamModelList) throws ClassNotFoundException, IOException {

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(pojoName).addModifiers(Modifier.PUBLIC);

        //生成字段，get,set方法
        for(RequestParamModel requestParamModel : requestParamModelList){
            //根据java系统自有的类型生成字段，get, set方法
            genFieldAndGetSetMethodFromJavaSystemType(typeSpecBuilder, requestParamModel);
        }

        //生成toString()
        genToString(pojoName, requestParamModelList, typeSpecBuilder);

        //生成pojo
        genPojoToFile(typeSpecBuilder, builderMojo.getOutPath() + "android/", requestPackageName);
    }

    private static void genFieldAndGetSetMethodFromJavaSystemType(TypeSpec.Builder typeSpecBuilder, ParamModel paramModel) {
        String type = paramModel.getType();
        String packageNameStr = makePackageName(type);
        String classNameStr = makeClassName(type);
        String key = paramModel.getKey();

        ClassName className = ClassName.get(packageNameStr, classNameStr);

        genFieldAndGetSetMethod(typeSpecBuilder, key, className, paramModel);
    }

    private static void genFieldAndGetSetMethod(TypeSpec.Builder typeSpecBuilder, String key, TypeName type, ParamModel paramModel) {
        //生成字段
        FieldSpec fieldSpec = FieldSpec.builder(type, key)
                .addModifiers(Modifier.PRIVATE)
                .addJavadoc(paramModel.getComment())
                .build();

        typeSpecBuilder.addField(fieldSpec);

        //生成get方法
        MethodSpec getMethodSpec = MethodSpec.methodBuilder("get" + Character.toUpperCase(key.charAt(0)) + key.substring(1))
                .addModifiers(Modifier.PUBLIC)
                .returns(type)
                .addStatement("return this." + key)
                .build();

        typeSpecBuilder.addMethod(getMethodSpec);

        //生成set方法
        MethodSpec setMethodSpec = MethodSpec.methodBuilder("set" + Character.toUpperCase(key.charAt(0)) + key.substring(1))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(type, key)
                .addStatement("this." + key + " = " + key)
                .build();

        typeSpecBuilder.addMethod(setMethodSpec);
    }

    private static void genToString(String pojoName, List<? extends ParamModel> paramModelList, TypeSpec.Builder typeSpecBuilder) {
        MethodSpec.Builder toStringMethodSpecBuilder = MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Override.class)
                .addCode("return $S", pojoName + " { ")
                .addCode(" + ");

        for(ParamModel paramModel : paramModelList){
            toStringMethodSpecBuilder.addCode("$S + " + paramModel.getKey() + " + $S + ", paramModel.getKey() + "='", "'\t");
        }

        toStringMethodSpecBuilder.addStatement("$S", " }");

        typeSpecBuilder.addMethod(toStringMethodSpecBuilder.build());
    }

    private static TypeSpec genPojoToFile(TypeSpec.Builder typeSpecBuilder, String outputDir, String packageName) throws IOException {

        TypeSpec pojo = typeSpecBuilder.build();

        JavaFile javaFile = JavaFile.builder(packageName, pojo).build();

        //写入物理路径
        javaFile.writeTo(new File(outputDir));

        return pojo;
    }

    private static TypeSpec genResponsePojo(BuilderMojo builderMojo, String pojoName, List<ResponseParamModel> responseParamModelList) throws IOException, ClassNotFoundException {

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(pojoName).addModifiers(Modifier.PUBLIC);

        //生成字段，get,set方法
        for(ResponseParamModel responseParamModel : responseParamModelList) {

            String key = responseParamModel.getKey();
            String responseParamTypeString = responseParamModel.getType();

            //检查是否系统自有类型
            if(isJavaSystemType(builderMojo, responseParamTypeString)) {

                //根据java系统自有的类型生成字段，get, set方法
                genFieldAndGetSetMethodFromJavaSystemType(typeSpecBuilder, responseParamModel);

            } else if(isList(responseParamTypeString)) {//检查是否List类型

                String packageName;
                String className;

                if(responseParamModel.getSubResponseParamModel().size() > 0) {//参数具有子参数列表，判断是需要递归生成对象的
                    //获得泛型的对象名
                    String subPojoName = makePojoNameByType(responseParamTypeString);
                    //递归调用，生成子对象
                    TypeSpec pojo = genResponsePojo(builderMojo, subPojoName, responseParamModel.getSubResponseParamModel());

                    packageName = responsePackageName;
                    className = pojo.name;
                } else {
                    packageName = findGenericsPackageName(responseParamTypeString);
                    className = findGenericsClassName(responseParamTypeString);
                }

                ClassName list = ClassName.get("java.util", "List");
                ClassName generics = ClassName.get(packageName, className);
                TypeName listType = ParameterizedTypeName.get(list, generics);

                //生成list字段，get, set方法
                genFieldAndGetSetMethod(typeSpecBuilder, key, listType, responseParamModel);

            } else {//生成自定义pojo
                String subPojoName = makePojoNameByType(responseParamModel.getType());
                TypeSpec pojo = genResponsePojo(builderMojo, subPojoName, responseParamModel.getSubResponseParamModel());

                //生成自定义pojo字段，get, set方法
                ClassName type = ClassName.get(responsePackageName, pojo.name);
                genFieldAndGetSetMethod(typeSpecBuilder, key, type, responseParamModel);
            }
        }

        //生成toString()
        genToString(pojoName, responseParamModelList, typeSpecBuilder);

        //生成pojo
        return genPojoToFile(typeSpecBuilder, builderMojo.getOutPath() + "android/", responsePackageName);
    }

    private static String findGenericsClassName(String type) {
        Pattern pattern = Pattern.compile("<(.+)(\\..+?)>");
        Matcher matcher = pattern.matcher(type);
        if(matcher.find()) {
            return matcher.group(2).substring(1);
        } else {
            return "";
        }
    }

    private static String findGenericsPackageName(String type) {
        Pattern pattern = Pattern.compile("<(.+)(\\..+?)>");
        Matcher matcher = pattern.matcher(type);
        if(matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    private static boolean isList(String responseParamTypeString) {
        return responseParamTypeString.startsWith("java.util.List");
    }

    private static boolean isJavaSystemType(BuilderMojo builderMojo, String responseParamTypeString) {
        try {
            Class clazz = builderMojo.getClassLoaderInterface().loadClass(responseParamTypeString);
            if(clazz.equals(String.class) ||
                    clazz.equals(Boolean.class) ||
                    clazz.equals(Short.class) ||
                    clazz.equals(Integer.class) ||
                    clazz.equals(Long.class) ||
                    clazz.equals(Float.class) ||
                    clazz.equals(Double.class)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private static String makeClassName(String classStr) {
        return classStr.substring(classStr.lastIndexOf(".") + 1);
    }

    private static String makePackageName(String classStr) {
        return classStr.substring(0, classStr.lastIndexOf("."));
    }

    private static String makePojoNameByType(String type) {
        if(type.indexOf("<") > 0) {
            type = type.substring(type.indexOf("<"), type.length() - 1);
        }
        type = type.substring(type.lastIndexOf(".") + 1);
        type = type.substring(0, 1).toUpperCase() + type.substring(1);
        return type;
    }
}
