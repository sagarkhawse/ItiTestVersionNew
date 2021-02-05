package com.skteam.ititest.ui.quiz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.CountDownTimer;
import android.view.MotionEvent;

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
    private CountDownTimer timer;
    private static long QUESTION_TOTAL_TIME = 30000;
    private static long TIMER_VARIATION = 1000;
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
        testName = getIntent().getStringExtra("testName");
        if (getIntent().getStringExtra("test_Id") != null && !getIntent().getStringExtra("test_Id").isEmpty()) {
            testId = getIntent().getStringExtra("test_Id");
        } else {
            dialog = showAlertDialog(this, ERROR, "Quiz will be updated soon!", "ITI Test");
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
        timer = new CountDownTimer(QUESTION_TOTAL_TIME, TIMER_VARIATION) {

            public void onTick(long millisUntilFinished) {
                saveTime = QUESTION_TOTAL_TIME - TIMER_VARIATION;
                String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                binding.time.setText(v + ":" + String.format("%02d", va));
            }

            public void onFinish() {

                binding.submit.performClick();
            }
        };
        binding.submit.setOnClickListener(v -> {
            try {
                if (isSubmited) {
                    showCustomAlert("Test Already Submited!!");
                } else {
                    timer.cancel();
                    isSubmited = true;
                    quizAdapter.UpdateSubmit(true);

                }
            } catch (Exception e) {

            }

        });
        viewModel.GetAllQuiz(testId).observe(this, resItems -> {
            if (resItems != null && resItems.size() > 0) {
                timer.start();
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
            dialog = showAlertDialog(this, ERROR, "Quiz will be updated soon!", "ITI Test");
            dialog.setConfirmText("Go Back")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        dialog.dismissWithAnimation();
                        finish();
                    });
            //dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
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
    public void NoQuestionSelected() {
        try {
            showCustomAlert("Sorry! You Got 0 Points From this Test Series");
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
        showCustomAlert("Test Submited Succesfully!!");
        Gson gson = new Gson();
        String json = gson.toJson(resItems);
        String timerNew = String.valueOf((QUESTION_TOTAL_TIME - saveTime) / 1000);
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