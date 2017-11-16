package com.jtool.codegenbuilderplugin.test.api.request;

import com.jtool.codegenannotation.CodeGenField;

public class AppFullAd extends BaseAdRequest {

    @CodeGenField("广告状态")
    private Boolean adStart;

    @CodeGenField("广告标题")
    private String adTitle;

    public Boolean getAdStart() {
        return adStart;
    }

    public void setAdStart(Boolean adStart) {
        this.adStart = adStart;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }
}
