package com.jtool.codegenbuilderplugin.test.api.request;

import com.jtool.codegenannotation.CodeGenField;
import com.jtool.codegenbuilderplugin.test.validate.Get;

import javax.validation.constraints.NotNull;
import java.util.List;

public class GetSdkConfigResponse extends BaseResponse {

    @NotNull
    @CodeGenField(value = "sdk插件列表", groups = Get.class)
    private List<SdkConfig> sdkConfigsList;

    public List<SdkConfig> getSdkConfigsList() {
        return sdkConfigsList;
    }

    public void setSdkConfigsList(List<SdkConfig> sdkConfigsList) {
        this.sdkConfigsList = sdkConfigsList;
    }

}
