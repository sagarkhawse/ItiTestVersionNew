
package com.skteam.ititest.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.skteam.ititest.BR;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentHomeBinding;
import com.skteam.ititest.restModel.home.subjects.Re;
import com.skteam.ititest.ui.home.adapter.LeaderBoardAdapter;
import com.skteam.ititest.ui.home.adapter.SubjectAdapter;
import com.skteam.ititest.ui.signup.SignUpFragment;

import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding,HomeViewModel>implements HomeNav {

private FragmentHomeBinding binding;
private HomeViewModel viewModel;
private static HomeFragment instance;
private LeaderBoardAdapter leaderBoardAdapter;
private SubjectAdapter subjectAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment getInstance(){
        instance = instance == null ? new HomeFragment() : instance;
        return instance;
    }


    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public HomeViewModel getViewModel() {
        return viewModel = new HomeViewModel(getContext(), getSharedPre(), getActivity());
    }
    @Override
    public String toString() {
        return HomeFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=getViewDataBinding();
        viewModel.setNavigator(this);
        binding.name.setText(getSharedPre().getName());
        binding.emailAddress.setText(getSharedPre().getUserEmail());
        leaderBoardAdapter=new LeaderBoardAdapter(getContext());
        subjectAdapter=new SubjectAdapter(getContext());
        binding.rvSubjects.setAdapter(subjectAdapter);
        binding.rvBestPlayers.setAdapter(leaderBoardAdapter);
        if(getSharedPre().isGoogleLoggedIn()|| getSharedPre().isFaceboobkLoggedIn()){
            Glide.with(this).load(getSharedPre().getClientProfile()).into( binding.userDp);
        }else{

        }
        viewModel.GetAllSubjectNow().observe(getBaseActivity(), res -> {
            if(res!=null && res.size()>0){
                subjectAdapter.updateList(res);
            }
        });
        viewModel.GetAllLeaderBoardNow().observe(getBaseActivity(), res -> {
            if(res!=null && res.size()>0){
                leaderBoardAdapter.updateList(res);
            }
        });


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
    public void setMessage(String message) {
        showCustomAlert(message);
    }
}