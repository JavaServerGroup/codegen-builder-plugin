package com.jtool.codegenbuilderplugin.model;

import java.util.ArrayList;
import java.util.List;

public class ResponseParamModel extends ParamModel implements Comparable<ResponseParamModel> {

	private List<ResponseParamModel> subResponseParamModel = new ArrayList<>();

	public List<ResponseParamModel> getSubResponseParamModel() {
		return subResponseParamModel;
	}

	public void setSubResponseParamModel(List<ResponseParamModel> subResponseParamModel) {
		this.subResponseParamModel = subResponseParamModel;
	}

	@Override
	public String toString() {
		return "ResponseParamModel{" +
				"key='" + key + '\'' +
				", required=" + required +
				", comment='" + comment + '\'' +
				", type='" + type + '\'' +
				", subResponseParamMeodel=" + subResponseParamModel +
				'}';
	}

	@Override
	public int compareTo(ResponseParamModel o) {
		return this.getKey().compareTo(o.getKey());
	}
}
