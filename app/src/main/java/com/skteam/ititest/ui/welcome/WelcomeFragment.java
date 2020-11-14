/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.welcome;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.SplashActivityBinding;
import com.skteam.ititest.databinding.WelcomeFragmentBinding;
import com.skteam.ititest.ui.login.Login;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

public class WelcomeFragment extends BaseFragment<WelcomeFragmentBinding,WelcomeViewModel> implements WelcomeNav {

    private WelcomeViewModel viewModel;
    private WelcomeFragmentBinding binding;
    private static WelcomeFragment instance;
    private Disposable disposable;

    public static WelcomeFragment newInstance() {
        instance = instance == null ? new WelcomeFragment() : instance;
        return instance;
    }

    @Override
    public String toString() {
        return WelcomeFragment.class.getSimpleName();
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.welcome_fragment;
    }

    @Override
    public WelcomeViewModel getViewModel() {
        return viewModel=new WelcomeViewModel(getContext(),getSharedPre(),getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.setNavigator(this);
        binding=getViewDataBinding();
        disposable= RxView.clicks(binding.loginBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                showCustomAlert("Login clicked Successfully");
                getBaseActivity().startFragment(Login.newInstance(),true,Login.newInstance().toString());
            }
        });
        disposable= RxView.clicks(binding.createBtn).throttleFirst(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                showCustomAlert("Create Account clicked Successfully");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SplashActivityBinding)getBaseActivity().getViewDataBinding()).background.setImageResource(R.color.colorSpeciallight);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
        }
    }
}