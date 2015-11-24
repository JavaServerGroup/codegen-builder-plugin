package com.jtool.codegenbuilderplugin.test.api.response;

import com.jtool.codegenannotation.CodeGenField;

public class Pages {

    @CodeGenField("结果共有都少页")
    private Integer totalPage;

    @CodeGenField("结果集的版本号")
    private String version;

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Pages{" +
                "totalPage=" + totalPage +
                ", version='" + version + '\'' +
                '}';
    }
}
