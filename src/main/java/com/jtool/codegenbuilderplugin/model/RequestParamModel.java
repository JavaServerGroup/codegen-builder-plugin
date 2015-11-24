package com.jtool.codegenbuilderplugin.model;

import java.lang.annotation.Annotation;
import java.util.List;

public class RequestParamModel extends ParamModel {
	
	private List<Annotation> constraint;

	public List<Annotation> getConstraint() {
		return constraint;
	}

	public void setConstraint(List<Annotation> constraint) {
		this.constraint = constraint;
	}

	@Override
	public String toString() {
		return "RequestParamModel{" +
				"key='" + key + '\'' +
				", required=" + required +
				", constraint=" + constraint +
				", comment='" + comment + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}
