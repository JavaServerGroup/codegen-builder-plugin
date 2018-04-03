package com.jtool.codegenbuilderplugin.test.api.request;

import com.alibaba.fastjson.JSON;
import com.jtool.annotation.AvailableValues;
import com.jtool.codegenannotation.CodeGenField;
import com.jtool.codegenbuilderplugin.test.validate.Get;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SdkConfig {

    @NotNull
    @CodeGenField(value = "唯一id", groups = Get.class)
    private String id;

    @CodeGenField(value = "插件对象", groups = Get.class)
    private SdkPlugin sdkPlugin;

    @CodeGenField(value = "sdkPlugin的Id", groups = {Add.class})
    @NotNull(groups = {Add.class})
    private String sdkPluginId;

    @CodeGenField(value = "渠道", groups = {Add.class, Get.class})
    @NotNull(groups = {Add.class})
    private String channel;

    @CodeGenField(value = "国家列表", groups = {Add.class, Get.class})
    @NotNull(groups = {Add.class})
    private List<Integer> countryList;

    @CodeGenField(value = "运营商列表", groups = {Add.class, Get.class})
    @NotNull(groups = {Add.class})
    private List<Integer> operatorsList;

    @CodeGenField(value = "品牌", groups = {Add.class, Get.class})
    @NotNull(groups = {Add.class})
    @AvailableValues(values = {"ALL", "TRANSSION", "OTHERS"})
    private String brand;

    @CodeGenField(value = "目标包名", groups = {Add.class, Get.class})
    private String targetPackageName = "ALL";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SdkPlugin getSdkPlugin() {
        return sdkPlugin;
    }

    public void setSdkPlugin(SdkPlugin sdkPlugin) {
        this.sdkPlugin = sdkPlugin;
    }

    public String getSdkPluginId() {
        return sdkPluginId;
    }

    public void setSdkPluginId(String sdkPluginId) {
        this.sdkPluginId = sdkPluginId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<Integer> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Integer> countryList) {
        this.countryList = countryList;
    }

    public List<Integer> getOperatorsList() {
        return operatorsList;
    }

    public void setOperatorsList(List<Integer> operatorsList) {
        this.operatorsList = operatorsList;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTargetPackageName() {
        return targetPackageName;
    }

    public void setTargetPackageName(String targetPackageName) {
        this.targetPackageName = targetPackageName;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SdkConfig sdkConfig = (SdkConfig) o;

        if (id != null ? !id.equals(sdkConfig.id) : sdkConfig.id != null) return false;
        if (sdkPlugin != null ? !sdkPlugin.equals(sdkConfig.sdkPlugin) : sdkConfig.sdkPlugin != null) return false;
        if (sdkPluginId != null ? !sdkPluginId.equals(sdkConfig.sdkPluginId) : sdkConfig.sdkPluginId != null)
            return false;
        if (channel != null ? !channel.equals(sdkConfig.channel) : sdkConfig.channel != null) return false;
        if (countryList != null ? !countryList.equals(sdkConfig.countryList) : sdkConfig.countryList != null)
            return false;
        if (operatorsList != null ? !operatorsList.equals(sdkConfig.operatorsList) : sdkConfig.operatorsList != null)
            return false;
        if (brand != null ? !brand.equals(sdkConfig.brand) : sdkConfig.brand != null) return false;
        return targetPackageName != null ? targetPackageName.equals(sdkConfig.targetPackageName) : sdkConfig.targetPackageName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sdkPlugin != null ? sdkPlugin.hashCode() : 0);
        result = 31 * result + (sdkPluginId != null ? sdkPluginId.hashCode() : 0);
        result = 31 * result + (channel != null ? channel.hashCode() : 0);
        result = 31 * result + (countryList != null ? countryList.hashCode() : 0);
        result = 31 * result + (operatorsList != null ? operatorsList.hashCode() : 0);
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (targetPackageName != null ? targetPackageName.hashCode() : 0);
        return result;
    }
}
