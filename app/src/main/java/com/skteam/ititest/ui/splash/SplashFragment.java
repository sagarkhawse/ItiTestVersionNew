/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

package com.skteam.ititest.ui.splash;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentSplashBinding;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.ui.welcome.WelcomeFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashFragment extends BaseFragment<FragmentSplashBinding,SplashViewModel> implements SplashNav {
    private static SplashFragment instance;
    private static final int SPLASH_SCREEN_TIME_OUT=3;
    private FragmentSplashBinding binding;
    private Disposable disposable;
    private SplashViewModel viewModel;
    public SplashFragment() {
        // Required empty public constructor
    }

    public static SplashFragment newInstance() {
        instance = instance == null ? new SplashFragment() : instance;
        return instance;
    }
    @Override
    public String toString() {
        return SplashFragment.class.getSimpleName();
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    public SplashViewModel getViewModel() {
        return viewModel=new SplashViewModel(getContext(),getSharedPre(),getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
        binding.companyName.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
       String tokenFinal=getSharedPre().getFirebaseDeviceToken() ;
        if (tokenFinal==null || tokenFinal.isEmpty()) {
            try {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w("Firebase", "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            getSharedPre().setFirebaseToken(token);
                            disposable = Observable.timer(SPLASH_SCREEN_TIME_OUT, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(aLong -> StartIntent());
                        });
            } catch (Exception e) {
            }

        } else {
            disposable = Observable.timer(SPLASH_SCREEN_TIME_OUT, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> StartIntent());
        }
    }
    void StartIntent(){
        showCustomAlert("Thank You for Choosing us");
        getBaseActivity().startFragment(WelcomeFragment.newInstance(),true,WelcomeFragment.newInstance().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
        }
    }
}

