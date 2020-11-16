
package com.skteam.ititest.restModel.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Re {

    @SerializedName("date_of_birth")
    private String dateOfBirth;
    @Expose
    private String email;
    @Expose
    private String gender;
    @Expose
    private String name;
    @Expose
    private String phone;
    @SerializedName("profile_pic")
    private String profilePic;
    @SerializedName("user_id")
    private String userId;
    @Expose
    private String verified;

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

}
