/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.signup;

import com.skteam.ititest.restModel.signup.Re;

public interface SignUpNav {
    void onLoginFail();

    void setLoading(boolean b);

    void SignUpResponse(Re re);
}
