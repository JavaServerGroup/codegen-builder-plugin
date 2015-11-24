package com.jtool.codegenbuilderplugin.test.api.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SearchUserApiResponse {

    @NotNull
    @CodeGenField("状态码, 0：完成")
    private String code;

    @CodeGenField("查找返回的用户列表")
    private List<User> users;

    @CodeGenField("结果的分页信息")
    private Pages pages;

    @CodeGenField("url的字符串列表")
    private List<String> urls;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Pages getPages() {
        return pages;
    }

    public void setPages(Pages pages) {
        this.pages = pages;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "SearchUserApiResponse{" +
                "code='" + code + '\'' +
                ", users=" + users +
                ", pages=" + pages +
                ", urls=" + urls +
                '}';
    }
}
