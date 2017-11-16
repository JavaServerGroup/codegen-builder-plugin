package com.jtool.codegenbuilderplugin.test.api.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CheckSdkPluginsResponse extends BaseResponse {

    @NotNull
    @CodeGenField("下次来更新的时间间隔，单位分钟")
    private Integer interval = 10;

    @CodeGenField("插件检查结果集合")
    @NotNull
    private List<SdkPluginsResponseItem> pluginsInfo;

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public List<SdkPluginsResponseItem> getPluginsInfo() {
        return pluginsInfo;
    }

    public void setPluginsInfo(List<SdkPluginsResponseItem> pluginsInfo) {
        this.pluginsInfo = pluginsInfo;
    }
}
