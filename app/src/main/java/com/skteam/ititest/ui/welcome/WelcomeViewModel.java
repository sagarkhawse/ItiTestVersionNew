/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.welcome;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.skteam.ititest.baseclasses.BaseViewModel;
import com.skteam.ititest.prefrences.SharedPre;

public class WelcomeViewModel extends BaseViewModel<WelcomeNav> {

    public WelcomeViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
}