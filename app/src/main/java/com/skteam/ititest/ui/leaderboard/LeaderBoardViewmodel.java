package com.skteam.ititest.ui.leaderboard;

import android.app.Activity;
import android.content.Context;

import com.skteam.ititest.baseclasses.BaseViewModel;
import com.skteam.ititest.prefrences.SharedPre;

public class LeaderBoardViewmodel extends BaseViewModel<LeaderNav> {
    public LeaderBoardViewmodel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
    }
}
