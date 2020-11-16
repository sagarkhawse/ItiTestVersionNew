/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.skteam.ititest.BuildConfig;
import com.skteam.ititest.baseclasses.BaseViewModel;
import com.skteam.ititest.prefrences.SharedPre;
import com.skteam.ititest.restModel.signup.ResponseSignUp;
import com.skteam.ititest.setting.AppConstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import static com.skteam.ititest.setting.CommonUtils.getFacebookData;

public class SignUpViewModel extends BaseViewModel<SignUpNav> {
    private SignUpRepository signUpRepository;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    public SignUpViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        getGoogleClient();
        signUpRepository=new SignUpRepository(context);
    }

    public void SignupNow(String name,String Email,String password) {
       SignuViaClient(name,Email , "",getSharedPre().getUserId() , AppConstance.LOGIN_TYPE_EMAIL,BuildConfig.VERSION_NAME);
    }
//google
    public void SignUpViaGoogle(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);
            String email = account.getEmail();
            String name = account.getDisplayName();
            String gSocialId = account.getId();
            String profilePic = "";
            if (account.getPhotoUrl() != null)
                profilePic = account.getPhotoUrl().toString();
            Log.d("googleStatus", "" + email + " " + name + " " + " " + profilePic);
            String first = "", last = "";
            if (!TextUtils.isEmpty(name) && name.split("\\w+").length > 1) {
                first = name.substring(0, name.lastIndexOf(' '));
                last = name.substring(name.lastIndexOf(" ") + 1);
            } else
                first = name;
            SignuViaClient(name,email,profilePic,getSharedPre().getUserId(),AppConstance.LOGIN_TYPE_GOOGLE, BuildConfig.VERSION_NAME);

        } catch (ApiException e) {
            Log.e("googleStatus", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void SignuViaClient(String name, String email, String profilePic,String userId,String clientType,String deviceVersion ) {
        AndroidNetworking.post(AppConstance.API_BASE_URL + AppConstance.SIGN_UP)
                .addBodyParameter("user_id", getSharedPre().getUserId())
                .addBodyParameter("name", name)
                .addBodyParameter("email", email)
                .addBodyParameter("app_version", deviceVersion)
                .addBodyParameter("verified", "1")
                .addBodyParameter("signup_type", clientType)
                .addBodyParameter("profile_pic", profilePic)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(ResponseSignUp.class, new ParsedRequestListener<ResponseSignUp>() {
                    @Override
                    public void onResponse(ResponseSignUp response) {
                        getNavigator().setLoading(false);
                        try {
                            if (response != null) {
                                if (response.getCode().equals("200")) {
                                   showCustomAlert("SignUp Successfully");
                                    getNavigator().SignUpResponse(response.getRes().get(0));
                                } else {
                                    showCustomAlert("Please try again later!");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            getNavigator().setLoading(false);

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        getNavigator().setLoading(false);
                        showCustomAlert("Please try again later!");
                    }
                });
    }
    //facebook
    private void registerFBCallBack() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String accessToken = loginResult.getAccessToken().getToken();
                        Log.i("FBstatus accessToken", accessToken);
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivity", response.toString());
                                // Get facebook data from login
                                Bundle bFacebookData = getFacebookData(object);
                                if (bFacebookData != null) {
                                    Log.d("FBstatus", "Welcome" + " " + bFacebookData.getString("first_name"));
                                    String name=bFacebookData.getString("first_name","")+bFacebookData.getString("last_name","");
                                    String email=bFacebookData.getString("email","");
                                    String profilePic=bFacebookData.getString("profile_pic","");
                                    String facebookId=bFacebookData.getString("idFacebook");
                                    SignuViaClient(name,email , profilePic,getSharedPre().getUserId() , AppConstance.LOGIN_TYPE_FB,BuildConfig.VERSION_NAME);
                                } else{
                                    getNavigator().onLoginFail();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("FBstatus", "canceled");
                        getNavigator().onLoginFail();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e("FBstatus", exception.getMessage());
                        getNavigator().onLoginFail();
                    }
                });
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    try {
                        getNavigator().onLoginFail();
                    } catch (NullPointerException ex) {

                    }
                }
            }
        };
        accessTokenTracker.startTracking();

    }



    public void SignUpViaFacebook() {
        registerFBCallBack();
    }
    public GoogleSignInClient getGoogleClient() {
       if(googleSignInClient!=null){
           return googleSignInClient;
       }else{
           return  googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
       }

    }
}