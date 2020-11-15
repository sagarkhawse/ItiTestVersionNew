/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.login;

import android.app.Activity;
import android.content.Context;

import com.skteam.ititest.baseclasses.BaseViewModel;
import com.skteam.ititest.prefrences.SharedPre;

public class LoginViewModel extends BaseViewModel<LoginNav> {
    public LoginViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }

    public void LoginviaGoogle() {
    }

    public void LoginviaFacebook() {
    }

    public void LoginNow() {
    }
}
