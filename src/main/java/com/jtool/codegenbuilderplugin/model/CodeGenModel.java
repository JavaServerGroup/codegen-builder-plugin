package com.jtool.codegenbuilderplugin.model;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Optional;

public class CodeGenModel implements Comparable<CodeGenModel> {

	private Double docSeq;
	private String apiName;
	private String apiMethodName;
	private String description;
	private String url;
	private String forWho;
	private String httpMethod;
	private List<ParamModel> requestParamModelList;
	private List<ParamModel> responseParamModelList;
	private String responseDIY;
	private String curlExample;
	private Optional<Class> requestClass;
	private Optional<Class> responseClass;
	private Class[] requestGroups;
	private Class[] responseGroups;

	private String successReturnJsonStr;
	private String remark;
	private boolean isDeprecated;

	private boolean isGenSDK;

	private boolean isRest;

	public boolean isRest() {
		return isRest;
	}

	public void setRest(boolean rest) {
		isRest = rest;
	}

	public Optional<Class> getRequestClass() {
		return requestClass;
	}

	public void setRequestClass(Optional<Class> requestClass) {
		this.requestClass = requestClass;
	}

	@Override
	public int compareTo(CodeGenModel o) {
		return this.getDocSeq().compareTo(o.getDocSeq());
	}

	public Double getDocSeq() {
		return docSeq;
	}

	public void setDocSeq(Double docSeq) {
		this.docSeq = docSeq;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getSuccessReturnJsonStr() {
		return successReturnJsonStr;
	}

	public void setSuccessReturnJsonStr(String successReturnJsonStr) {
		this.successReturnJsonStr = successReturnJsonStr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isDeprecated() {
		return isDeprecated;
	}

	public void setIsDeprecated(boolean isDeprecated) {
		this.isDeprecated = isDeprecated;
	}

	public List<ParamModel> getRequestParamModelList() {
		return requestParamModelList;
	}

	public void setRequestParamModelList(List<ParamModel> requestParamModelList) {
		this.requestParamModelList = requestParamModelList;
	}

	public List<ParamModel> getResponseParamModelList() {
		return responseParamModelList;
	}

	public void setResponseParamModelList(List<ParamModel> responseParamModelList) {
		this.responseParamModelList = responseParamModelList;
	}

	public String getForWho() {
		return forWho;
	}

	public void setForWho(String forWho) {
		this.forWho = forWho;
	}

	public boolean isGenSDK() {
		return isGenSDK;
	}

	public void setIsGenSDK(boolean isGenSDK) {
		this.isGenSDK = isGenSDK;
	}

	public String getApiMethodName() {
		return apiMethodName;
	}

	public void setApiMethodName(String apiMethodName) {
		this.apiMethodName = apiMethodName;
	}

	public String getResponseDIY() {
		return responseDIY;
	}

	public void setResponseDIY(String responseDIY) {
		this.responseDIY = responseDIY;
	}

	public String getCurlExample() {
		return curlExample;
	}

	public void setCurlExample(String curlExample) {
		this.curlExample = curlExample;
	}

	public Optional<Class> getResponseClass() {
		return responseClass;
	}

	public void setResponseClass(Optional<Class> responseClass) {
		this.responseClass = responseClass;
	}

	public Class[] getRequestGroups() {
		return requestGroups;
	}

	public void setRequestGroups(Class[] requestGroups) {
		this.requestGroups = requestGroups;
	}

	public Class[] getResponseGroups() {
		return responseGroups;
	}

	public void setResponseGroups(Class[] responseGroups) {
		this.responseGroups = responseGroups;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}