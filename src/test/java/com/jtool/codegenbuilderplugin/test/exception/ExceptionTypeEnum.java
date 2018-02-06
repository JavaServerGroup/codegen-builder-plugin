package com.jtool.codegenbuilderplugin.test.exception;

import com.jtool.codegenannotation.CodeGenExceptionTypeEnum;

@CodeGenExceptionTypeEnum
public enum ExceptionTypeEnum {

	PARAMS_EXCEPTION("-3","参数错误"),
	BACK_END_EXCEPTION("-98", "后端错误");

	private String code;
	private String desc;

	private ExceptionTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
