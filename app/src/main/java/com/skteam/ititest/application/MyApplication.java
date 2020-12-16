/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

package com.skteam.ititest.application;

import android.app.Application;
import android.content.IntentFilter;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ConnectionQuality;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.ConnectionQualityChangeListener;
import com.skteam.ititest.brodcastReceivers.ConnectionReceiver;
import com.skteam.ititest.prefrences.SharedPre;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;


public class MyApplication extends Application implements LifeCycleDelegate {

    private static MyApplication mInstance;
    private ConnectionReceiver connectionReceiver;
    private IntentFilter networkintentFilter = new IntentFilter();
    private CookieManager cookieManager;

    @Override
    public void onCreate() {
        super.onCreate();
        connectionReceiver = new ConnectionReceiver();
        cookieManager=new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HttpLoggingInterceptor logInter = new HttpLoggingInterceptor();
        logInter.setLevel(HttpLoggingInterceptor.Level.BODY);
        networkintentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                /*.cookieJar(new JavaNetCookieJar(cookieManager))
                .addInterceptor(logInter)*/
                .readTimeout(60, TimeUnit.SECONDS)
                . writeTimeout(60, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(this,okHttpClient);
        AppLifecycleHandler lifeCycleHandler = new AppLifecycleHandler(this);
        registerLifecycleHandler(lifeCycleHandler);
        registerReceiver(connectionReceiver, networkintentFilter);
        mInstance = this;
        VMExceptionHandler.install();
    }


    public void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }


    private void registerLifecycleHandler(AppLifecycleHandler lifeCycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
    }

    @Override
    public void onAppBackgrounded() {
        SharedPre.getInstance(this).setIsAppBackground(true);
//        unregisterReceiver(connectionReceiver);
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(connectionReceiver);

    }

    @Override
    public void onAppForegrounded() {
        SharedPre.getInstance(this).setIsAppBackground(false);
    }
}
