package com.skteam.ititest.ui.profile;

import android.app.Activity;
import android.content.Context;

import com.skteam.ititest.baseclasses.BaseViewModel;
import com.skteam.ititest.prefrences.SharedPre;

public class ProfileViewmodel extends BaseViewModel<ProfileNav> {
    public ProfileViewmodel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
}
