package com.skteam.ititest.ui.quiz;

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
import com.skteam.ititest.setting.AppConstance;

import java.util.List;

public class QuizViewModel extends BaseViewModel<QuizNav> {
    private MutableLiveData<List<String>>QuizLiveData=new MutableLiveData<>();
    public QuizViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
    private MutableLiveData<List<String>> getAllQuizData(String testId){
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.QUIZ)
                .addBodyParameter("test_id",testId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(LeaderBoardResponse.class, new ParsedRequestListener<LeaderBoardResponse>() {
                    @Override
                    public void onResponse(LeaderBoardResponse response) {
                        getNavigator().setLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                               // QuizLiveData.postValue(response.getRes().toString());
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
        return QuizLiveData;
    }
    public LiveData<List<String>>GetAllQuiz(String testId){
        return getAllQuizData(testId);
    }
}
