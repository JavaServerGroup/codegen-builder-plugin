package com.jtool.codegenbuilderplugin.parser;

import com.jtool.codegenannotation.*;
import com.jtool.codegenbuilderplugin.BuilderMojo;
import com.jtool.codegenbuilderplugin.model.*;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        //分析排序的序号
        codeGenModel.setDocSeq(paresDocSeq(method));

        //分析api的名字
        codeGenModel.setApiName(paresApiName(method));

        //分析主机host的值,表示api会放在那个机器的主机上
        codeGenModel.setHost(paresHost(builderMojo, method));

        //分析说明
        codeGenModel.setDescription(paresDescription(method));

        //分析http的方法:GET或者POST
        codeGenModel.setHttpMethod(paresHttpMethod(method));

        //分析备注内容
        codeGenModel.setRemark(paresRemark(method));

        //分析API的路径值
        codeGenModel.setUrl(paresUrl(method));

        //分析方法错误返回，就是分析自定义Exception
        codeGenModel.setErrorType(paresErrorType(method));

        List<RequestParamModel> requestParamModelList = new ArrayList<>();
        List<PathParamModel> pathParamModelList = new ArrayList<>();

        parseParam(method, requestParamModelList, pathParamModelList, codeGenModel.getUrl());

        //分析request的pojo的参数列表
        codeGenModel.setRequestParamModelList(requestParamModelList);

        //分析path param的参数列表
        codeGenModel.setPathParamModelList(pathParamModelList);

        //分析response的pojo的参数列表
        codeGenModel.setResponseParamModelList(parseResponseParamModelList(builderMojo, method));

        //分析是否弃用的方法
        codeGenModel.setIsDeprecated(parseIsDeprecated(method));

        //分析接口是给谁用的
        codeGenModel.setForWho(parseForWho(method));

        //分析成功请求返回的字符串
        codeGenModel.setSuccessReturn(parseSuccessReturnString(builderMojo, method));

        //分析获得请求pojo的名字
        codeGenModel.setRequestPojoName(parseRequestPojoName(method));

        //分析获得返回pojo的名字
        codeGenModel.setResponsePojoName(parseResponsePojoName(method));

        //分析是否生成SDK
        codeGenModel.setIsGenSDK(parseIsGenSDK(method));

        //分析这个方法的名称
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

        if(httpMethod != null && httpMethod.contains("GET")) {

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

            result += "\"<br/>";
        }

        if(httpMethod != null && httpMethod.contains("POST")) {

            if(isPostFile(requestParamModelList)) {
                String queryString = "";

                for(RequestParamModel requestParamModel : requestParamModelList) {
                    queryString += " -F \"" + requestParamModel.getKey() + "=" +  "\" ";
                }

                result += "curl -X POST " + queryString + " \"http://host:port" + uri + "\"";
            } else {
                String queryString = "";

                for(RequestParamModel requestParamModel : requestParamModelList) {
                    queryString += " --data-urlencode \"" + requestParamModel.getKey() + "=" +  "\" ";
                }

                result += "curl -X POST " + queryString + " \"http://host:port" + uri + "\"";
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

    private static Object parseSuccessReturnString(BuilderMojo builderMojo, Method method) {

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
            } else if(field.getType().equals(Short.class)) {
                field.set(obj, 0);
            } else if(field.getType().equals(Integer.class)) {
                field.set(obj, 0);
            } else if(field.getType().equals(Long.class)) {
                field.set(obj, 0L);
            } else if(field.getType().equals(Float.class)){
                field.set(obj, 0.0f);
            }  else if (field.getType().equals(Double.class)){
                field.set(obj, 0.0d);
            } else if(field.getType().equals(Date.class)) {
                field.set(obj, new Date());
            } else if (field.getType().equals(List.class) || field.getType().equals(Set.class)) {

                //生成一个list
                Collection<Object> collection;

                if(field.getType().equals(List.class)) {
                    collection = new ArrayList<>();
                } else {
                    collection = new HashSet<>();
                }

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
                            collection.add("string");
                            break;
                        case "java.lang.Boolean" :
                            collection.add(false);
                            break;
                        case "java.lang.Short" :
                        case "java.lang.Integer" :
                        case "java.lang.Long" :
                            collection.add(0);
                            break;
                        case "java.lang.Float" :
                        case "java.lang.Double" :
                            collection.add(0);
                            break;
                        default:
                            collection.add(genParamJsonObj(builderMojo, (builderMojo.getClassLoaderInterface().loadClass(className))));
                    }
                }

                //设置值到list
                field.set(obj, collection);

            } else if(!field.getType().isPrimitive()) {
                field.set(obj, genParamJsonObj(builderMojo, field.getType()));
            }
        }

        return obj;
    }


    private static void parseParam(Method method, List<RequestParamModel> requestParamModelList, List<PathParamModel> pathParamModelList, String url) {

        CodeGenRequest codeGenRequest = method.getAnnotation(CodeGenRequest.class);
        if (codeGenRequest != null) {

            List<String> pathParamList =  new ArrayList<>();
            Pattern pattern = Pattern.compile("\\{(.+?)\\}");
            Matcher matcher = pattern.matcher(url);

            while (matcher.find()){
                pathParamList.add(matcher.group(1));
            }

            for(Field field : codeGenRequest.value().getDeclaredFields()) {

                //遍历请求pojo的带有@CodeGenField注解的变量
                CodeGenField codeGenField = field.getAnnotation(CodeGenField.class);

                if(codeGenField != null) {
                    if(pathParamList.contains(field.getName())) {
                        PathParamModel pathParamModel = new PathParamModel();
                        pathParamModel.setKey(field.getName());
                        pathParamModel.setRequired(paresFieldIsRequired(field));
                        pathParamModel.setComment(codeGenField.value());
                        //分析请求的约束
                        pathParamModel.setConstraint(Arrays.asList(field.getAnnotations()));
                        pathParamModel.setType(field.getType().getName());

                        pathParamModelList.add(pathParamModel);
                    } else {
                        RequestParamModel requestParamModel = new RequestParamModel();
                        requestParamModel.setKey(field.getName());
                        requestParamModel.setRequired(paresFieldIsRequired(field));
                        requestParamModel.setComment(codeGenField.value());
                        //分析请求的约束
                        requestParamModel.setConstraint(Arrays.asList(field.getAnnotations()));
                        requestParamModel.setType(field.getType().getName());

                        requestParamModelList.add(requestParamModel);
                    }
                }
            }
        }

    }

    private static List<RequestParamModel> paresRequestParamModelList(Method method, List<String> pathParamList) {

        List<RequestParamModel> result = new ArrayList<>();

        CodeGenRequest codeGenRequest = method.getAnnotation(CodeGenRequest.class);
        if (codeGenRequest != null) {

            for(Field field : codeGenRequest.value().getDeclaredFields()) {

                //遍历请求pojo的带有@CodeGenField注解的变量
                CodeGenField codeGenField = field.getAnnotation(CodeGenField.class);

                if(codeGenField != null && !pathParamList.contains(field.getName())) {
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
                    if (field.getType().equals(List.class)) {//如果是List集合,就得分析List泛型的类型

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
                    } else if(field.getType().equals(Map.class)) {
                        throw new RuntimeException("不应该使用Map, 请使用pojo");
                    } else {
                        // 如果是普通的类
                        List<ResponseParamModel> responseParamModelList = parseResponseParamModel(builderMojo, field.getType());
                        Collections.sort(responseParamModelList);
                        responseParamModel.setSubResponseParamModel(responseParamModelList);
                    }
                } else {
                    throw new RuntimeException("不应该使用简单类型");
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
                result += ", " + url;
            }
        }
        return result;
    }

    private static List<ErrorInfo> paresErrorType(Method method) {

        List<ErrorInfo> resultException = new ArrayList<>();

        Class<?>[] exceptionTypes = method.getExceptionTypes();

        //扫描方法抛出的异常，其中包含@CodeGenExceptionDefine注解的
        for(Class exceptionType : exceptionTypes) {
            CodeGenExceptionDefine codeGenExceptionDefine = (CodeGenExceptionDefine) exceptionType.getAnnotation(CodeGenExceptionDefine.class);
            if(codeGenExceptionDefine != null){
                ErrorInfo errorInfo = new ErrorInfo();
                errorInfo.setErrorCode(codeGenExceptionDefine.code());
                errorInfo.setErrorDesc(codeGenExceptionDefine.desc());
                resultException.add(errorInfo);
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
