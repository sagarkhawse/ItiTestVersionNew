package com.skteam.ititest.restModel.scoreView;

import com.google.gson.annotations.SerializedName;

public class ResItem{

	@SerializedName("user_id")
	private String userId;

	@SerializedName("points")
	private String points;

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setPoints(String points){
		this.points = points;
	}

	public String getPoints(){
		return points;
	}

	@Override
 	public String toString(){
		return 
			"ResItem{" + 
			"user_id = '" + userId + '\'' + 
			",points = '" + points + '\'' + 
			"}";
		}
}