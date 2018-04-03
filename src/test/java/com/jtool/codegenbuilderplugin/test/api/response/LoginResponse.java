package com.jtool.codegenbuilderplugin.test.api.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class LoginResponse extends BaseResponse {

    @NotNull
    @CodeGenField("令牌，后续接口需要带上，用来校验登陆状态")
    private String token;

    @NotNull
    @CodeGenField("用户的信息")
    private Account account;

    public Account getAccount() {
        return account;
    }

    public LoginResponse setAccount(Account account) {
        this.account = account;
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
