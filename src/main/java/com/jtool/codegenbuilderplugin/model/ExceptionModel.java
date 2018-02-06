package com.jtool.codegenbuilderplugin.model;

import com.alibaba.fastjson.JSON;

public class ExceptionModel implements Comparable<ExceptionModel> {

	private Integer code;
	private String desc;

	@Override
	public int compareTo(ExceptionModel o) {
		return o.getCode().compareTo(this.getCode());
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}