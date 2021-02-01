/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

package com.skteam.ititest.baseclasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.skteam.ititest.R;
import com.skteam.ititest.application.MyApplication;
import com.skteam.ititest.brodcastReceivers.ConnectionReceiver;
import com.skteam.ititest.database.RoomDatabase;
import com.skteam.ititest.databinding.ActivityHomeBinding;
import com.skteam.ititest.databinding.CustomToastBinding;
import com.skteam.ititest.prefrences.SharedPre;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.setting.dialog.SweetAlertDialog;
import com.skteam.ititest.ui.home.HomeFragment;
import com.skteam.ititest.ui.welcome.WelcomeFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.skteam.ititest.setting.AppConstance.BASIC;
import static com.skteam.ititest.setting.AppConstance.BASIC_DISCRIPTION;
import static com.skteam.ititest.setting.AppConstance.ERROR;
import static com.skteam.ititest.setting.AppConstance.PROGRESS;
import static com.skteam.ititest.setting.AppConstance.SUCCESS;
import static com.skteam.ititest.setting.AppConstance.WARNING;


public abstract class BaseActivity<B extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {
    private static ArrayList<Class> runningActivities = new ArrayList<>();
    private B mViewDataBinding;
    private V mViewModel;
    private ProgressDialog progressDialog;
    private SharedPre sharedPre;
    private RoomDatabase database;
    private boolean addToBackStack = false;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment fragment;
    private ConnectionReceiver.ConnectionReceiverListener connectionReceiverListener;
    private Toast toast;
    private Vibrator vibe;
    private RxPermissions rxPermissions;
    private boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth auth;
    private SweetAlertDialog dialog;
//replace yourActivity.this with your own activity or if you declared a context you can write context.getSystemService(Context.VIBRATOR_SERVICE);

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        addThisActivityToRunningActivities(this.getClass());
        super.onCreate(savedInstanceState);
        connectionReceiverListener = this;
        ((MyApplication) getApplicationContext()).setConnectionListener(connectionReceiverListener);
        performDataBinding();
        manager = getSupportFragmentManager();
        toast = new Toast(this);
    }

    public FirebaseAuth getAuth() {
        if (auth != null) {
            return auth;
        } else {
           return auth = FirebaseAuth.getInstance();
        }
    }

    public RoomDatabase getDatabase() {
        if (database == null) {
            database = RoomDatabase.getInstance(this);
        }
        return database;
    }

    public Context GetApplicationContext() {
        return getApplicationContext();
    }

    public RxPermissions getRxPermissions() {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        return rxPermissions;
    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    public B getViewDataBinding() {
        return mViewDataBinding;
    }


    public Vibrator getVibe() {
        if (vibe == null) {
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }
        return vibe;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


    public SharedPre getSharedPre() {
        if (sharedPre != null) {
            return sharedPre;
        } else {
            sharedPre = SharedPre.getInstance(this);
            return sharedPre;
        }
    }

    public void showKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    public void addThisActivityToRunningActivities(Class cls) {
        if (!runningActivities.contains(cls))
            runningActivities.add(cls);
    }

    public void removeThisActivityFromRunningActivities(Class cls) {
        if (runningActivities.contains(cls))
            runningActivities.remove(cls);
    }

    @Override
    public void finish() {
        super.finish();
        removeThisActivityFromRunningActivities(this.getClass());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        removeThisActivityFromRunningActivities(this.getClass());
    }

    public void showCustomAlert(String msg) {
        CustomToastBinding toastBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.custom_toast, null, false);
        toastBinding.toastText.setText(msg);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setView(toastBinding.getRoot());
        toast.show();
    }

    public void showLoadingDialog(String s) {
        try {
            if (progressDialog == null)
                progressDialog = CommonUtils.showLoadingDialog(this, "Please wait");
            else
                progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideLoadingDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startFragment(Fragment fragment) {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (!isFinishing() && !isDestroyed()) {
            transaction.commit();
        }
    }

    public void startFragment(Fragment fragment, boolean addToBackStack, String backStackTag) {
        this.addToBackStack = addToBackStack;
        boolean fragmentPopped = manager.popBackStackImmediate(backStackTag, 0);
        if (!fragmentPopped) {
            transaction = manager.beginTransaction();
            if (addToBackStack) {
                transaction.addToBackStack(backStackTag);
            } else {
                transaction.addToBackStack(null);
            }
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }
    }

    public void startFragment(Fragment fragment, boolean addToBackStack, String backStackTag, boolean wantAnimation) {
        this.addToBackStack = addToBackStack;
        boolean fragmentPopped = manager.popBackStackImmediate(backStackTag, 0);
        if (!fragmentPopped) {
            transaction = manager.beginTransaction();
            if (wantAnimation) {
                transaction.setCustomAnimations(R.anim.slide_up, 0, 0, 0);
            }
            if (addToBackStack) {
                transaction.addToBackStack(backStackTag);
            } else {
                transaction.addToBackStack(null);
            }
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }
    }


    @Override
    public void onBackPressed() {
        fragment = getCurrentFragment();
        for (int entry = 0; entry < manager.getBackStackEntryCount(); entry++) {
            Log.e("Ishant", "Found fragment: " + manager.getBackStackEntryAt(entry).getId());
        }
        if (addToBackStack) {
            if (fragment instanceof WelcomeFragment) {
                addToBackStack = false;
                finish();
            }
            if (fragment instanceof HomeFragment) {
                if (doubleBackToExitPressedOnce) {
                    toast.cancel();
                    finish();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                // showAlertDialog();
                showCustomAlert(" Please Press BACK again to exit");
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            } else {
                if (manager != null && manager.getBackStackEntryCount() > 0) {
                    manager.popBackStackImmediate();
                }
            }
        } else {
            super.onBackPressed();
        }


    }
    public SweetAlertDialog showAlertDialog(Context context, int type, String msg, String title) {
        switch (type) {
            case PROGRESS: {
                dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                dialog.setTitleText(title);
                dialog.setCancelable(false);
                return dialog;
            }
            case SUCCESS:{
                dialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitleText(title);
                dialog.setContentText(msg);
                dialog.setCancelable(false);
                return dialog;
            }
            case BASIC: {
                dialog = new SweetAlertDialog(context);
                dialog.setTitleText(title);
                dialog.setCancelable(false);
                return dialog;
            }
            case BASIC_DISCRIPTION: {
                dialog = new SweetAlertDialog(context);
                dialog.setTitleText(title);
                dialog.setContentText(msg);
                dialog.setCancelable(false);
                return dialog;
            }
            case ERROR: {
                dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                dialog.setTitleText(title);
                dialog.setContentText(msg);
                dialog.setCancelable(false);
                return dialog;
            }
            case WARNING: {
                dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText(title);
                dialog.setContentText(msg);
                return dialog;
            }
            default:{
                dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                dialog.setTitleText(title);
                dialog.setCancelable(false);
                return dialog;
            }
        }

    }
    public Fragment getCurrentFragment() {
        fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        return fragment;
    }

}
