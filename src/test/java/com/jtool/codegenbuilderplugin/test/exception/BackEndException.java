package com.jtool.codegenbuilderplugin.test.exception;

import com.jtool.codegenannotation.CodeGenExceptionDefine;

@CodeGenExceptionDefine(code="-98", desc="后端错误")
public class BackEndException extends MyCheckedException {
	private static final long serialVersionUID = 6255503754366082895L;
	public BackEndException(){
		super();
		CodeGenExceptionDefine codeGenExceptionDefine = this.getClass().getAnnotation(CodeGenExceptionDefine.class);
		if(codeGenExceptionDefine != null) {
			this.setCode(codeGenExceptionDefine.code());
			this.setDesc(codeGenExceptionDefine.desc());
		}
	}
}
