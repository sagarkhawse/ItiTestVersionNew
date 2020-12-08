package com.skteam.ititest.ui.quiz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseActivity;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentQuizBinding;
import com.skteam.ititest.restModel.quiz.QuizResponse;
import com.skteam.ititest.restModel.quiz.ResItem;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.setting.dialog.SweetAlertDialog;
import com.skteam.ititest.ui.quiz.adapter.PostionAdapter;
import com.skteam.ititest.ui.quiz.adapter.QuizAdapter;

import java.util.List;

import static com.skteam.ititest.setting.AppConstance.ERROR;
import static com.skteam.ititest.setting.AppConstance.SEC30;
import static com.skteam.ititest.setting.AppConstance.SUCCESS;
import static com.skteam.ititest.setting.AppConstance.WARNING;


public class QuizFragment extends BaseActivity<FragmentQuizBinding, QuizViewModel> implements QuizAdapter.NoQuestionSelected,QuizNav, PostionAdapter.onClickQuistionStatus {
    private QuizViewModel viewModel;
    private FragmentQuizBinding binding;
    private Dialog internetDialog;
    private static QuizFragment instance;
    private static String testId;
    private SweetAlertDialog dialog;
    private PostionAdapter postionAdapter;
    private QuizAdapter quizAdapter;
    private SnapHelper postionHelper, quizHelper;
    private boolean isSubmited =false;

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
    public FragmentQuizBinding getBinding(){
        return binding;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
        if(getIntent().getStringExtra("test_Id")!=null && !getIntent().getStringExtra("test_Id").isEmpty()){
            testId = getIntent().getStringExtra("test_Id");
        }else{
            dialog =showAlertDialog(this, ERROR, "Quiz will be updated soon!", "ITI Test");
            dialog.setConfirmText("Go Back")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        dialog.dismissWithAnimation();
                        finish();
                    });
            //dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            dialog.show();
        }
        quizAdapter = new QuizAdapter(this,this);
        postionAdapter = new PostionAdapter(this, this);
        postionHelper = new LinearSnapHelper();
        postionHelper.attachToRecyclerView(binding.statusList);
        quizHelper = new PagerSnapHelper();
        quizHelper.attachToRecyclerView(binding.questionsList);
        binding.statusList.setAdapter(postionAdapter);
        binding.questionsList.setAdapter(quizAdapter);
        binding.questionsList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
        });
        CountDownTimer timer = new CountDownTimer(SEC30, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                binding.submit.performClick();
            }
        };
        long elapsedMillis = SystemClock.elapsedRealtime() - binding.time.getBase();
        binding.time.setBase(SystemClock.elapsedRealtime() - elapsedMillis);

        binding.submit.setOnClickListener(v -> {
            if(isSubmited){
                showCustomAlert("Test Already Submited!!");
            }else {
                isSubmited=true;
                quizAdapter.UpdateSubmit(true);
                binding.time.stop();
                timer.cancel();
                dialog = showAlertDialog(this, SUCCESS, "Test Submited Successfully", "ITI Test");
                dialog.setConfirmText("OK")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            dialog.dismissWithAnimation();
                        });
                //dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                dialog.show();
            }

        });
        viewModel.GetAllQuiz(testId).observe(this, resItems -> {
            if (resItems != null && resItems.size() > 0) {
                binding.time.start();
                timer.start();
                postionAdapter.UpdateList(resItems);
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
        showCustomAlert("Quiz will be updated soon!");
        dialog =showAlertDialog(this, ERROR, "Quiz will be updated soon!", "ITI Test");
        dialog.setConfirmText("Go Back")
                .setConfirmClickListener(sweetAlertDialog -> {
                    dialog.dismissWithAnimation();
                   finish();
                });
        //dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        dialog.show();
    }

    @Override
    public void onBackPressed() {

        dialog =showAlertDialog(this, WARNING, "Do You want to cancel Your Test", "ITI Test");
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
    }

    @Override
    public void onClickOnStatus(int pos) {
        binding.questionsList.smoothScrollToPosition(pos);
    }

    @Override
    public void NoQuestionSelected() {
        showCustomAlert("Sorry! You Got 0 Points From this Test Series");
        dialog =showAlertDialog(this, ERROR, "No Question Selected", "Times Up!!");
        dialog.setConfirmText("Yes")
                .setConfirmClickListener(sweetAlertDialog -> {

                    dialog.dismissWithAnimation();
                    finish();
                });

        dialog.show();
    }

    @Override
    public void getResult(List<ResItem> resItems) {
        Gson gson = new Gson();
        String json = gson.toJson(resItems);
        startActivityForResult(new Intent(QuizFragment.this,SubmitActivity.class).putExtra("data",json),001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 001:{
                finish();
            }
        }
    }
}