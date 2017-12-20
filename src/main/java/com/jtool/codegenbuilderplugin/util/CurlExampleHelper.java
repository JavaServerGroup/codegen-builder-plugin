package com.jtool.codegenbuilderplugin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jtool.codegenbuilderplugin.BuilderMojo;
import com.jtool.codegenbuilderplugin.model.CodeGenModel;
import com.jtool.codegenbuilderplugin.model.ParamModel;

import java.io.IOException;

import static com.jtool.codegenbuilderplugin.parser.MethodParser.genParamJsonObj;
import static com.jtool.codegenbuilderplugin.parser.MethodParser.isPostFile;

public class CurlExampleHelper {

    public static String parseCurlExample(BuilderMojo builderMojo, CodeGenModel codeGenModel) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {

        String result = "";

        if(codeGenModel.getHttpMethod() != null && codeGenModel.getHttpMethod().contains("GET")) {

            String queryString = "";

            for(int i = 0; i < codeGenModel.getRequestParamModelList().size(); i++) {
                if(i != 0) {
                    queryString += "&";
                }

                queryString += codeGenModel.getRequestParamModelList().get(i).getKey() + "=";
            }

            result = "curl -X GET \"http://host:port" + codeGenModel.getUrl();

            if(!"".equals(queryString)) {
                result += "?" + queryString;
                result += "\"";
            }
        }

        if(codeGenModel.getHttpMethod() != null && codeGenModel.getHttpMethod().contains("POST")) {

            if(!"".equals(result)) {
                result += "   \r\n";
                result += "   \r\n";
            }

            if(isPostFile(codeGenModel.getRequestParamModelList())) {
                String queryString = "";

                for(ParamModel requestParamModel : codeGenModel.getRequestParamModelList()) {
                    queryString += " -F \"" + requestParamModel.getKey() + "=" +  "\" ";
                }

                result += "curl -X POST " + queryString + " \"http://host:port" + codeGenModel.getUrl() + "\"";
            } else {
                String queryString = "";

                if(codeGenModel.isRest() && codeGenModel.getRequestClass().isPresent()) {
                    final Object obj = genParamJsonObj(builderMojo, codeGenModel.getRequestClass().get(), codeGenModel.getRequestGroups());
                    queryString += " -d '" + JSON.toJSONString(obj, SerializerFeature.PrettyFormat) + "'";

                } else {
                    for(ParamModel requestParamModel : codeGenModel.getRequestParamModelList()) {
                        queryString += " --data-urlencode \"" + requestParamModel.getKey() + "=" +  "\" ";
                    }
                }

                result += "curl -X POST " + queryString + " \"http://host:port" + codeGenModel.getUrl() + "\"";
            }
        }

        return result;
    }

}
