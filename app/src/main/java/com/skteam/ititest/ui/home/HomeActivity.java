
package com.skteam.ititest.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
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
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseActivity;
import com.skteam.ititest.databinding.ActivityHomeBinding;
import com.skteam.ititest.databinding.NavHeaderMainBinding;
import com.skteam.ititest.restModel.signup.Re;
import com.skteam.ititest.setting.AppConstance;
import com.skteam.ititest.setting.CommonUtils;

import java.util.List;
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
        SetOnClickListnersAll();
        setData();


    }

    private void setData() {
        viewModel.GetAllUserDetails().observe(this, res -> {
            if (res != null && res.size() > 0) {
                navigationViewHeaderBinding.navHeaderTitle.setText(res.get(0).getName());
                navigationViewHeaderBinding.navHeaderSubtitle.setText(res.get(0).getEmail());
                if(res.get(0).getProfilePic()!=null) {
                    Uri uri = Uri.parse(res.get(0).getProfilePic());
                    String protocol = uri.getScheme();
                    String server = uri.getAuthority();
                    if(protocol!=null && server!=null){
                        Glide.with(context).load(res.get(0).getProfilePic()).into(navigationViewHeaderBinding.profilePic);
                    }else{
                        Glide.with(context).load(AppConstance.IMG_URL+res.get(0).getProfilePic()).into(navigationViewHeaderBinding.profilePic);
                    }
                }
            }
        });



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
            switch (id) {
                case R.id.nav_home_email: {
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