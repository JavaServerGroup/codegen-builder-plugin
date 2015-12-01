package com.jtool.codegenbuilderplugin.generator;

import com.jtool.codegenbuilderplugin.BuilderMojo;
import com.jtool.codegenbuilderplugin.model.CodeGenModel;
import com.jtool.codegenbuilderplugin.model.RequestParamModel;
import com.squareup.javapoet.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.FileUtils;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ApiGenerator {

    private static String apiPackageName = "com.jtool.codegen.api";

    public static void genApi(BuilderMojo builderMojo, List<CodeGenModel> apiModelList) throws IOException {

        try {
            genApiUtil(builderMojo);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        for (CodeGenModel codeGenModel : apiModelList) {

            //判断是否需要生成SD，默认是true
            if(codeGenModel.isGenSDK()) {

                //请求的pojo名
                String requestPojoName = codeGenModel.getRequestPojoName();
                //返回的pojo名
                String responsePojoName = codeGenModel.getResponsePojoName();
                //api的类名，就是controller的方法名加Api
                String apiName = codeGenModel.getApiMethodName() + "Api";

                //api类Builder
                TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(apiName).addModifiers(Modifier.PUBLIC);

                //api类
                TypeSpec apiClassTypeSpec;

                ClassName listenerClass = ClassName.get("com.android.volley.Response", "Listener");
                ClassName errorListenerClass = ClassName.get("com.android.volley.Response", "ErrorListener");
                ClassName requestPojoClass = ClassName.get(apiPackageName + ".request", requestPojoName);
                ClassName responsePojoClass = ClassName.get(apiPackageName + ".response", responsePojoName);
                ClassName stringClass = ClassName.get(String.class);

                //判断是否发送附件的请求
                if(isPostFileRequest(codeGenModel)) {

                    ClassName multipartRequestClass = ClassName.get("com.jtool.codegen.api.util", "MultipartRequest");

                    //api的发送请求方法
                    MethodSpec.Builder sentRequestBuilder = MethodSpec.methodBuilder("sentRequest")
                            .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                            .returns(ParameterizedTypeName.get(multipartRequestClass, responsePojoClass))
                            .addParameter(stringClass, "url", Modifier.FINAL)
                            .addParameter(requestPojoClass, "bean", Modifier.FINAL)
                            .addParameter(ParameterizedTypeName.get(listenerClass, responsePojoClass), "onResponseListener", Modifier.FINAL)
                            .addParameter(errorListenerClass, "onErrorListener", Modifier.FINAL)
                            .addStatement("$T params = new $T()", ParameterizedTypeName.get(Map.class, String.class, Object.class), ParameterizedTypeName.get(HashMap.class, String.class, Object.class));

                    //遍历生成请求参数字段
                    for(RequestParamModel requestParamModel : codeGenModel.getRequestParamModelList()){
                        String key = requestParamModel.getKey();
                        String firstUpperCaseKey = Character.toUpperCase(key.charAt(0)) + key.substring(1);

                        if("org.springframework.web.multipart.MultipartFile".equals(requestParamModel.getType())) {
                            sentRequestBuilder.addStatement("if(bean.get$N() != null) params.put(\"$N\", bean.get$N())", firstUpperCaseKey, key, firstUpperCaseKey);
                        } else {
                            sentRequestBuilder.addStatement("if(bean.get$N() != null) params.put(\"$N\", bean.get$N().toString())", firstUpperCaseKey, key, firstUpperCaseKey);
                        }
                    }

                    sentRequestBuilder.addStatement("return new $L($N, $N, $N, $N, $N)", multipartRequestClass, "url", "params", responsePojoName + ".class", "onResponseListener", "onErrorListener");

                    apiClassTypeSpec = typeSpecBuilder.addMethod(sentRequestBuilder.build()).build();

                } else {

                    ClassName gsonRequestClass = ClassName.get("com.jtool.codegen.api.util", "GsonRequest");
                    ClassName authFailureErrorClass = ClassName.get("com.android.volley", "AuthFailureError");

                    //匿名StringRequest类的重写方法
                    MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("getParams")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PROTECTED)
                            .returns(ParameterizedTypeName.get(Map.class, String.class, String.class))
                            .addException(authFailureErrorClass)
                            .addStatement("$T params = new $T()", ParameterizedTypeName.get(Map.class, String.class, String.class), ParameterizedTypeName.get(HashMap.class, String.class, String.class));

                    //遍历生成请求参数字段
                    for(RequestParamModel requestParamModel : codeGenModel.getRequestParamModelList()){
                        String key = requestParamModel.getKey();
                        String firstUpperCaseKey = Character.toUpperCase(key.charAt(0)) + key.substring(1);

                        methodSpecBuilder.addStatement("if(bean.get$N() != null) params.put(\"$N\", bean.get$N().toString())", firstUpperCaseKey, key, firstUpperCaseKey);
                    }

                    methodSpecBuilder.addStatement("return params");

                    //匿名类GsonRequest
                    TypeSpec gsonRequestAnonymousClass = TypeSpec.anonymousClassBuilder("$N, $N, $N, $N, $N", "com.android.volley.Request.Method." + codeGenModel.getHttpMethod(), "url", responsePojoName + ".class", "onResponseListener", "onErrorListener")
                            .addSuperinterface(ParameterizedTypeName.get(gsonRequestClass, responsePojoClass))
                            .addMethod(methodSpecBuilder.build())
                            .build();

                    //api的发送请求方法
                    MethodSpec sentRequest = MethodSpec.methodBuilder("sentRequest")
                            .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                            .returns(ParameterizedTypeName.get(gsonRequestClass, responsePojoClass))
                            .addParameter(stringClass, "url", Modifier.FINAL)
                            .addParameter(requestPojoClass, "bean", Modifier.FINAL)
                            .addParameter(ParameterizedTypeName.get(listenerClass, responsePojoClass), "onResponseListener", Modifier.FINAL)
                            .addParameter(errorListenerClass, "onErrorListener", Modifier.FINAL)
                            .addStatement("return $L", gsonRequestAnonymousClass)
                            .build();

                    apiClassTypeSpec = typeSpecBuilder.addMethod(sentRequest).build();
                }

                //生成api类
                JavaFile javaFile = JavaFile.builder(apiPackageName, apiClassTypeSpec).build();

                //写入物理路径
                javaFile.writeTo(new File(builderMojo.getOutPath() + "android/"));
            }
        }
    }

    private static boolean isPostFileRequest(CodeGenModel codeGenModel) {
        for(RequestParamModel requestParamModel : codeGenModel.getRequestParamModelList()) {
            if("org.springframework.web.multipart.MultipartFile".equals(requestParamModel.getType())) {
                return true;
            }
        }
        return false;
    }

    private static void genApiUtil(BuilderMojo builderMojo) throws IOException, URISyntaxException {

        new File(builderMojo.getOutPath() + "android/com/jtool/codegen/api/util/").mkdirs();

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(builderMojo.getClass(), "/template");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        try {
            Template docFile = cfg.getTemplate("GsonRequest.java");
            Writer out = new FileWriter(new File(builderMojo.getOutPath() + "android/com/jtool/codegen/api/util/GsonRequest.java"));
            docFile.process(new HashMap<>(), out);

            Template docFile2 = cfg.getTemplate("MultipartRequest.java");
            Writer out2 = new FileWriter(new File(builderMojo.getOutPath() + "android/com/jtool/codegen/api/util/MultipartRequest.java"));
            docFile2.process(new HashMap<>(), out2);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
