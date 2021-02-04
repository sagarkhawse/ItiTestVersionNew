package com.skteam.ititest.restModel.scoreupdate;

import com.google.gson.annotations.SerializedName;

public class ScoreUpdateResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("code")
	private String code;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	@Override
 	public String toString(){
		return 
			"ScoreUpdateResponse{" + 
			"msg = '" + msg + '\'' + 
			",code = '" + code + '\'' + 
			"}";
		}
}