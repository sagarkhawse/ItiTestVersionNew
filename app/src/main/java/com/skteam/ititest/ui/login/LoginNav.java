/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.login;

public interface LoginNav {
    void onLoginFail(String message);

    void setLoading(boolean b);

    void StartHomeNow();
}
