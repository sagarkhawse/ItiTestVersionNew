package com.skteam.ititest.ui.quiz;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentQuizBinding;
import com.skteam.ititest.setting.CommonUtils;

import java.util.List;


public class QuizFragment extends BaseFragment<FragmentQuizBinding, QuizViewModel> implements QuizNav {
    private QuizViewModel viewModel;
    private FragmentQuizBinding binding;
    private Dialog internetDialog;
    private static QuizFragment instance;
    private static String testId;

    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment getInstance(String testIdMain) {
        testId=testIdMain;
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
        binding=getViewDataBinding();
        viewModel.setNavigator(this);
        viewModel.GetAllQuiz(testId).observe(getBaseActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {

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

    }

    @Override
    public void setMessage(String s) {

    }
}