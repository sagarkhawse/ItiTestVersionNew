package com.skteam.ititest.restModel.home.subjects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChapterListItem{
	@SerializedName("chapter_id")
	private String chapterId;
	private String title;
	@SerializedName("test_list")
	private List<TestListItem> testList;

	public String getChapterId(){
		return chapterId;
	}

	public String getTitle(){
		return title;
	}

	public List<TestListItem> getTestList(){
		return testList;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTestList(List<TestListItem> testList) {
		this.testList = testList;
	}

	@Override
 	public String toString(){
		return 
			"ChapterListItem{" + 
			"chapter_id = '" + chapterId + '\'' + 
			",title = '" + title + '\'' + 
			",test_list = '" + testList + '\'' + 
			"}";
		}
}