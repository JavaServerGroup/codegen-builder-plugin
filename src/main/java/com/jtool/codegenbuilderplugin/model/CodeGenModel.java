package com.jtool.codegenbuilderplugin.model;

import java.util.List;

public class CodeGenModel implements Comparable<CodeGenModel> {

	private Double docSeq;
	private String apiName;
	private String apiMethodName;
	private String description;
	private String host;
	private String url;
	private String forWho;
	private String httpMethod;
	private String requestPojoName;
	private String responsePojoName;
	private List<RequestParamModel> requestParamModelList;
	private List<ResponseParamModel> responseParamModelList;
	private String responseDIY;
	private List<Exception> errorType;
	private String curlExample;

	private Object successReturn;
	private String remark;
	private boolean isDeprecated;

	private boolean isGenSDK;

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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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

	public Object getSuccessReturn() {
		return successReturn;
	}

	public void setSuccessReturn(Object successReturn) {
		this.successReturn = successReturn;
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

	public List<RequestParamModel> getRequestParamModelList() {
		return requestParamModelList;
	}

	public void setRequestParamModelList(List<RequestParamModel> requestParamModelList) {
		this.requestParamModelList = requestParamModelList;
	}

	public List<Exception> getErrorType() {
		return errorType;
	}

	public void setErrorType(List<Exception> errorType) {
		this.errorType = errorType;
	}

	public List<ResponseParamModel> getResponseParamModelList() {
		return responseParamModelList;
	}

	public void setResponseParamModelList(List<ResponseParamModel> responseParamModelList) {
		this.responseParamModelList = responseParamModelList;
	}

	public String getForWho() {
		return forWho;
	}

	public void setForWho(String forWho) {
		this.forWho = forWho;
	}

	public String getRequestPojoName() {
		return requestPojoName;
	}

	public void setRequestPojoName(String requestPojoName) {
		this.requestPojoName = requestPojoName;
	}

	public String getResponsePojoName() {
		return responsePojoName;
	}

	public void setResponsePojoName(String responsePojoName) {
		this.responsePojoName = responsePojoName;
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

	@Override
	public String toString() {
		return "CodeGenModel{" +
				"docSeq=" + docSeq +
				", apiName='" + apiName + '\'' +
				", apiMethodName='" + apiMethodName + '\'' +
				", description='" + description + '\'' +
				", host='" + host + '\'' +
				", url='" + url + '\'' +
				", forWho='" + forWho + '\'' +
				", httpMethod='" + httpMethod + '\'' +
				", requestPojoName='" + requestPojoName + '\'' +
				", responsePojoName='" + responsePojoName + '\'' +
				", requestParamModelList=" + requestParamModelList +
				", responseParamModelList=" + responseParamModelList +
				", responseDIY='" + responseDIY + '\'' +
				", errorType=" + errorType +
				", curlExample='" + curlExample + '\'' +
				", successReturn=" + successReturn +
				", remark='" + remark + '\'' +
				", isDeprecated=" + isDeprecated +
				", isGenSDK=" + isGenSDK +
				'}';
	}
}