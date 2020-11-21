package com.skteam.ititest.restModel.home.subjects;

import java.util.List;

public class SubjectResponse{
	private List<ResItem> res;
	private String code;
	public List<ResItem> getRes(){
		return res;
	}
	public String getCode(){
		return code;
	}

	public void setRes(List<ResItem> res) {
		this.res = res;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
 	public String toString(){
		return 
			"SubjectResponse{" + 
			"res = '" + res + '\'' + 
			",code = '" + code + '\'' + 
			"}";
		}
}