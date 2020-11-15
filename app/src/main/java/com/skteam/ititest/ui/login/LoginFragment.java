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

import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentLoginBinding;
import com.skteam.ititest.ui.signup.SignUpFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginViewModel> implements LoginNav {
    private static LoginFragment instance;
    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    private Disposable disposable;


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
        return viewModel = new LoginViewModel(getContext(), getSharedPre(), getActivity());
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
        binding = getViewDataBinding();
        SetClickListeners();

    }

    private void SetClickListeners() {
        disposable = RxView.clicks(binding.loginBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVib().vibrate(100);
            viewModel.LoginNow();
        });
        disposable = RxView.clicks(binding.createBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVib().vibrate(100);
            getBaseActivity().startFragment(SignUpFragment.newInstance(), true, SignUpFragment.newInstance().toString());
        });
        disposable = RxView.clicks(binding.otherSigninOption.googleBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVib().vibrate(100);
            viewModel.LoginviaGoogle();
        });
        disposable = RxView.clicks(binding.otherSigninOption.faceBookBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            getVib().vibrate(100);
            viewModel.LoginviaFacebook();
        });
    }
}