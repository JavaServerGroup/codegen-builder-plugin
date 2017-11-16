package com.jtool.codegenbuilderplugin.model;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class ParamModel implements Comparable<ParamModel> {

    protected String key;
    protected boolean required;
    protected List<String> constraintStr;
    protected String comment;
    protected String type;
    protected List<ParamModel> subParamModel = new ArrayList<>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<String> getConstraintStr() {
        return constraintStr;
    }

    public void setConstraintStr(List<String> constraintStr) {
        this.constraintStr = constraintStr;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ParamModel> getSubParamModel() {
        return subParamModel;
    }

    public void setSubParamModel(List<ParamModel> subParamModel) {
        this.subParamModel = subParamModel;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public int compareTo(ParamModel o) {
        return this.getKey().compareTo(o.getKey());
    }
}
