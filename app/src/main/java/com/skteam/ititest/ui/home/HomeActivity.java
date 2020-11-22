
package com.skteam.ititest.ui.home;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseActivity;
import com.skteam.ititest.databinding.ActivityHomeBinding;
import com.skteam.ititest.databinding.NavHeaderMainBinding;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.ui.splash.SplashActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> implements HomeNav {

    private ActivityHomeBinding binding;
    private HomeViewModel viewModel;
    private Dialog internetDialog;
    private Disposable disposable;
    private Context context;
    private NavHeaderMainBinding navigationViewHeaderBinding;


    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public HomeViewModel getViewModel() {
        return viewModel = new HomeViewModel(this, getSharedPre(), this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);
        context=this;

        navigationViewHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_main, binding.navView, false);
        binding.navView.addHeaderView(navigationViewHeaderBinding.getRoot());
        if (savedInstanceState == null) {
            startFragment(HomeFragment.getInstance(), true, HomeFragment.getInstance().toString());
        }
        SetOnClickListenersAll();
        setData();


    }

    private void setData() {
        viewModel.GetAllUserDetails().observe(this, res -> {
            if (res != null && res.size() > 0) {
                navigationViewHeaderBinding.navHeaderTitle.setText(res.get(0).getName());
                navigationViewHeaderBinding.navHeaderSubtitle.setText(res.get(0).getEmail());
            }
        });
        disposable=RxView.clicks(navigationViewHeaderBinding.btnLogout).throttleFirst(1000,TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(unit -> {
                    getAuth().signOut();
                    showLoadingDialog("");
                    if (getSharedPre().isFaceboobkLoggedIn()) {
                        if (AccessToken.getCurrentAccessToken() == null) {
                            getSharedPre().Logout();
                            hideLoadingDialog();
                            startActivity(new Intent(HomeActivity.this, SplashActivity.class));
                            finish();
                            return; // user already logged out
                        }
                        try {
                            GraphRequestAsyncTask graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, graphResponse -> {
                                LoginManager.getInstance().logOut();
                                hideLoadingDialog();
                                getSharedPre().Logout();
                                startActivity(new Intent(HomeActivity.this, SplashActivity.class));
                                finish();
                            }).executeAsync();
                        } catch (Exception e) {

                        }

                    } else if (getSharedPre().isGoogleLoggedIn()) {
                        GoogleSignInOptions  gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getResources().getString(R.string.GOOGLE_SIGNIN_SECRET)).requestEmail()
                                .requestScopes(new Scope("https://www.googleapis.com/auth/user.birthday.read"),
                                        new Scope("https://www.googleapis.com/auth/userinfo.profile"))
                                .build();
                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, gso);
                        googleSignInClient.signOut()
                                .addOnCompleteListener(HomeActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        getSharedPre().Logout();
                                        hideLoadingDialog();
                                        startActivity(new Intent(HomeActivity.this, SplashActivity.class));
                                        finish();
                                    }
                                });
                    }else {
                        hideLoadingDialog();
                        getSharedPre().Logout();
                        startActivity(new Intent(HomeActivity.this, SplashActivity.class));
                        finish();
                    }

                });



    }


    @SuppressLint("RtlHardcoded")
    private void SetOnClickListenersAll() {
        disposable = RxView.clicks(binding.toolbar.drwerAccess).throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        binding.navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.nav_profile:{

                    break;
                }
                case R.id.nav_leaderboard:{
                    break;
                }
                case R.id.nav_share_app:{
                    break;

                }
                case R.id.nav_about_us:{
                    break;
                }
                case R.id.nav_contact_us:{
                    break;
                }
                case R.id.nav_privacy_policy:{
                    break;
                }
            }
            return false;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(this, false);
        }
        if (isConnected) {
            internetDialog.dismiss();
        } else {
            internetDialog.show();
        }
    }


    @Override
    public void setLoading(boolean b) {
        if (b) {
            showLoadingDialog("");
        } else {
            hideLoadingDialog();
        }
    }

    @Override
    public void setMessage(String message) {
        showCustomAlert(message);
    }

}