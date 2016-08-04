package com.jtool.codegenbuilderplugin.test.api.request;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

/**
 * Created by chenfengbin on 16-6-24.
 */
public class PrePaymentRequest {

    @NotNull
    @CodeGenField("注册时生成的应用ID")
    private String appId;

    @NotNull
    @CodeGenField("账号ID")
    private String accountId;

    @CodeGenField("手机号码")
    private String phoneNum;

    @NotNull
    @CodeGenField("金额,保留两位小数")
    private String amount;

    @NotNull
    @CodeGenField("币种")
    private String currency;

    @NotNull
    @CodeGenField("提交支付后回调的结果页面")
    private String returnUrl;

    @NotNull
    @CodeGenField("应用生成的交易定单Id,")
    private String tradeNo;

    @NotNull
    @CodeGenField("支付说明,主要用于页面显示,json格式如:{\n" +
            "    \"0\": {\n" +
            "        \"key\": \"item\",\n" +
            "        \"value\": \"Recharge 100 PCoin\"\n" +
            "    },\n" +
            "    \"1\": {\n" +
            "        \"key\": \"palyId\",\n" +
            "        \"value\": \"123456\"\n" +
            "    }\n" +
            "}")
    private String productDesc;

    @NotNull
    @CodeGenField("时间戳,13位")
    private String timestamp;

    @NotNull
    @CodeGenField("(appId+accountId+amount+returnUrl+tradeNo+productDesc+timestamp+appSecret)" +
            "用32位小写的MD5加密")
    private String sign;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
