package com.jtool.codegenbuilderplugin.test.api.request;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class CheckSdkPluginsRequest {

    @CodeGenField("sim卡唯一标示，没有请上传999999999999999，15个9")
    @NotNull
    @Size(min = 15, max = 16)
    private String imsi;

    @CodeGenField("设备唯一标示")
    @NotNull
    @Size(min = 15, max = 16)
    private String imei;

    @CodeGenField("渠道")
    @NotNull
    private String channel;

    @CodeGenField("宿主应用版本号")
    @NotNull
    private String clientVer;

    @CodeGenField("设备品牌")
    @NotNull
    private String brand;

    @CodeGenField("设备型号")
    @NotNull
    private String model;

    @CodeGenField("分辨率")
    @NotNull
    private String resolution;

    @CodeGenField("宿主应用包名")
    @NotNull
    private String packageName;

    @CodeGenField("sdk版本号，int类型")
    @NotNull
    private Integer sdkVer;

    @CodeGenField("插件信息集合")
    @NotNull
    private List<CheckSdkPluginsItem> pluginsInfo;

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getClientVer() {
        return clientVer;
    }

    public void setClientVer(String clientVer) {
        this.clientVer = clientVer;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getSdkVer() {
        return sdkVer;
    }

    public void setSdkVer(Integer sdkVer) {
        this.sdkVer = sdkVer;
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
