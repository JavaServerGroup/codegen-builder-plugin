package com.jtool.codegenbuilderplugin.test.api.response;

import com.alibaba.fastjson.JSON;
import com.jtool.annotation.AvailableValues;
import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class SdkPluginsResponseItem {

    @CodeGenField("插件id")
    @NotNull
    private String id;

    @CodeGenField("插件下载url")
    @NotNull
    private String url;

    @CodeGenField("插件版本")
    @NotNull
    private Integer version;

    @CodeGenField("插件状态, 可能值有: OK, NEED_UPDATE, DELETE")
    @NotNull
    @AvailableValues(values = {"OK", "NEED_UPDATE", "DELETE"})
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
