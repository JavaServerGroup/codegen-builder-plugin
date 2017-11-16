package com.jtool.codegenbuilderplugin.test.api.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BaseCheckSdkPluginsRequest {

    @CodeGenField("sim卡唯一标示，没有请上传999999999999999，15个9")
    @NotNull
    @Size(min = 15, max = 16)
    protected String imsi;

    @CodeGenField("设备唯一标示")
    @NotNull
    @Size(min = 15, max = 16)
    protected String imei;

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
}
