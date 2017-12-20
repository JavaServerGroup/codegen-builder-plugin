package com.jtool.codegenbuilderplugin.test.api.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class EnumTypeRequest {

    @NotNull
    @CodeGenField("类型枚举")
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
