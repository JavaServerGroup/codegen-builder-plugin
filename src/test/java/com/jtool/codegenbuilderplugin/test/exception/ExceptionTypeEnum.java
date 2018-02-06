package com.jtool.codegenbuilderplugin.test.exception;

public enum ExceptionTypeEnum {

	PARAMS_EXCEPTION("-3","参数错误"),
	ORDER_ID_EXCEPTION("-29", "订单id错误");

	private String exceptionCode;
	private String message;

	private ExceptionTypeEnum(String exceptionCode, String message) {
		this.exceptionCode = exceptionCode;
		this.message = message;
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
