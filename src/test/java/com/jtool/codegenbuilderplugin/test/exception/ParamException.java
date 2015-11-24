package com.jtool.codegenbuilderplugin.test.exception;

import com.jtool.codegenannotation.CodeGenExceptionDefine;

@CodeGenExceptionDefine(code="-3", desc="参数错误")
public class ParamException extends MyCheckedException {
	private static final long serialVersionUID = 1L;
	public ParamException() {
		CodeGenExceptionDefine codeGenExceptionDefine = this.getClass().getAnnotation(CodeGenExceptionDefine.class);
		if(codeGenExceptionDefine != null) {
			this.setCode(codeGenExceptionDefine.code());
			this.setDesc(codeGenExceptionDefine.desc());
		}
	}
	public ParamException(String paramJson) {
		super(paramJson);
		CodeGenExceptionDefine codeGenExceptionDefine = this.getClass().getAnnotation(CodeGenExceptionDefine.class);
		if(codeGenExceptionDefine != null) {
			this.setCode(codeGenExceptionDefine.code());
			this.setDesc(codeGenExceptionDefine.desc());
		}
	}
}
