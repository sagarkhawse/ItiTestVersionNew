package com.skteam.ititest.ui.quiz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseActivity;
import com.skteam.ititest.databinding.FragmentQuizBinding;
import com.skteam.ititest.restModel.quiz.ResItem;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.setting.dialog.SweetAlertDialog;
import com.skteam.ititest.ui.quiz.adapter.QuizAdapter;

import java.util.List;

import static com.skteam.ititest.setting.AppConstance.ERROR;
import static com.skteam.ititest.setting.AppConstance.WARNING;


public class QuizActivity extends BaseActivity<FragmentQuizBinding, QuizViewModel> implements QuizAdapter.NoQuestionSelected, QuizNav {
    private QuizViewModel viewModel;
    private FragmentQuizBinding binding;
    private Dialog internetDialog;
    private Activity instance;
    private static String testId;
    private SweetAlertDialog dialog;
    private QuizAdapter quizAdapter;
    private SnapHelper postionHelper, quizHelper;
    private boolean isSubmited = false;
    private LinearLayoutManager manager;
    private Stopwatch stopwatch;
    private InterstitialAd mInterstitialAd;
   // private CountDownTimer timer;
   // private static long QUESTION_TOTAL_TIME = 30000;
   // private static long TIMER_VARIATION = 1000;
    private long saveTime = 0;
    private String testName;

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_quiz;
    }

    @Override
    public QuizViewModel getViewModel() {
        return viewModel = new QuizViewModel(this, getSharedPre(), this);
    }

    public FragmentQuizBinding getBinding() {
        return binding;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
        instance = this;
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        stopwatch=new Stopwatch();
        testName = getIntent().getStringExtra("testName");


        InterstitialAd.load(this,getString(R.string.InterstiatladdMobId), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
                Log.i("Add Mob Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i("Add Mob Ads", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

        binding.tvTitle.setText(testName);
        if (getIntent().getStringExtra("test_Id") != null && !getIntent().getStringExtra("test_Id").isEmpty()) {
            testId = getIntent().getStringExtra("test_Id");
        } else {
            dialog = showAlertDialog(this, ERROR, "Points will be updated soon!", "ITI Test");
            dialog.setConfirmText("Go Back")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        dialog.dismissWithAnimation();
                        finish();
                    });
            //dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            dialog.show();
        }
        manager = new LinearLayoutManager(instance, RecyclerView.HORIZONTAL, false);
        binding.questionsList.setLayoutManager(manager);
        quizAdapter = new QuizAdapter(this, this);
        quizHelper = new PagerSnapHelper();
        quizHelper.attachToRecyclerView(binding.questionsList);
        binding.time.setVisibility(View.GONE);
        stopwatch.start();
        binding.questionsList.setAdapter(quizAdapter);
        /*binding.questionsList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return e.getAction() == MotionEvent.ACTION_MOVE;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/
       /* timer = new CountDownTimer(QUESTION_TOTAL_TIME, TIMER_VARIATION) {

            public void onTick(long millisUntilFinished) {
                saveTime = QUESTION_TOTAL_TIME - TIMER_VARIATION;
                String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                binding.time.setText(v + ":" + String.format("%02d", va));
            }

            public void onFinish() {

                binding.submit.performClick();
            }
        };*/
        binding.submit.setOnClickListener(v -> {
            try {
                if (isSubmited) {
                    showCustomAlert("Test Already Submitted!!");
                } else {
                   // timer.cancel();
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(QuizActivity.this);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }
                    isSubmited = true;
                    stopwatch.stop();
                    quizAdapter.UpdateSubmit(true);

                }
            } catch (Exception e) {

            }

        });
        viewModel.GetAllQuiz(testId).observe(this, resItems -> {
            if (resItems != null && resItems.size() > 0) {
                //timer.start();
//                timer.start();
                quizAdapter.UpdateList(resItems);

            }
        });


    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(this, false);
        }
        if (isConnected) {
            internetDialog.dismiss();

        } else {
            internetDialog.show();
        }
    }

    @Override
    public void setLoading(boolean b) {
        if (b) {
            showLoadingDialog("");
        } else {
            hideLoadingDialog();
        }
    }

    @Override
    public void setMessage(String s) {
        try {
            dialog.setConfirmText("Go Back")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        dialog.dismissWithAnimation();
                        finish();
                    });
            dialog.show();
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        try {
            dialog = showAlertDialog(this, WARNING, "Do You want to cancel Your Test", "ITI Test");
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dialog.dismissWithAnimation();
                }
            });
            dialog.setConfirmText("Yes")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        dialog.dismissWithAnimation();
                        finish();
                    });

            dialog.show();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopwatch.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopwatch.pause();
    }

    @Override
    public void NoQuestionSelected() {
        try {
            showCustomAlert("Sorry! You Got 0 Points From this Test ");
            dialog = showAlertDialog(this, ERROR, "No Question Selected", "Times Up!!");
            dialog.setConfirmText("Yes")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        dialog.dismissWithAnimation();
                        finish();
                    });

            dialog.show();
        } catch (Exception e) {

        }
    }

    @Override
    public void getResult(List<ResItem> resItems) {
        showCustomAlert("Test Submitted Successfully!!");
        Gson gson = new Gson();
        String json = gson.toJson(resItems);
        String timerNew = String.valueOf((stopwatch.getElapsedTimeMili()) / 1000);
        startActivityForResult(new Intent(QuizActivity.this, ReportActivity.class)
                        .putExtra("data", json)
                        .putExtra("timeMain", timerNew)
                        .putExtra("testName", testName)
                , 001);
    }

    @Override
    public void UpdatePreviousButton() {
        int firstVisible = manager.findFirstVisibleItemPosition() - 1;
        int lastVisible = manager.findLastVisibleItemPosition() + 1;

        if (firstVisible <= quizAdapter.getItemCount()) {
            manager.scrollToPosition(firstVisible);
        }
    }

    @Override
    public void UpdateNextButton() {
        int firstVisible = manager.findFirstVisibleItemPosition() - 1;
        int lastVisible = manager.findLastVisibleItemPosition() + 1;

        if (lastVisible <= quizAdapter.getItemCount()) {
            manager.scrollToPosition(lastVisible);
        }
        if(lastVisible==quizAdapter.getItemCount()){
            showCustomAlert("Please submit your test to know your result!!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 001: {
                finish();
            }
        }
    }
}