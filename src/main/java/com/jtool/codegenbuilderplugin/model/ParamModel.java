package com.jtool.codegenbuilderplugin.model;


import java.lang.annotation.Annotation;
import java.util.List;

public abstract class ParamModel {

    protected String key;
    protected boolean required;
    protected List<Annotation> constraint;
    protected String comment;
    protected String type;

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

    public List<Annotation> getConstraint() {
        return constraint;
    }

    public void setConstraint(List<Annotation> constraint) {
        this.constraint = constraint;
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
}
