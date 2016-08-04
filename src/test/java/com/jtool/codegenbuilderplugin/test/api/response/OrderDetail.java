package com.jtool.codegenbuilderplugin.test.api.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by wen on 16-6-29.
 */
public class OrderDetail {

    public OrderDetail() {
    }

    @NotNull
    @CodeGenField("id, Integer")
    private Integer id;

    @NotNull
    @CodeGenField("定单id, String")
    private String orderId;

    @NotNull
    @CodeGenField("定单状态(0成功,-1失败,1等待), Integer")
    private Integer orderStatus;

    @NotNull
    @CodeGenField("应用id,这定单对应哪个应用的, String")
    private String appId;

    @NotNull
    @CodeGenField("生成的交易id, String")
    private String tradeNo;

    @NotNull
    @CodeGenField("账号, String")
    private String accountId;

    @NotNull
    @CodeGenField("金额, double")
    private Double amount;

    @NotNull
    @CodeGenField("国家, String")
    private String country;

    @NotNull
    @CodeGenField("币种, String")
    private String currency;

    @NotNull
    @CodeGenField("支付成功后,告知商家或应用回调的接口, String")
    private String redirectUrl;

    @NotNull
    @CodeGenField("支付商品的说明, String")
    private String productDesc;

    @NotNull
    @CodeGenField("支付渠道(如:paga or interswitch), String")
    private String payChannel;

    @NotNull
    @CodeGenField("结果说明, String")
    private String resultDesc;

    @NotNull
    @CodeGenField("定单创建的时间, Date")
    private Date createTime;

    @NotNull
    @CodeGenField("最后修改时间, Date")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public OrderDetail setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderDetail setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public OrderDetail setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public OrderDetail setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public OrderDetail setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public OrderDetail setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public OrderDetail setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public OrderDetail setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public OrderDetail setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public OrderDetail setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public OrderDetail setProductDesc(String productDesc) {
        this.productDesc = productDesc;
        return this;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public OrderDetail setPayChannel(String payChannel) {
        this.payChannel = payChannel;
        return this;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public OrderDetail setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public OrderDetail setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public OrderDetail setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", orderStatus=" + orderStatus +
                ", appId='" + appId + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", accountId='" + accountId + '\'' +
                ", amount=" + amount +
                ", country='" + country + '\'' +
                ", currency='" + currency + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                ", productDesc='" + productDesc + '\'' +
                ", payChannel='" + payChannel + '\'' +
                ", resultDesc='" + resultDesc + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
