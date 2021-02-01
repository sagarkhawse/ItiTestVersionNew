
package com.skteam.ititest.restModel.home.leaderboard;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Re {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String points;
    @SerializedName("profile_pic")
    private String profilePic;
    @SerializedName("user_id")
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
