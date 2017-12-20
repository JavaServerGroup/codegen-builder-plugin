package com.jtool.codegenbuilderplugin.test.api.request;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class CheckSdkPluginsRequest extends BaseCheckSdkPluginsRequest {

    @CodeGenField(value = "唯一id")
    private String id;

    @CodeGenField(value = "渠道", groups = Add.class)
    @NotNull
    private String channel;

    @CodeGenField(value = "md5值", groups = {Add.class, Check.class})
    @NotNull
    private String md5;

    @CodeGenField(value = "插件信息集合", groups = Add.class)
    @NotNull
    private List<CheckSdkPluginsItem> pluginsInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public List<CheckSdkPluginsItem> getPluginsInfo() {
        return pluginsInfo;
    }

    public void setPluginsInfo(List<CheckSdkPluginsItem> pluginsInfo) {
        this.pluginsInfo = pluginsInfo;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
