package com.skteam.ititest.prefrences;
/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SharedPre {
    private static final String ITI = "iti";
    private static final String EMAIL = "email";
    private static final String MOBILE_NO = "mobile_no";
    private static final String APP_BACKGROUND = "app_in_background";
    private static final String IS_ADD_MOB_SAVED = "isSvedAddMobData";
    private static final String IS_LOGGED_IN = "login";
    private static final String IS_REGISTER = "register";
    private static final String USER_ID = "userId";
    private static SharedPre Instance;
    @NonNull
    Context mContext;

    private SharedPre(Context context) {
        if (Instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class( Mr.professional - Ishant ).");
        }
        mContext = context.getApplicationContext();
    }

    public synchronized static SharedPre getInstance(Context context) {
        if (Instance == null) {
            Instance = new SharedPre(context);
        }
        return Instance;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(ITI, Context.MODE_PRIVATE);
    }

    public boolean isSavedAd() {
        return GetDataBoolean(this.IS_ADD_MOB_SAVED);
    }

    public void setIsSavedAd(boolean value) {
        SetDataBoolean(this.IS_ADD_MOB_SAVED, value);
    }

    public boolean isLoggedIn() {
        return GetDataBoolean(this.IS_LOGGED_IN);
    }

    public void setIsLoggedIn(boolean value) {
        SetDataBoolean(this.IS_LOGGED_IN, value);
    }

    public boolean isRegister() {
        return GetDataBoolean(this.IS_REGISTER);
    }

    public void setIsRegister(boolean value) {
        SetDataBoolean(this.IS_REGISTER, value);
    }

    public void setIsAppBackground(boolean b) {
        SetDataBoolean(this.APP_BACKGROUND, b);
    }

    public boolean IsAppBackgrounded() {
        return GetDataBoolean(this.APP_BACKGROUND);
    }

    public void setUserId(String uid) {
        SetDataString(this.USER_ID, uid);
    }

    public String getUserId() {
        return GetDataString(this.USER_ID);
    }

    public void setUserMobile(String userMobile) {
        SetDataString(this.MOBILE_NO, userMobile);
    }

    public String getUserMobile() {
        return GetDataString(this.MOBILE_NO);
    }

    public void setUserEmail(String email) {
        SetDataString(this.EMAIL, email);
    }

    public String getUserEmail() {
        return GetDataString(this.EMAIL);
    }


//--------------------------------------Boolean Values--------------------------------------------

    //------------------------------------------------------------------------------------------------
    private String GetDataString(String key) {
        String cbValue = null;
        try {
            cbValue = getSharedPreferences(mContext).getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cbValue;
    }


    private String GetDataStringZero(String key) {
        String cbValue = null;
        try {
            cbValue = getSharedPreferences(mContext).getString(key, "0.0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cbValue;
    }

    private void SetDataString(String key, String value) {
        SharedPreferences.Editor edit = getSharedPreferences(mContext).edit();
        edit.putString(key, value);
        edit.commit();

    }
    private int GetDataInt(String key) {
        int cbValue = getSharedPreferences(mContext).getInt(key, 0);
        return cbValue;
    }

    private void SetDataInt(String key, int value) {
        SharedPreferences.Editor edit = getSharedPreferences(mContext).edit();
        edit.putInt(key, value);
        edit.commit();
    }

    private long GetDataLong(String key) {
        long cbValue = getSharedPreferences(mContext).getLong(key, 0);
        return cbValue;
    }

    private void SetDataLong(String key, long value) {
        SharedPreferences sp =getSharedPreferences(mContext);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    private Boolean GetDataBoolean(String key) {
        boolean cbValue = getSharedPreferences(mContext).getBoolean(key, false);
        if (cbValue) {
            return true;
        } else {
            return false;
        }
    }

    private void SetDataBoolean(String key, Boolean value) {
        SharedPreferences.Editor edit = getSharedPreferences(mContext).edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public void Logout() {

        getSharedPreferences(mContext).edit().clear().commit();
        LogoutPrefrences();
    }

    private static void removePreferences(String key, Context cntxt) {
        getSharedPreferences(cntxt).edit().remove(key).commit();
    }

    private void LogoutPrefrences() {
        removePreferences(APP_BACKGROUND, mContext);

    }



}


