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

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;


import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


    public BaseViewModel(Context context, SharedPre sharedPre, Activity activity) {
        this.sharedPre = sharedPre;
        this.context = context;
        this.activity = activity;

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

}
