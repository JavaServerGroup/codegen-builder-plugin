package com.jtool.codegenbuilderplugin.test.api.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

/**
 * Created by wen on 16-6-30.
 */
public class QuerySingleResponse {

    public QuerySingleResponse() {
    }

    public QuerySingleResponse(OrderDetail order) {
        this.code = "0";
        this.order = order;
    }

    @NotNull
    @CodeGenField("0:成功")
    private String code;

    @NotNull
    @CodeGenField("订单详情")
    private OrderDetail order;

    public String getCode() {
        return code;
    }

    public QuerySingleResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public OrderDetail getOrder() {
        return order;
    }

    public QuerySingleResponse setOrder(OrderDetail order) {
        this.order = order;
        return this;
    }
}
