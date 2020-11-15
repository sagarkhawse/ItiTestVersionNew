/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.signup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.SignUpFragmentBinding;

public class SignUpFragment extends BaseFragment<SignUpFragmentBinding, SignUpViewModel> implements SignUpNav {

    private SignUpViewModel viewModel;
    private SignUpFragmentBinding binding;
    private static SignUpFragment instance;


    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.sign_up_fragment;
    }

    @Override
    public String toString() {
        return SignUpFragment.class.getSimpleName();
    }

    public static SignUpFragment newInstance() {
        instance = instance == null ? new SignUpFragment() : instance;
        return instance;
    }

    @Override
    public SignUpViewModel getViewModel() {
        return viewModel = new SignUpViewModel(getContext(), getSharedPre(), getBaseActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
    }

}