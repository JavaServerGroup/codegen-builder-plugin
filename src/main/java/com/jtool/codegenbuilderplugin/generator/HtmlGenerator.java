package com.jtool.codegenbuilderplugin.generator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jtool.annotation.AvailableValues;
import com.jtool.codegenbuilderplugin.BuilderMojo;
import com.jtool.codegenbuilderplugin.model.CodeGenModel;
import com.jtool.codegenbuilderplugin.model.LogicInfo;
import com.jtool.codegenbuilderplugin.model.ParamModel;
import freemarker.ext.beans.StringModel;
import freemarker.template.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HtmlGenerator {

    public static void genHtmlDoc(BuilderMojo builderMojo, List<CodeGenModel> codeGenModelList, List<LogicInfo> logicInfos) {
        Map<String, List<CodeGenModel>> codeGenModelMapByForWho = groupCodeGenModelListByFowWho(codeGenModelList);
        for(Map.Entry<String, List<CodeGenModel>> entry : codeGenModelMapByForWho.entrySet()) {
            genEachHtmlDoc(builderMojo, entry.getKey(), entry.getValue(), logicInfos);
        }
    }

    private static void genEachHtmlDoc(BuilderMojo builderMojo, String forWho, List<CodeGenModel> codeGenModelList, List<LogicInfo> logicInfoList) {
        //文档最后修改时间
        String lastModifyStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String lastModifyDayStr = new SimpleDateFormat("yyyy_MM_dd").format(new Date());

        //自定义文件内容
        String infoHtmlFileContent = "未填写任何项目信息，强烈要求补全。";
        try {
            if(builderMojo.getInfoHtmlFile() != null && builderMojo.getInfoHtmlFile().exists()) {
                infoHtmlFileContent = FileUtils.readFileToString(builderMojo.getInfoHtmlFile());
            }
        } catch (FileNotFoundException e) {
            //ignore
        } catch (IOException e) {
            e.printStackTrace();
        }

        //修改日志内容
        String changeLogHtmlFileContent = null;
        try {
            if(builderMojo.getInfoHtmlFile() != null && builderMojo.getInfoHtmlFile().exists()) {
                changeLogHtmlFileContent = FileUtils.readFileToString(builderMojo.getChangeLogHtmlFile());
            }
        } catch (FileNotFoundException e) {
            //ignore
        } catch (IOException e) {
            e.printStackTrace();
        }

        //组装数据
        Map<String, Object> root = new HashMap<>();
        root.put("apiModelList", codeGenModelList);
        if(logicInfoList != null && logicInfoList.size() > 0) {
            root.put("logicInfoList", logicInfoList);
        }
        root.put("lastModifyStr", lastModifyStr);
        root.put("infoHtmlFileContent", infoHtmlFileContent);
        if(changeLogHtmlFileContent != null && changeLogHtmlFileContent.length() > 0) {
            root.put("changeLogHtmlFileContent", changeLogHtmlFileContent);
        }
        root.put("projectName", builderMojo.getProjectName());
        root.put("hasRequestParams", true);//是否有请求参数

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(builderMojo.getClass(), "/template");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setSharedVariable("constraint", new HtmlConstraintGenerator());
        cfg.setSharedVariable("successReturnJson", new HtmlSuccessReturnJsonGenerator());
        cfg.setSharedVariable("successReturnParam", new HtmlSuccessReturnGenerator());
        cfg.setSharedVariable("isMaxDouble", new IsMaxDouble());

        try {
            Template docFile = cfg.getTemplate("doc.template");
            String fileName;
            if("default".equals(forWho)) {
                fileName = builderMojo.getOutPath() + "/" + builderMojo.getProjectName() + "_doc_" + lastModifyDayStr + ".html";
            } else {
                fileName = builderMojo.getOutPath() + "/" + builderMojo.getProjectName() + "_" + forWho + "_doc_" + lastModifyDayStr + ".html";
            }
            Writer out = new FileWriter(new File(fileName));
            docFile.process(root, out);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }

    //根据forWho属性,将list分组成一个以forWho为key,以相应相同forWho组成的list为值的map
    private static Map<String, List<CodeGenModel>> groupCodeGenModelListByFowWho(List<CodeGenModel> codeGenModelList) {
        Map<String, List<CodeGenModel>> result = new HashMap<>();
        for(CodeGenModel codeGenModel : codeGenModelList) {
            List<CodeGenModel> codeGenModelFromMap = result.get(codeGenModel.getForWho());
            if(codeGenModelFromMap == null) {//如果指定forWho没找到list,就要新建一个并把codeGenModel加到这个list,最后放到map里面
                codeGenModelFromMap = new ArrayList<>();
                codeGenModelFromMap.add(codeGenModel);
                result.put(codeGenModel.getForWho(), codeGenModelFromMap);
            } else {//能找到就直接添加codeGenModel到list
                codeGenModelFromMap.add(codeGenModel);
                result.put(codeGenModel.getForWho(), codeGenModelFromMap);
            }
        }
        return result;
    }
}

class HtmlConstraintGenerator implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {

        if (list != null && list.size() == 1) {

            if (list.get(0) instanceof DefaultListAdapter) {

                String result = "";

                DefaultListAdapter defaultListAdapter = (DefaultListAdapter) list.get(0);

                for (int i = 0; i < defaultListAdapter.size(); i++) {
                    Object obj = ((StringModel) defaultListAdapter.get(i)).getWrappedObject();

                    result += "<div>" + obj.toString() + "</div>";
                }

                if ("".equals(result)) {
                    return " --- ";
                } else {
                    return result;
                }

            } else {
                return " --- ";
            }

        } else {
            return " --- ";
        }

    }
}

class HtmlSuccessReturnJsonGenerator implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {

        try {
            if (list != null && list.size() == 1) {
                return list.get(0).toString();
            } else {
                return " --- ";
            }
        } catch (NullPointerException e) {
            return " --- ";
        }

    }
}

class HtmlSuccessReturnGenerator implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        ParamModel responseParamModel = (ParamModel)(((StringModel)list.get(0)).getWrappedObject());
        return generator(responseParamModel, 0);
    }

    private String generator(ParamModel responseParamModel, int level) {
        String result = "<tr>";

        result += "<td>";
        for(int i = 0; i < level; i++) {
            result += "&nbsp;&nbsp;&nbsp;&nbsp;";
        }
        result += responseParamModel.getKey();
        result += "</td>";

        result += "<td>";
        result += responseParamModel.isRequired() ? "必须" : "可选";
        result += "</td>";

        result += "<td>";
        result += responseParamModel.getComment();
        result += "</td>";

        result += "</tr>";

        if(responseParamModel.getSubParamModel() != null && responseParamModel.getSubParamModel().size() > 0) {
            level++;
            for(ParamModel subResponseParamModel : responseParamModel.getSubParamModel()) {
                result += generator(subResponseParamModel, level);
            }
        }

        return result;
    }

}

class IsMaxDouble implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        Double d = (Double)(((SimpleNumber)list.get(0)).getAsNumber());
        return d == Double.MAX_VALUE;
    }
}