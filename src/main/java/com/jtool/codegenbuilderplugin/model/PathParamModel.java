package com.jtool.codegenbuilderplugin.model;

import java.lang.annotation.Annotation;
import java.util.List;

public class PathParamModel extends ParamModel {

	private List<Annotation> constraint;

	public List<Annotation> getConstraint() {
		return constraint;
	}

	public void setConstraint(List<Annotation> constraint) {
		this.constraint = constraint;
	}

	@Override
	public String toString() {
		return "PathParamModel{" +
				"constraint=" + constraint +
				'}';
	}
}
