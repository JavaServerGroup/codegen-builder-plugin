package com.jtool.codegenbuilderplugin.test.api.request;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class BaseResponse {

    @NotNull
    @CodeGenField("状态码, 0：完成")
    protected Integer code = 0;

    public BaseResponse() {
    }

    public BaseResponse(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
