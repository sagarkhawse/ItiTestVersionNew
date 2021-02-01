package com.skteam.ititest.ui.leaderboard;

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
import com.skteam.ititest.restModel.home.leaderboard.Re;
import com.skteam.ititest.setting.AppConstance;

import java.util.List;

import static com.skteam.ititest.setting.CommonUtils.CurrentTimeAsFormat;

public class LeaderBoardViewmodel extends BaseViewModel<LeaderNav> {
    private MutableLiveData<List<com.skteam.ititest.restModel.home.leaderboard.Re>> leaderBoardMutabledata=new MutableLiveData<>();
    public LeaderBoardViewmodel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
    private MutableLiveData<List<Re>> GetAllLeaderBoard(String type) {
        String date= String.valueOf(System.currentTimeMillis());
        getNavigator().setLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.LEADERBOARD)
                .addBodyParameter("date",CurrentTimeAsFormat(date))
                .addBodyParameter("type",type)
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
    public LiveData<List<Re>> GetAllLeaderBoardNow(String type){
        return GetAllLeaderBoard(type);
    }
}
