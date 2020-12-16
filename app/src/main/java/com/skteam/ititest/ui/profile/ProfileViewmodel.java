package com.skteam.ititest.ui.profile;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.skteam.ititest.application.RestApiInterface;
import com.skteam.ititest.application.RetrofitService;
import com.skteam.ititest.baseclasses.BaseViewModel;
import com.skteam.ititest.prefrences.SharedPre;
import com.skteam.ititest.restModel.editProdile.EditProfileResponse;
import com.skteam.ititest.restModel.editProdile.ResItem;
import com.skteam.ititest.restModel.signup.Re;
import com.skteam.ititest.restModel.signup.ResponseSignUp;
import com.skteam.ititest.setting.AppConstance;
import com.skteam.ititest.setting.Functions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewmodel extends BaseViewModel<ProfileNav> {
    private MutableLiveData<List<Re>> LoginDetaild=new MutableLiveData<>();
    private MutableLiveData<List<ResItem>> ProfileUpdated=new MutableLiveData<>();
    private RestApiInterface servicePost;
    public ProfileViewmodel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
        servicePost = RetrofitService.cteateService(RestApiInterface.class);
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
    private MutableLiveData<List<ResItem>> SaveUserFrofile(File userDpmain){
        getNavigator().setLoading(true);
        MultipartBody.Part user_dp = null;
        RequestBody userid = null;
        if (userDpmain != null) {

            user_dp = Functions.prepareFilePart(getContext(), "file", userDpmain.toString());
            userid = Functions.createPartFromString(getSharedPre().getUserId());
        }
        servicePost.UploadDp(user_dp, userid).enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                getNavigator().setLoading(false);
                if(response.isSuccessful()){
                    if(response.body().getCode().equalsIgnoreCase("200") ){
                        getNavigator().setMessage("Profile Updated");
                        getSharedPre().setClientProfile(response.body().getRes().get(0).getProfilePic());
                        getSharedPre().setEmailProfile(response.body().getRes().get(0).getProfilePic());
                        getNavigator().setMessage("Profile Picture Updated Successfully");
                        ProfileUpdated.postValue(response.body().getRes());
                    }
                }
                }


            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                getNavigator().setMessage("Server Not Responding");
                getNavigator().setLoading(false);
            }
        });

        /*AndroidNetworking.upload(AppConstance.API_BASE_URL + AppConstance.UPDATE_DP)
                .addPathParameter("user_id", getSharedPre().getUserId())
                .addMultipartFile("file",userDp)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            try {
                                if(response.getString("code").equalsIgnoreCase("200")){
                                    getNavigator().setMessage("Profile Updated");
                                }else{
                                    getNavigator().setMessage("Server Not Responding");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });*/
               /* .getAsObject(EditProfileResponse.class, new ParsedRequestListener<EditProfileResponse>() {
                    @Override
                    public void onResponse(EditProfileResponse response) {
                        getNavigator().setLoading(false);
                       https://androappdev.xyz/ItiTest/index.php?p=update_user_dp
                            if (response.getCode().equals("200")) {
                                getSharedPre().setClientProfile(response.getRes().get(0).getProfilePic());
                                getSharedPre().setEmailProfile(response.getRes().get(0).getProfilePic());
                                getNavigator().setMessage("Profile Picture Updated Successfully");
                                ProfileUpdated.postValue(response.getRes());
                            } else {
                                getNavigator().setMessage("Server Not Responding");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().setLoading(false);
                    }*/

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
                                getNavigator().setMessage("Profile Updated!!");
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
