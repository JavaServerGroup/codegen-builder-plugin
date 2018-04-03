package com.jtool.codegenbuilderplugin.test.api.request;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class LoginRequest {

    public LoginRequest() {
    }

    public LoginRequest(@NotNull String accountId, @NotNull String password) {
        this.accountId = accountId;
        this.password = password;
    }

    @CodeGenField("用户账号（目前是手机号）")
    @NotNull
    private String accountId;

    @CodeGenField("密码")
    @NotNull
    private String password;

    public String getAccountId() {
        return accountId;
    }

    public LoginRequest setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
