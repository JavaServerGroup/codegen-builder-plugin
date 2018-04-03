package com.jtool.codegenbuilderplugin.test.api.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class EnumTypeRequest {

    @NotNull
    @CodeGenField("类型枚举")
    private Type type;

    @NotEmpty
    @CodeGenField("类型枚举列表")
    private List<Type> typeList;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Type> getTypeList() {
        return typeList;
    }

    public EnumTypeRequest setTypeList(List<Type> typeList) {
        this.typeList = typeList;
        return this;
    }
}
