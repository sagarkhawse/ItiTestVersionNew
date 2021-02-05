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
import com.skteam.ititest.restModel.quiz.QuizResponse;
import com.skteam.ititest.restModel.quiz.ResItem;
import com.skteam.ititest.restModel.scoreupdate.ScoreUpdateResponse;
import com.skteam.ititest.setting.AppConstance;
import com.skteam.ititest.setting.CommonUtils;

import java.util.List;

public class QuizViewModel extends BaseViewModel<QuizNav> {
    private MutableLiveData<List<ResItem>>QuizLiveData=new MutableLiveData<>();
    public QuizViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
    private MutableLiveData<List<ResItem>> getAllQuizData(String testId){
        getNavigator().setLoading(true);
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.QUIZ)
                .addBodyParameter("test_id",testId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(QuizResponse.class, new ParsedRequestListener<QuizResponse>() {
                    @Override
                    public void onResponse(QuizResponse response) {
                        getNavigator().setLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                               QuizLiveData.postValue(response.getRes());
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
    public LiveData<List<ResItem>>GetAllQuiz(String testId){
        return getAllQuizData(testId);
    }

    public void UpdateTheScore(String score) {
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.SCORE_UPDATED)
                .addBodyParameter("user_id",getSharedPre().getUserId())
                .addBodyParameter("points",score)
                .addBodyParameter("date", CommonUtils.getFormattedDate(getContext(),System.currentTimeMillis(),"yy-MM-dd h.mm.ss"))
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(ScoreUpdateResponse.class, new ParsedRequestListener<ScoreUpdateResponse>() {
                    @Override
                    public void onResponse(ScoreUpdateResponse response) {
                        getNavigator().setLoading(false);
                        if (response != null) {
                            if (response.getCode().equals("200")) {
                               getSharedPre().setUserScore(score);
                               getNavigator().setMessage("Score Updated Successfully");
                            } else {

                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().setLoading(false);
                    }
                });
    }
}
