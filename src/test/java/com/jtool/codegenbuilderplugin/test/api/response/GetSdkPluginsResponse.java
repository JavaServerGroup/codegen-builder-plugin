package com.jtool.codegenbuilderplugin.test.api.response;

import com.jtool.codegenannotation.CodeGenField;
import com.jtool.codegenbuilderplugin.test.api.request.SdkPlugin;

import javax.validation.constraints.NotNull;
import java.util.List;

public class GetSdkPluginsResponse extends BaseResponse {

    @NotNull
    @CodeGenField("sdk插件列表")
    private List<SdkPlugin> sdkPluginsList;

}
