/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.skteam.ititest.BuildConfig;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseViewModel;
import com.skteam.ititest.prefrences.SharedPre;
import com.skteam.ititest.setting.AppConstance;

import org.json.JSONObject;

import static com.skteam.ititest.setting.CommonUtils.getFacebookData;

public class LoginViewModel extends BaseViewModel<LoginNav> {
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private String Profile="";
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions gso;
    public LoginViewModel(Context context, SharedPre sharedPre, Activity activity) {
        super(context, sharedPre, activity);
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activity.getResources().getString(R.string.GOOGLE_SIGNIN_SECRET)).requestEmail().build();
        getGoogleClient();
    }
    public GoogleSignInClient getGoogleClient() {
        if (googleSignInClient != null) {
            return googleSignInClient;
        } else {
            return googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        }

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
            getSharedPre().setIsGoogleLoggedIn(true);
            getSharedPre().setIsFaceboobkLoggedIn(false);
            getSharedPre().setIsLoggedIn(true);
            getSharedPre().setIsRegister(true);
            getSharedPre().setUserEmail(email);
            getSharedPre().setName(name);
            getSharedPre().setClientId(gSocialId);
            getSharedPre().setProfileFacebook("");
            Profile=profilePic;
            getSharedPre().setGoogleProfile(profilePic);
            firebaseAuthWithClient(account.getIdToken(),AppConstance.LOGIN_TYPE_GOOGLE);
        } catch (ApiException e) {
            Log.e("googleStatus", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    public void LoginviaFacebook() {
        registerFBCallBack();
    }

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
                                    getSharedPre().setIsGoogleLoggedIn(false);
                                    getSharedPre().setIsFaceboobkLoggedIn(true);
                                    getSharedPre().setIsLoggedIn(false);
                                    getSharedPre().setIsRegister(true);
                                    getSharedPre().setUserEmail(email);
                                    getSharedPre().setProfileFacebook(profilePic);
                                    Profile=profilePic;
                                    getSharedPre().setGoogleProfile("");
                                    getSharedPre().setName(name);
                                    getSharedPre().setClientId(facebookId);
                                    firebaseAuthWithClient(accessToken,AppConstance.LOGIN_TYPE_FB);

                                } else{
                                    getNavigator().onLoginFail("User not found");
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
                        getNavigator().onLoginFail("User not found");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e("FBstatus", exception.getMessage());
                        getNavigator().onLoginFail("User not found");
                    }
                });
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    try {
                        getNavigator().onLoginFail("User not found");
                    } catch (NullPointerException ex) {

                    }
                }
            }
        };
        accessTokenTracker.startTracking();

    }
    public void LoginViaEmail(String email,String pass){
        getmAuth().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = getmAuth().getCurrentUser();
                            getSharedPre().setUserId(user.getUid());
                            getSharedPre().setUserEmail(user.getEmail());
                            getNavigator().setLoading(false);
                            getSharedPre().setIsLoggedIn(true);
                            getSharedPre().setIsRegister(true);
                        } else {
                            getNavigator().setLoading(false);
                        }
                    }

                });
    }
    //firebase login via client
    private void firebaseAuthWithClient(String idToken,String type) {
        AuthCredential credential=null;
        String typeFinal =null;
        switch(type){
            case AppConstance.LOGIN_TYPE_GOOGLE:{
                credential = GoogleAuthProvider.getCredential(idToken, null);
                Profile=getSharedPre().getGoogleProfile();
                typeFinal=AppConstance.LOGIN_TYPE_GOOGLE;
                break;
            }
            case AppConstance.LOGIN_TYPE_FB:{
                credential = FacebookAuthProvider.getCredential(idToken);
                Profile=getSharedPre().getFacebookProfile();
                typeFinal=AppConstance.LOGIN_TYPE_FB;
                break;
            }
            default:{
                credential=null;
                Profile="";
            }
        }
        if(credential!=null){
            getNavigator().setLoading(true);
            String finalTypeFinal = typeFinal;
            getmAuth().signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = getmAuth().getCurrentUser();
                                getSharedPre().setUserId(user.getUid());
                                getNavigator().StartHomeNow();
                            } else {
                                // If sign in fails, display a message to the user.
                                getNavigator().onLoginFail("Authentication Failed");
                            }

                            // ...
                        }
                    });
        }else{
            getNavigator().onLoginFail("Client Error");
        }

    }
    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void signOut() {
        getmAuth().signOut();
    }
}
