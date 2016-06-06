package com.jtool.codegenbuilderplugin.parser;

import com.jtool.codegenannotation.*;
import com.jtool.codegenbuilderplugin.BuilderMojo;
import com.jtool.codegenbuilderplugin.model.CodeGenModel;
import com.jtool.codegenbuilderplugin.model.RequestParamModel;
import com.jtool.codegenbuilderplugin.model.ResponseParamModel;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class MethodParser {

    public static List<CodeGenModel> parseMethodToCodeGenModel(BuilderMojo builderMojo, List<Method> methodLists) {

        List<CodeGenModel> result = new ArrayList<>();

        for (Method method : methodLists) {
            if (method.getAnnotation(CodeGenApi.class) != null) {
                CodeGenModel codeGenModel = MethodParser.parse(builderMojo, method);
                result.add(codeGenModel);
            } else {
                throw new RuntimeException("本方法应该有@CodeGenApi注解才对");
            }
        }

        Collections.sort(result);

        return result;
    }


    private static CodeGenModel parse(BuilderMojo builderMojo, Method method) {
        CodeGenModel codeGenModel = new CodeGenModel();
        codeGenModel.setDocSeq(paresDocSeq(method));
        codeGenModel.setApiName(paresApiName(method));
        codeGenModel.setHost(paresHost(builderMojo, method));
        codeGenModel.setDescription(paresDescription(method));
        codeGenModel.setHttpMethod(paresHttpMethod(method));
        codeGenModel.setRemark(paresRemark(method));
        codeGenModel.setUrl(paresUrl(method));
        //分析方法错误返回，就是分析自定义Exception
        codeGenModel.setErrorType(paresErrorType(method));
        //分析request的pojo的参数列表
        codeGenModel.setRequestParamModelList(paresRequestParamModelList(method));
        //分析response的pojo的参数列表
        codeGenModel.setResponseParamModelList(parseResponseParamModelList(builderMojo, method));
        //分析是否弃用的方法
        codeGenModel.setIsDeprecated(parseIsDeprecated(method));
        codeGenModel.setForWho(parseForWho(method));
        //分析成功请求返回
        codeGenModel.setSuccessReturn(parseSuccessReturn(builderMojo, method));
        //分析获得请求pojo的名字
        codeGenModel.setRequestPojoName(parseRequestPojoName(method));
        //分析获得返回pojo的名字
        codeGenModel.setResponsePojoName(parseResponsePojoName(method));
        codeGenModel.setIsGenSDK(parseIsGenSDK(method));
        codeGenModel.setApiMethodName(parseApiMethodName(method));
        //分析获得curlExample
        codeGenModel.setCurlExample(parseCurlExample(codeGenModel));

        return codeGenModel;
    }

    private static String parseCurlExample(CodeGenModel codeGenModel) {

        String result = "";

        String httpMethod = codeGenModel.getHttpMethod();
        String uri = codeGenModel.getUrl();

        List<RequestParamModel> requestParamModelList = codeGenModel.getRequestParamModelList();

        if("GET".equals(httpMethod)) {

            String queryString = "";

            for(int i = 0; i < requestParamModelList.size(); i++) {
                if(i != 0) {
                    queryString += "&";
                }

                queryString += requestParamModelList.get(i).getKey() + "=";
            }

            result = "curl -X GET \"http://host:port" + uri;

            if(!"".equals(queryString)) {
                result += "?" + queryString;
            }

            result += "\"";

        } else if("POST".equals(httpMethod)) {

            if(isPostFile(requestParamModelList)) {
                String queryString = "";

                for(RequestParamModel requestParamModel : requestParamModelList) {
                    queryString += " -F \"" + requestParamModel.getKey() + "=" +  "\" ";
                }

                result = "curl -X POST " + queryString + " \"http://host:port" + uri + "\"";
            } else {
                String queryString = "";

                for(RequestParamModel requestParamModel : requestParamModelList) {
                    queryString += " --data-urlencode \"" + requestParamModel.getKey() + "=" +  "\" ";
                }

                result = "curl -X POST " + queryString + " \"http://host:port" + uri + "\"";
            }
        }

        return result;
    }

    private static boolean isPostFile(List<RequestParamModel> requestParamModelList) {
        for(RequestParamModel requestParamModel : requestParamModelList) {
            if("org.springframework.web.multipart.MultipartFile".equals(requestParamModel.getType())) {
                return true;
            }
        }

        return false;
    }

    private static String parseApiMethodName(Method method) {
        return method.getName();
    }

    private static Object parseSuccessReturn(BuilderMojo builderMojo, Method method) {

        CodeGenResponse codeGenResponse = method.getAnnotation(CodeGenResponse.class);
        if(codeGenResponse == null) {
            return null;
        }

        try {
            Class clazz = method.getAnnotation(CodeGenResponse.class).value();
            return genParamJsonObj(builderMojo, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Object();
    }

    //生成请求成功返回的json表示
    private static Object genParamJsonObj(BuilderMojo builderMojo, Class clazz) throws ClassNotFoundException, IllegalArgumentException,
            IllegalAccessException, InstantiationException, IOException {

        Object obj = clazz.newInstance();

        for (Field field : clazz.getDeclaredFields()) {

            field.setAccessible(true);

            if (field.getType().equals(String.class)) {
                field.set(obj, field.getName());
            } else if(field.getType().equals(Boolean.class)){
                field.set(obj, false);
            } else if(field.getType().equals(Short.class) || field.getType().equals(Integer.class) || field.getType().equals(Long.class)){
                field.set(obj, 0);
            } else if(field.getType().equals(Float.class) || field.getType().equals(Double.class)){
                field.set(obj, 0.0);
            } else if (field.getType().equals(List.class)) {

                //生成一个list
                List<Object> list = new ArrayList<>();

                Type fc = field.getGenericType();

                if (fc == null) {
                    continue;
                }

                if (fc instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) fc;
                    String className = pt.getActualTypeArguments()[0].getTypeName();

                    //List里面的泛型参数
                    switch (className) {
                        case "java.lang.String" :
                            list.add("string");
                            break;
                        case "java.lang.Boolean" :
                            list.add(false);
                            break;
                        case "java.lang.Short" :
                        case "java.lang.Integer" :
                        case "java.lang.Long" :
                            list.add(0);
                            break;
                        case "java.lang.Float" :
                        case "java.lang.Double" :
                            list.add(0);
                            break;
                        default:
                            list.add(genParamJsonObj(builderMojo, (builderMojo.getClassLoaderInterface().loadClass(className))));
                    }
                }

                //设置值到list
                field.set(obj, list);

            } else if (field.getType().equals(Set.class)) {

                //生成一个set
                Set<Object> set = new HashSet<>();

                Type fc = field.getGenericType();

                if (fc == null) {
                    continue;
                }

                if (fc instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) fc;
                    String className = pt.getActualTypeArguments()[0].getTypeName();

                    //List里面的泛型参数
                    switch (className) {
                        case "java.lang.String" :
                            set.add("string");
                            break;
                        case "java.lang.Boolean" :
                            set.add(false);
                            break;
                        case "java.lang.Short" :
                        case "java.lang.Integer" :
                        case "java.lang.Long" :
                            set.add(0);
                            break;
                        case "java.lang.Float" :
                        case "java.lang.Double" :
                            set.add(0);
                            break;
                        default:
                            set.add(genParamJsonObj(builderMojo, (builderMojo.getClassLoaderInterface().loadClass(className))));
                    }
                }

                //设置值到list
                field.set(obj, set);

            } else if(!field.getType().isPrimitive()) {
                field.set(obj, genParamJsonObj(builderMojo, field.getType()));
            }
        }

        return obj;
    }

    private static List<RequestParamModel> paresRequestParamModelList(Method method) {

        List<RequestParamModel> result = new ArrayList<>();

        CodeGenRequest codeGenRequest = method.getAnnotation(CodeGenRequest.class);
        if (codeGenRequest != null) {

            for(Field field : codeGenRequest.value().getDeclaredFields()) {

                //遍历请求pojo的带有@CodeGenField注解的变量
                CodeGenField codeGenField = field.getAnnotation(CodeGenField.class);

                if(codeGenField != null) {
                    RequestParamModel requestParamModel = new RequestParamModel();
                    requestParamModel.setKey(field.getName());
                    requestParamModel.setRequired(paresFieldIsRequired(field));
                    requestParamModel.setComment(codeGenField.value());
                    //分析请求的约束
                    requestParamModel.setConstraint(Arrays.asList(field.getAnnotations()));
                    requestParamModel.setType(field.getType().getName());

                    result.add(requestParamModel);
                }
            }
        }

        return result;
    }

    private static List<ResponseParamModel> parseResponseParamModelList(BuilderMojo builderMojo, Method method) {

        List<ResponseParamModel> responseParamModelList = new ArrayList<>();

        CodeGenResponse codeGenResponse = method.getAnnotation(CodeGenResponse.class);
        if (codeGenResponse != null) {
            responseParamModelList.addAll(parseResponseParamModel(builderMojo, codeGenResponse.value()));
        }

        Collections.sort(responseParamModelList);
        return responseParamModelList;
    }

    private static List<ResponseParamModel> parseResponseParamModel(BuilderMojo builderMojo, Class<?> clazz) {

        List<ResponseParamModel> result = new ArrayList<>();

        for(Field field : clazz.getDeclaredFields()) {

            //遍历返回pojo的带有@CodeGenField注解的变量
            CodeGenField codeGenField = field.getAnnotation(CodeGenField.class);

            if(codeGenField != null) {
                ResponseParamModel responseParamModel = new ResponseParamModel();
                responseParamModel.setKey(field.getName());
                responseParamModel.setRequired(paresFieldIsRequired(field));
                responseParamModel.setComment(codeGenField.value());
                responseParamModel.setType(field.getGenericType().getTypeName());

                result.add(responseParamModel);

                if(!field.getType().isPrimitive()) {
                    if (field.getType().equals(List.class)) {
                        // 如果是List集合
                        Type type = field.getGenericType();

                        if (type == null) {
                            continue;
                        }

                        if (type instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) type;

                            // List集合中的参数类型的字节码
                            try {
                                String genericsTypeStr = pt.getActualTypeArguments()[0].getTypeName();
                                List<ResponseParamModel> responseParamModelList = parseResponseParamModel(builderMojo, builderMojo.getClassLoaderInterface().loadClass(genericsTypeStr));
                                Collections.sort(responseParamModelList);
                                responseParamModel.setSubResponseParamModel(responseParamModelList);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException("找不到类：" + pt.getActualTypeArguments()[0].getTypeName());
                            }
                        }
                    } else {
                        // 如果是普通的类
                        List<ResponseParamModel> responseParamModelList = parseResponseParamModel(builderMojo, field.getType());
                        Collections.sort(responseParamModelList);
                        responseParamModel.setSubResponseParamModel(responseParamModelList);
                    }
                }
            }
        }

        return result;
    }

    private static String parseForWho(Method method) {
        return method.getAnnotation(CodeGenApi.class).forWho();
    }

    private static boolean parseIsGenSDK(Method method) {
        return method.getAnnotation(CodeGenApi.class).genSDK();
    }

    private static String parseResponsePojoName(Method method) {
        CodeGenResponse codeGenResponse = method.getAnnotation(CodeGenResponse.class);
        if(codeGenResponse == null) {
            return null;
        }
        Class clazz = method.getAnnotation(CodeGenResponse.class).value();
        String typeName = clazz.getTypeName();
        typeName = typeName.substring(typeName.lastIndexOf(".") + 1);
        return typeName;
    }

    private static String parseRequestPojoName(Method method) {
        CodeGenRequest codeGenRequest = method.getAnnotation(CodeGenRequest.class);
        if(codeGenRequest == null) {
            return null;
        }
        Class clazz = codeGenRequest.value();
        String typeName = clazz.getTypeName();
        typeName = typeName.substring(typeName.lastIndexOf(".") + 1);
        return typeName;
    }

    private static String paresApiName(Method method) {
        return method.getAnnotation(CodeGenApi.class).name();
    }

    private static String paresHost(BuilderMojo builderMojo, Method method) {
        String host = method.getAnnotation(CodeGenApi.class).host();

        if(!"host(get from initialization)".equals(host)) {//host不等于默认值,就是用户指定了,返回指定值
            return host;
        } else {//如果等于默认,就检查配置有没有值,有就返回配置值
            Map<String, String> hostsMap = builderMojo.getHosts();

            if(hostsMap != null && hostsMap.size() > 0) {
                String result = "";
                for(Map.Entry<String, String> entry : hostsMap.entrySet()) {
                    result += entry.getKey() + ":&nbsp;&nbsp;" + entry.getValue() + "<br/>";
                }
                return result;
            }
        }

        return host;
    }

    private static String paresDescription(Method method) {
        return method.getAnnotation(CodeGenApi.class).description();
    }

    private static String paresHttpMethod(Method method) {
        String result = "";
        for (RequestMethod requestMethod : method.getAnnotation(RequestMapping.class).method()) {
            if (result.equals("")) {
                result += requestMethod.name();
            } else {
                result += "," + requestMethod.name();
            }
        }
        return result;
    }

    private static String paresRemark(Method method) {
        return method.getAnnotation(CodeGenApi.class).remark();
    }

    private static String paresUrl(Method method) {
        String result = "";
        for (String url : method.getAnnotation(RequestMapping.class).value()) {
            if (result.equals("")) {
                result += url;
            } else {
                result += "," + url;
            }
        }
        return result;
    }

    private static List<Exception> paresErrorType(Method method) {

        List<Exception> resultException = new ArrayList<>();

        Class<?>[] exceptionTypes = method.getExceptionTypes();

        //扫描方法抛出的异常，其中包含@CodeGenExceptionDefine注解的
        for(Class exceptionType : exceptionTypes) {
            if(exceptionType.getAnnotation(CodeGenExceptionDefine.class) != null){
                try {
                    resultException.add((Exception)exceptionType.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    throw new RuntimeException("实例化对象失败,请添加不带参数的构造函数以便生成成功.");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        //扫描方法@CodeGenException注解定义的异常
        CodeGenException docException = method.getAnnotation(CodeGenException.class);
        if (docException != null) {
            // 得到该方法所有异常的Class字节码
            for (Class clazz : docException.value()) {
                try {
                    resultException.add((Exception)clazz.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    throw new RuntimeException("实例化对象失败,请添加不带参数的构造函数以便生成成功.");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultException;
    }

    private static boolean parseIsDeprecated(Method method) {
        Deprecated deprecated = method.getAnnotation(Deprecated.class);
        return deprecated != null;
    }

    private static Double paresDocSeq(Method method) {
        return method.getAnnotation(CodeGenApi.class).docSeq();
    }

    private static boolean paresFieldIsRequired(Field field) {
        return field.getAnnotation(NotNull.class) != null;
    }

}
