package com.jtool.codegenbuilderplugin.generator;

import com.jtool.codegenbuilderplugin.BuilderMojo;
import com.jtool.codegenbuilderplugin.model.CodeGenModel;
import com.jtool.codegenbuilderplugin.model.ExceptionModel;
import com.jtool.codegenbuilderplugin.model.ParamModel;
import freemarker.ext.beans.StringModel;
import freemarker.template.*;

import java.io.*;
import java.util.*;

public class DocMdFormatGenerator {

    public static void genMdDoc(BuilderMojo builderMojo, List<CodeGenModel> codeGenModelList, List<ExceptionModel> exceptionModels) {
        Map<String, List<CodeGenModel>> codeGenModelMapByForWho = groupCodeGenModelListByFowWho(codeGenModelList);
        for(Map.Entry<String, List<CodeGenModel>> entry : codeGenModelMapByForWho.entrySet()) {
            genEachMdDoc(builderMojo, entry.getKey(), entry.getValue(), exceptionModels);
        }
    }

    private static void genEachMdDoc(BuilderMojo builderMojo, String forWho, List<CodeGenModel> codeGenModelList, List<ExceptionModel> exceptionModels) {

        //组装数据
        Map<String, Object> root = new HashMap<>();
        root.put("apiModelList", codeGenModelList);
        root.put("exceptionModels", exceptionModels);
        root.put("projectName", builderMojo.getProjectName());
        root.put("hasRequestParams", true);//是否有请求参数

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(builderMojo.getClass(), "/template");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setSharedVariable("successReturnJson", new MdSuccessReturnJsonGenerator());
        cfg.setSharedVariable("returnParamDetail", new MdReturnParamDetailGenerator());
        cfg.setSharedVariable("isMaxDouble", new IsMaxDouble());

        try {
            Template docFile = cfg.getTemplate("mdTemplate.md");
            String fileName;
            if("default".equals(forWho)) {
                fileName = builderMojo.getOutPath() + "/" + builderMojo.getProjectName() + "_md_.md";
            } else {
                fileName = builderMojo.getOutPath() + "/" + builderMojo.getProjectName() + "_" + forWho + "_md_.md";
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
            }
            codeGenModelFromMap.add(codeGenModel);
            result.put(codeGenModel.getForWho(), codeGenModelFromMap);
        }
        return result;
    }
}

class MdReturnParamDetailGenerator implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        ParamModel responseParamModel = (ParamModel)(((StringModel)list.get(0)).getWrappedObject());
        return generator(responseParamModel, 0);
    }

    private String generator(ParamModel responseParamModel, int level) {
        String result = "<tr><td>";
        for(int i = 0; i < level; i++) {
            result += System.getProperties().get("line.separator") + "&nbsp;&nbsp;&nbsp;&nbsp;";
        }
        result += responseParamModel.getKey();
        result += "</td><td>";

        result += responseParamModel.isRequired() ? "必须" : "可选";
        result += "</td><td>";

        if(responseParamModel.getConstraintStr() == null || responseParamModel.getConstraintStr().isEmpty()) {
            result += " --- ";
        } else {
            for (String constraintStr : responseParamModel.getConstraintStr()) {
                result += constraintStr + "<br/>";
            }
        }
        result += "</td><td>";

        result += responseParamModel.getComment();
        result += "</td>";

        if(responseParamModel.getSubParamModel() != null && responseParamModel.getSubParamModel().size() > 0) {
            level++;
            for(ParamModel subResponseParamModel : responseParamModel.getSubParamModel()) {
                result += generator(subResponseParamModel, level);
            }
        }

        return result + "</tr>";
    }

}

class MdSuccessReturnJsonGenerator implements TemplateMethodModelEx {

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

class IsMaxDouble implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        Double d = (Double)(((SimpleNumber)list.get(0)).getAsNumber());
        return d == Double.MAX_VALUE;
    }
}
