/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentLoginBinding;

public class LoginFragment extends BaseFragment<FragmentLoginBinding,LoginViewModel> implements LoginNav{
private static LoginFragment instance;
private FragmentLoginBinding binding;
private LoginViewModel viewModel;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public String toString() {
        return LoginFragment.class.getSimpleName();
    }

    public static LoginFragment newInstance() {
        instance = instance == null ? new LoginFragment() : instance;
        return instance;
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        return viewModel=new LoginViewModel(getContext(),getSharedPre(),getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.setNavigator(this);
        binding=getViewDataBinding();
        viewModel.setNavigator(this);
//        ((SplashActivityBinding)getBaseActivity().getViewDataBinding()).background.setImageResource(R.color.color_white_light);
    }
}