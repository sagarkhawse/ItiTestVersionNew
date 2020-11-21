
package com.skteam.ititest.restModel.home.subjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Re {

    @Expose
    private String image;

    @SerializedName("subject_id")
    private int subjectId;

    @SerializedName("chapter_id")
    private int chapterId;

    @SerializedName("test_id")
    private int testId;

    @Expose
    private String title;
    @SerializedName("chapters_count")
    private String chapters;



    @SerializedName("chapter_list")
    private List<Re> chapterList;

    @SerializedName("test_list")
    private List<Re> testList;

    public String getImage() {
        return image;
    }

    public Re(int chapterId, String title, List<Re> testList) {
        this.chapterId = chapterId;
        this.title = title;
        this.testList = testList;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChapters() {
        return chapters;
    }

    public void setChapters(String chapters) {
        this.chapters = chapters;
    }

    public List<Re> getChapterList() {
        return chapterList;
    }

    public int getChapterId() {
        return chapterId;
    }

    public int getTestId() {
        return testId;
    }

    public List<Re> getTestList() {
        return testList;
    }

    /**
     *for populating chapter spinner
     * @return title (chapter)
     */
    @Override
    public String toString() {
        return title;
    }
}
