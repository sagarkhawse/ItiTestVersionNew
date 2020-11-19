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
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.skteam.ititest.baseclasses.BaseViewModel;
import com.skteam.ititest.prefrences.SharedPre;
import com.skteam.ititest.restModel.home.leaderboard.LeaderBoardResponse;
import com.skteam.ititest.restModel.home.subjects.Re;
import com.skteam.ititest.restModel.home.subjects.SubjectResponse;
import com.skteam.ititest.restModel.signup.ResponseSignUp;
import com.skteam.ititest.setting.AppConstance;

import java.util.List;

public class HomeViewModel extends BaseViewModel<HomeNav> {
    private MutableLiveData<List<Re>> subjectListMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<List<com.skteam.ititest.restModel.home.leaderboard.Re>> leaderBoardMutabledata=new MutableLiveData<>();
    public HomeViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
    private MutableLiveData<List<Re>> GetAllSubject() {
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
    private MutableLiveData<List<com.skteam.ititest.restModel.home.leaderboard.Re>> GetAllLeaderBoard() {
        getNavigator().setLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.LEADERBOARD)
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
    public LiveData<List<Re>> GetAllSubjectNow(){
        return GetAllSubject();
    }
    public LiveData<List<com.skteam.ititest.restModel.home.leaderboard.Re>>GetAllLeaderBoardNow(){
        return GetAllLeaderBoard();
    }
}
