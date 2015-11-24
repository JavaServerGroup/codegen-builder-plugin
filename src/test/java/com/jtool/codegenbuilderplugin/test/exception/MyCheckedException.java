package com.jtool.codegenbuilderplugin.test.exception;

public abstract class MyCheckedException extends Exception{
	private static final long serialVersionUID = 1L;
	public String code;
	public String desc;
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
	public MyCheckedException(String message) {
		super(message);
	}
	public MyCheckedException() {
		super();
	}
}
