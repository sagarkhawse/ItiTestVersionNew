/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

package com.skteam.ititest.application;

import android.app.Application;
import android.content.IntentFilter;
import com.skteam.ititest.brodcastReceivers.ConnectionReceiver;
import com.skteam.ititest.prefrences.SharedPre;


public class MyApplication extends Application implements LifeCycleDelegate {

    private static MyApplication mInstance;
    private ConnectionReceiver connectionReceiver;
    private IntentFilter networkintentFilter = new IntentFilter();

    @Override
    public void onCreate() {
        super.onCreate();
        connectionReceiver = new ConnectionReceiver();
        networkintentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
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
