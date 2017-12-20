package com.jtool.codegenbuilderplugin.test.api.request;

public enum Type {
    PRODUCE("A"),DEMO("B"),FREE("C"),COSTOM("D");

    Type() {
    }

    Type(String value){
        this.setValue(value);
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
