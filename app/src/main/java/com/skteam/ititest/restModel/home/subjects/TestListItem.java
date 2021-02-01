package com.skteam.ititest.restModel.home.subjects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class TestListItem{
	private String title;
	@SerializedName("test_id")
	private String testId;

	public String getTitle(){
		return title;
	}

	public String getTestId(){
		return testId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	@Override
 	public String toString(){
		return new Gson().toJson(this,TestListItem.class);
		}
}
