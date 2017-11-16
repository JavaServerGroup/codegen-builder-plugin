package com.jtool.codegenbuilderplugin.test.api.request;

import com.alibaba.fastjson.JSON;

public abstract class BaseAdRequest {

    private String id;
    private String adName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
