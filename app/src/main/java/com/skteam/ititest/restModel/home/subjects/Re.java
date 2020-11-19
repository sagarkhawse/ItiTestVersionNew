
package com.skteam.ititest.restModel.home.subjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Re {

    @Expose
    private String image;
    @SerializedName("subject_id")
    private String subjectId;
    @Expose
    private String title;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
