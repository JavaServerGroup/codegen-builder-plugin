package com.jtool.codegenbuilderplugin.test.api.response;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class CommonResponse {

    @NotNull
    @CodeGenField("状态码, 0：完成")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
