
package com.skteam.ititest.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseActivity;
import com.skteam.ititest.databinding.ActivityHomeBinding;
import com.skteam.ititest.databinding.NavHeaderMainBinding;
import com.skteam.ititest.setting.CommonUtils;

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
        navigationViewHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.nav_header_main,binding.navView,false);
        binding.navView.addHeaderView(navigationViewHeaderBinding.getRoot());
        if (savedInstanceState == null) {
            startFragment(HomeFragment.getInstance(),true,HomeFragment.getInstance().toString());
        }
        SetOnClickListnersAll();
        setData();


    }

    private void setData() {
        navigationViewHeaderBinding.navHeaderTitle.setText(getSharedPre().getName());
        navigationViewHeaderBinding.navHeaderSubtitle.setText(getSharedPre().getUserEmail());
        if(getSharedPre().isGoogleLoggedIn()|| getSharedPre().isFaceboobkLoggedIn()){
            Glide.with(this).load(getSharedPre().getClientProfile()).into( navigationViewHeaderBinding.profilePic);
        }else{

        }

    }


    private void SetOnClickListnersAll() {
        disposable = RxView.clicks(binding.toolbar.drwerAccess).throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(unit -> {
            if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        binding.navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id){
                case R.id.nav_home_email:{
                    showCustomAlert("Click on Email button in Navigation View");
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