package com.skteam.ititest.setting;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.skteam.ititest.R;


public class Animations {



    public static void fade_in(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.startAnimation(animation);
    }

    public static void slide_Right(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right);
        view.startAnimation(animation);
    }
}
