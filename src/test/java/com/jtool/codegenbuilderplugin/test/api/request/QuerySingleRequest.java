package com.jtool.codegenbuilderplugin.test.api.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

/**
 * Created by wen on 16-6-30.
 */
public class QuerySingleRequest {

    @NotNull
    @CodeGenField("定单id")
    private String orderId;

    @NotNull
    @CodeGenField("账号")
    private String accountId;

    @NotNull
    @CodeGenField("我的Id")
    private String myId;

    @NotNull
    @CodeGenField("订单")
    private String order;

    public String getOrderId() {
        return orderId;
    }

    public QuerySingleRequest setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public QuerySingleRequest setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }


}
