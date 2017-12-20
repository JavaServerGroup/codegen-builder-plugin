package com.jtool.codegenbuilderplugin.test.api.request;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenField;
import com.jtool.codegenbuilderplugin.test.validate.Get;
import com.jtool.codegenbuilderplugin.test.validate.Modify;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

public class SdkPlugin {

    @NotNull(groups = {Default.class, Modify.class})
    @CodeGenField(value = "唯一id", groups = {Default.class, Modify.class})
    private String id;

    @NotNull(groups = Default.class)
    @CodeGenField(value = "插件包的md5计算值", groups = Default.class)
    private String md5;

    @CodeGenField(value = "插件名称", groups = {Default.class, Add.class, Modify.class, Get.class})
    @NotNull(groups = {Default.class, Add.class, Modify.class})
    @Size(min = 1, max = 50, groups = {Default.class, Add.class, Modify.class})
    private String name;

    @CodeGenField(value = "插件包的下载url", groups = {Default.class, Add.class, Modify.class})
    @NotNull(groups = {Default.class, Add.class, Modify.class})
    @Size(min = 1, max = 500, groups = {Default.class, Add.class, Modify.class})
    private String url;

    @CodeGenField(value = "插件版本", groups = {Default.class, Add.class, Modify.class, Get.class})
    @NotNull(groups = {Default.class, Add.class, Modify.class})
    @Min(value = 0, groups = {Default.class, Add.class, Modify.class})
    private Integer version;

    @CodeGenField(value = "插件包大小", groups = {Default.class, Add.class, Modify.class})
    @NotNull(groups = {Default.class, Add.class, Modify.class})
    @Min(value = 0, groups = {Default.class, Add.class, Modify.class})
    private Integer size;

    @CodeGenField(value = "插件包描述", groups = {Default.class, Add.class, Modify.class})
    @NotNull(groups = {Default.class, Add.class, Modify.class})
    @Size(min = 1, groups = {Default.class, Add.class, Modify.class})
    private String desc;

    @CodeGenField(value = "启动类的名称", groups = {Default.class, Add.class, Modify.class})
    @Size(min = 1, max = 1000, groups = {Default.class, Add.class, Modify.class})
    @NotNull(groups = {Default.class, Add.class, Modify.class})
    private String startPoint;

    @Size(min = 1, max = 1000, groups = {Default.class, Add.class, Modify.class})
    @NotNull(groups = {Default.class, Add.class, Modify.class})
    @CodeGenField(value = "插件的包名", groups = {Default.class, Add.class, Modify.class})
    private String pluginPackageName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

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

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getPluginPackageName() {
        return pluginPackageName;
    }

    public void setPluginPackageName(String pluginPackageName) {
        this.pluginPackageName = pluginPackageName;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SdkPlugin sdkPlugin = (SdkPlugin) o;

        if (id != null ? !id.equals(sdkPlugin.id) : sdkPlugin.id != null) return false;
        if (name != null ? !name.equals(sdkPlugin.name) : sdkPlugin.name != null) return false;
        if (url != null ? !url.equals(sdkPlugin.url) : sdkPlugin.url != null) return false;
        if (version != null ? !version.equals(sdkPlugin.version) : sdkPlugin.version != null) return false;
        if (size != null ? !size.equals(sdkPlugin.size) : sdkPlugin.size != null) return false;
        if (desc != null ? !desc.equals(sdkPlugin.desc) : sdkPlugin.desc != null) return false;
        if (startPoint != null ? !startPoint.equals(sdkPlugin.startPoint) : sdkPlugin.startPoint != null) return false;
        if (pluginPackageName != null ? !pluginPackageName.equals(sdkPlugin.pluginPackageName) : sdkPlugin.pluginPackageName != null)
            return false;
        return md5 != null ? md5.equals(sdkPlugin.md5) : sdkPlugin.md5 == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (startPoint != null ? startPoint.hashCode() : 0);
        result = 31 * result + (pluginPackageName != null ? pluginPackageName.hashCode() : 0);
        result = 31 * result + (md5 != null ? md5.hashCode() : 0);
        return result;
    }
}
