package com.skteam.ititest.restModel.quiz;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class QuizResponse{

	@SerializedName("res")
	private List<ResItem> res;

	@SerializedName("code")
	private String code;

	public void setRes(List<ResItem> res){
		this.res = res;
	}

	public List<ResItem> getRes(){
		return res;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}
}