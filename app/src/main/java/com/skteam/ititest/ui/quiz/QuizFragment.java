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
import androidx.recyclerview.widget.SnapHelper;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skteam.ititest.R;
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
import static com.skteam.ititest.setting.AppConstance.WARNING;


public class QuizFragment extends BaseFragment<FragmentQuizBinding, QuizViewModel> implements QuizNav, PostionAdapter.onClickQuistionStatus {
    private QuizViewModel viewModel;
    private FragmentQuizBinding binding;
    private Dialog internetDialog;
    private static QuizFragment instance;
    private static String testId;
    private SweetAlertDialog dialog;
    private PostionAdapter postionAdapter;
    private QuizAdapter quizAdapter;
    private SnapHelper postionHelper, quizHelper;

    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment getInstance(String testIdMain) {
        testId = testIdMain;
        return instance = instance == null ? new QuizFragment() : instance;
    }

    @Override
    public String toString() {
        return QuizFragment.class.getName();
    }

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
        return viewModel = new QuizViewModel(getContext(), getSharedPre(), getBaseActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
        quizAdapter = new QuizAdapter(getContext());
        postionAdapter = new PostionAdapter(getContext(), this);
        postionHelper = new LinearSnapHelper();
        postionHelper.attachToRecyclerView(binding.statusList);
        quizHelper = new PagerSnapHelper();
        quizHelper.attachToRecyclerView(binding.questionsList);
        binding.statusList.setAdapter(postionAdapter);
        binding.questionsList.setAdapter(quizAdapter);
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
        binding.time.start();
        timer.start();

        binding.submit.setOnClickListener(v -> {
            quizAdapter.UpdateSubmit(true);
            binding.time.stop();
            timer.cancel();
        });
        viewModel.GetAllQuiz(testId).observe(getBaseActivity(), resItems -> {
            if (resItems != null && resItems.size() > 0) {
                postionAdapter.UpdateList(resItems);
                quizAdapter.UpdateList(resItems);
            }
        });

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(getActivity(), false);
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
        dialog = getBaseActivity().showAlertDialog(getActivity(), ERROR, "Quiz will be updated soon!", "ITI Test");
        dialog.setConfirmText("Go Back")
                .setConfirmClickListener(sweetAlertDialog -> {
                    dialog.dismissWithAnimation();
                    getBaseActivity().onBackPressed();
                });
        //dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        dialog.show();
    }


    @Override
    public void onClickOnStatus(int pos) {
        binding.questionsList.smoothScrollToPosition(pos);
    }
}