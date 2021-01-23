package com.skteam.ititest.ui.quiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseActivity;
import com.skteam.ititest.databinding.FragmentSubmitBinding;
import com.skteam.ititest.restModel.quiz.ResItem;
import com.skteam.ititest.ui.quiz.adapter.QuizAdapter;
import com.skteam.ititest.ui.quiz.adapter.ViewSolutionAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SubmitActivity extends BaseActivity<FragmentSubmitBinding, QuizViewModel>  {
    private static SubmitActivity instance;
    private FragmentSubmitBinding binding;
    private QuizViewModel quizViewModel;
    private ViewSolutionAdapter adapter;
    private static List<ResItem> finalAnswerSheet=new ArrayList<>();


    @NonNull
    @Override
    public String toString() {
        return SubmitActivity.class.getName();
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_submit;
    }

    @Override
    public QuizViewModel getViewModel() {
        return quizViewModel = new QuizViewModel(this, getSharedPre(), this);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=getViewDataBinding();
        Gson gson = new Gson();
        String json = getIntent().getStringExtra("data");
        if (json.isEmpty()) {
            Toast.makeText(this, "There is something error", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Type type = new TypeToken<List<ResItem>>() {
            }.getType();
            List<ResItem> restItems = gson.fromJson(json, type);
            adapter = new ViewSolutionAdapter(this,restItems );
            binding.submitListRecycler.setAdapter(adapter);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
}