package com.skteam.ititest.restModel.home.subjects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResItem{
	@SerializedName("subject_id")
	private String subjectId;
	private String image;
	@SerializedName("chapter_list")
	private List<ChapterListItem> chapterList;
	private String title;
	@SerializedName("chapters_count")
	private String chaptersCount;

	public String getSubjectId(){
		return subjectId;
	}

	public String getImage(){
		return image;
	}

	public List<ChapterListItem> getChapterList(){
		return chapterList;
	}

	public String getTitle(){
		return title;
	}

	public String getChaptersCount(){
		return chaptersCount;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setChapterList(List<ChapterListItem> chapterList) {
		this.chapterList = chapterList;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setChaptersCount(String chaptersCount) {
		this.chaptersCount = chaptersCount;
	}

	@Override
 	public String toString(){
		return 
			"ResItem{" + 
			"subject_id = '" + subjectId + '\'' + 
			",image = '" + image + '\'' + 
			",chapter_list = '" + chapterList + '\'' + 
			",title = '" + title + '\'' + 
			",chapters_count = '" + chaptersCount + '\'' + 
			"}";
		}
}