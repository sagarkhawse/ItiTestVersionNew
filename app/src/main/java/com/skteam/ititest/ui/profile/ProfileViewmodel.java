package com.skteam.ititest.ui.profile;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.skteam.ititest.baseclasses.BaseViewModel;
import com.skteam.ititest.prefrences.SharedPre;
import com.skteam.ititest.restModel.editProdile.EditProfileResponse;
import com.skteam.ititest.restModel.editProdile.ResItem;
import com.skteam.ititest.restModel.signup.Re;
import com.skteam.ititest.restModel.signup.ResponseSignUp;
import com.skteam.ititest.setting.AppConstance;

import java.io.File;
import java.util.List;

public class ProfileViewmodel extends BaseViewModel<ProfileNav> {
    private MutableLiveData<List<Re>> LoginDetaild=new MutableLiveData<>();
    private MutableLiveData<List<ResItem>> ProfileUpdated=new MutableLiveData<>();
    public ProfileViewmodel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
    public LiveData<List<Re>> GetAllUserDetails(){
        if(LoginDetaild!=null){
            return GetAllDetailsOfUserId();
        }else{
            return LoginDetaild;
        }
    }
    private MutableLiveData<List<Re>> GetAllDetailsOfUserId(){
        getNavigator().setLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.SIGN_UP)
                .addBodyParameter("user_id", getSharedPre().getUserId())
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(ResponseSignUp.class, new ParsedRequestListener<ResponseSignUp>() {
                    @Override
                    public void onResponse(ResponseSignUp response) {
                        getNavigator().setLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                                LoginDetaild.postValue(response.getRes());
                            } else {
                                getNavigator().setMessage("Server Not Responding");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().setLoading(false);
                    }
                });
        return LoginDetaild;
    }
    private MutableLiveData<List<ResItem>> SaveUserFrofile(File userDp){
        getNavigator().setLoading(true);
        AndroidNetworking.upload(AppConstance.API_BASE_URL + AppConstance.UPDATE_DP)
                .addQueryParameter("user_id", getSharedPre().getUserId())
                .addMultipartFile("file",userDp)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(EditProfileResponse.class, new ParsedRequestListener<EditProfileResponse>() {
                    @Override
                    public void onResponse(EditProfileResponse response) {
                        getNavigator().setLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                                getSharedPre().setClientProfile(response.getRes().get(0).getProfilePic());
                                getSharedPre().setEmailProfile(response.getRes().get(0).getProfilePic());
                                ProfileUpdated.postValue(response.getRes());
                            } else {
                                getNavigator().setMessage("Server Not Responding");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().setLoading(false);
                    }
                });
        return ProfileUpdated;
    }
    public LiveData<List<ResItem>>UploadProfile(File dp){
        return SaveUserFrofile(dp);
    }
    public void EditNow(String name,String phone,String dob,String gender){
        getNavigator().setLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.UPLOAD_USER_DATA)
                .addBodyParameter("user_id", name)
                .addBodyParameter("name", getSharedPre().getUserId())
                .addBodyParameter("phone", phone)
                .addBodyParameter("date_of_birth", dob)
                .addBodyParameter("gender", gender)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(EditProfileResponse.class, new ParsedRequestListener<EditProfileResponse>() {
                    @Override
                    public void onResponse(EditProfileResponse response) {
                        getNavigator().setLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                                getSharedPre().setName(response.getRes().get(0).getName());
                                getSharedPre().setUserEmail(response.getRes().get(0).getEmail());
                                getSharedPre().setClientProfile(response.getRes().get(0).getProfilePic());
                                getSharedPre().setEmailProfile(response.getRes().get(0).getProfilePic());
                                getNavigator().OkDone();
                            } else {
                                getNavigator().setMessage("Server Not Responding");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().setMessage("Server Not Responding");
                        getNavigator().setLoading(false);
                    }
                });
    }

}
