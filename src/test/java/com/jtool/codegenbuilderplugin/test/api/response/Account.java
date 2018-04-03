package com.jtool.codegenbuilderplugin.test.api.response;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenField;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class Account {

    @CodeGenField("id")
    private String id;

    @CodeGenField("公司id")
    private String companyId;

    @CodeGenField("公司名称")
    private String companyName;

    @CodeGenField("账号登录ID")
    private String accountId;

    @CodeGenField("账号显示名称")
    private String accountName;

    //账号登录密码
    private String accountPw;

    @CodeGenField("角色权限")
    private List<String> roleList;

    //保存用户登录的token，每次成功登录刷新
    private String token;

    @CodeGenField("角色：超组管理员、管理员、班组")
    private RoleCategoryEnum roleCategory;

    @CodeGenField("角色可操作的功能模块,客户端点击某模块时进行验证")
    private List<RoleOperatorEnum> roleOperator;

    public String getId() {
        return id;
    }

    public Account setId(String id) {
        this.id = id;
        return this;
    }

    public String getCompanyId() {
        return companyId;
    }

    public Account setCompanyId(String companyId) {
        this.companyId = companyId;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Account setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public Account setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getAccountName() {
        return accountName;
    }

    public Account setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public String getAccountPw() {
        return accountPw;
    }

    public Account setAccountPw(String accountPw) {
        this.accountPw = accountPw;
        return this;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public Account setRoleList(List<String> roleList) {
        this.roleList = roleList;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Account setToken(String token) {
        this.token = token;
        return this;
    }

    public RoleCategoryEnum getRoleCategory() {
        return roleCategory;
    }

    public Account setRoleCategory(RoleCategoryEnum roleCategory) {
        this.roleCategory = roleCategory;
        return this;
    }

    public List<RoleOperatorEnum> getRoleOperator() {
        return roleOperator;
    }

    public Account setRoleOperator(List<RoleOperatorEnum> roleOperator) {
        this.roleOperator = roleOperator;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return new EqualsBuilder()
                .append(id, account.id)
                .append(companyId, account.companyId)
                .append(companyName, account.companyName)
                .append(accountId, account.accountId)
                .append(accountName, account.accountName)
                .append(accountPw, account.accountPw)
                .append(roleList, account.roleList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(companyId)
                .append(companyName)
                .append(accountId)
                .append(accountName)
                .append(accountPw)
                .append(roleList)
                .toHashCode();
    }
}
