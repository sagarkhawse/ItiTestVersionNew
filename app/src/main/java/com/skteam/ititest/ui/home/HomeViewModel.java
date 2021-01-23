/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.home;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.Window;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.skteam.ititest.baseclasses.BaseViewModel;
import com.skteam.ititest.prefrences.SharedPre;
import com.skteam.ititest.restModel.home.leaderboard.LeaderBoardResponse;
import com.skteam.ititest.restModel.home.subjects.ResItem;
import com.skteam.ititest.restModel.home.subjects.SubjectResponse;
import com.skteam.ititest.restModel.signup.Re;
import com.skteam.ititest.restModel.signup.ResponseSignUp;
import com.skteam.ititest.setting.AppConstance;

import java.util.List;

import static com.skteam.ititest.setting.AppConstance.ALL_TIME;
import static com.skteam.ititest.setting.AppConstance.MONTH;
import static com.skteam.ititest.setting.CommonUtils.CurrentTimeAsFormat;

public class HomeViewModel extends BaseViewModel<HomeNav> {
    private MutableLiveData<List<ResItem>> subjectListMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<List<com.skteam.ititest.restModel.home.leaderboard.Re>> leaderBoardMutabledata=new MutableLiveData<>();
    private MutableLiveData<List<Re>>LoginDetaild=new MutableLiveData<>();
    public HomeViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
    private MutableLiveData<List<ResItem>> GetAllSubject() {
        getNavigator().setLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.SUBJECTS)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(SubjectResponse.class, new ParsedRequestListener<SubjectResponse>() {
                    @Override
                    public void onResponse(SubjectResponse response) {
                        getNavigator().setLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                                subjectListMutableLiveData.postValue(response.getRes());
                            } else {
                                getNavigator().setMessage("Please try again later!");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().setLoading(false);
                        getNavigator().setMessage("Server not Responding");
                    }
                });
        return subjectListMutableLiveData;
    }
    private MutableLiveData<List<com.skteam.ititest.restModel.home.leaderboard.Re>> GetAllLeaderBoard(String type) {
        String date= String.valueOf(System.currentTimeMillis());
        String dateF=CurrentTimeAsFormat(date);
        getNavigator().setLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.LEADERBOARD)
                .addBodyParameter("type",type)
                .addBodyParameter("date",dateF)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(LeaderBoardResponse.class, new ParsedRequestListener<LeaderBoardResponse>() {
                    @Override
                    public void onResponse(LeaderBoardResponse response) {
                        getNavigator().setLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                                leaderBoardMutabledata.postValue(response.getRes());
                            } else {
                                getNavigator().setMessage("Please try again later!");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().setLoading(false);
                        getNavigator().setMessage("Server not Responding");
                    }
                });
        return leaderBoardMutabledata;
    }
    private MutableLiveData<List<Re>> GetAllDetailsOfUserId(){
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
    public LiveData<List<ResItem>> GetAllSubjectNow(){
        return GetAllSubject();
    }
    public LiveData<List<com.skteam.ititest.restModel.home.leaderboard.Re>>GetAllLeaderBoardNow(){
        return GetAllLeaderBoard(ALL_TIME);
    }
    public LiveData<List<Re>>GetAllUserDetails(){
        if(LoginDetaild!=null){
           return GetAllDetailsOfUserId();
        }else{
            return LoginDetaild;
        }
    }

}
