package com.jtool.codegenbuilderplugin.generator;

import com.jtool.codegenbuilderplugin.BuilderMojo;
import com.jtool.codegenbuilderplugin.model.CodeGenModel;
import com.jtool.codegenbuilderplugin.model.RequestParamModel;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ApiGenerator {

    private static String apiPackageName = "com.jtool.codegen.api";

    public static void genApi(BuilderMojo builderMojo, List<CodeGenModel> apiModelList) throws IOException {

        for (CodeGenModel codeGenModel : apiModelList) {

            //判断是否需要生成SD，默认是true
            if(codeGenModel.isGenSDK()) {

                //请求的pojo名
                String requestPojoName = codeGenModel.getRequestPojoName();
                //api的类名，规则是约定的请求pojo名去掉最后的Request
                String apiName = requestPojoName.substring(0, requestPojoName.lastIndexOf("Request"));

                ClassName stringRequestClass = ClassName.get("com.android.volley.toolbox", "StringRequest");
                ClassName authFailureErrorClass = ClassName.get("com.android.volley", "AuthFailureError");
                ClassName listenerClass = ClassName.get("com.android.volley.Response", "Listener");
                ClassName errorListenerClass = ClassName.get("com.android.volley.Response", "ErrorListener");

                ClassName pojoClass = ClassName.get(apiPackageName + ".request", requestPojoName);

                ClassName stringClass = ClassName.get(String.class);

                //api类
                TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(apiName).addModifiers(Modifier.PUBLIC);

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

                    methodSpecBuilder.addStatement("if(pojo.get$N() != null) params.put(\"$N\", pojo.get$N().toString())", firstUpperCaseKey, key, firstUpperCaseKey);
                }

                methodSpecBuilder.addStatement("return params");

                //匿名类StringRequest
                TypeSpec stringRequestAnonymousClass = TypeSpec.anonymousClassBuilder("$N, $N, $N, $N", "com.android.volley.Request.Method." + codeGenModel.getHttpMethod(), "url", "onResponseListener", "onErrorListener")
                        .addSuperinterface(stringRequestClass)
                        .addMethod(methodSpecBuilder.build())
                        .build();

                //api的发送请求方法
                MethodSpec sentRequest = MethodSpec.methodBuilder("sentRequest")
                        .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                        .returns(stringRequestClass)
                        .addParameter(stringClass, "url", Modifier.FINAL)
                        .addParameter(pojoClass, "pojo", Modifier.FINAL)
                        .addParameter(ParameterizedTypeName.get(listenerClass, stringClass), "onResponseListener", Modifier.FINAL)
                        .addParameter(errorListenerClass, "onErrorListener", Modifier.FINAL)
                        .addStatement("return $L", stringRequestAnonymousClass)
                        .build();

                TypeSpec api = typeSpecBuilder.addMethod(sentRequest).build();

                JavaFile javaFile = JavaFile.builder(apiPackageName, api).build();

                //写入物理路径
                javaFile.writeTo(new File(builderMojo.getOutPath() + "android/"));
            }
        }
    }
}
