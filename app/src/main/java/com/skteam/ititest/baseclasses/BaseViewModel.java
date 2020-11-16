/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */
package com.skteam.ititest.baseclasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;


import com.skteam.ititest.R;
import com.skteam.ititest.databinding.CustomToastBinding;
import com.skteam.ititest.prefrences.SharedPre;

import java.lang.ref.WeakReference;

public abstract class BaseViewModel<N> extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private WeakReference<N> mNavigator;
    private SharedPre sharedPre;
    private Activity activity;
    private Toast toast;

    public BaseViewModel(Context context,SharedPre sharedPre,Activity activity)
    {
        this.sharedPre=sharedPre;
        this.context = context;
        this.activity=activity;
    }

    public Activity getActivity() {
        return activity;
    }


    public SharedPre getSharedPre() {
        return sharedPre;
    }

    public N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N Navigator) {
        this.mNavigator = new WeakReference<>(Navigator);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void showCustomAlert(String msg) {
        CustomToastBinding toastBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.custom_toast, null, false);
        toastBinding.toastText.setText(msg);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setView(toastBinding.getRoot());
        toast.show();
    }
}
