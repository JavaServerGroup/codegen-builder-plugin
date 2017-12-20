package com.jtool.codegenbuilderplugin.test.api.response;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class BaseSdkPluginInfo {

    @CodeGenField("插件名称")
    @NotNull
    @Size(min = 1, max = 50)
    protected String name;

    @CodeGenField("插件包的下载url")
    @NotNull
    protected String url;

    @CodeGenField("插件版本")
    @NotNull
    @Min(0)
    protected Integer version;

    @CodeGenField("插件包大小")
    @NotNull
    @Min(0)
    protected Integer size;

    @CodeGenField("插件包描述")
    @Size(min = 1)
    protected String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
