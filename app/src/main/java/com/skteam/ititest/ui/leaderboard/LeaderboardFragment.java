
package com.skteam.ititest.ui.leaderboard;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentLeaderboardBinding;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.ui.home.HomeActivity;
import com.skteam.ititest.ui.splash.SplashFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

public class LeaderboardFragment extends BaseFragment<FragmentLeaderboardBinding, LeaderBoardViewmodel> implements LeaderNav {
    private FragmentLeaderboardBinding binding;
    private LeaderBoardViewmodel viewmodel;
    private Dialog internetDialog;
    private static LeaderboardFragment instance;
    private Disposable disposable;
    private boolean todaySelected = true, monthSelected = false, alltimeSelected = false;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    @Override
    public String toString() {
        return LeaderboardFragment.class.getSimpleName();
    }

    public static LeaderboardFragment newInstance() {
        instance = instance == null ? new LeaderboardFragment() : instance;
        return instance;
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_leaderboard;
    }

    @Override
    public LeaderBoardViewmodel getViewModel() {
        return viewmodel = new LeaderBoardViewmodel(getContext(), getSharedPre(), getBaseActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewmodel.setNavigator(this);
        ((HomeActivity)getBaseActivity()).getAppBar().toolbarMain.setVisibility(View.GONE);
        TabClickListeners();
    }

    private void TabClickListeners() {
        disposable = RxView.clicks(binding.todayButton).observeOn(AndroidSchedulers.mainThread()).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                if (!todaySelected) {
                    binding.monthButton.setBackgroundResource(R.drawable.bg_round1);
                    binding.allTimeButton.setBackgroundResource(R.drawable.bg_round1);
                    binding.todayButton.setBackgroundResource(R.drawable.bg_white_round);
                    binding.todayButton.setTextColor(getContext().getResources().getColor(R.color.black));
                    binding.monthButton.setTextColor(getContext().getResources().getColor(R.color.color_white));
                    binding.allTimeButton.setTextColor(getContext().getResources().getColor(R.color.color_white));
                    monthSelected = false;
                    alltimeSelected = false;
                    todaySelected = true;
                }
            }
        });
        disposable = RxView.clicks(binding.monthButton).observeOn(AndroidSchedulers.mainThread()).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                if (!monthSelected) {
                    binding.monthButton.setBackgroundResource(R.drawable.bg_white_round);
                    binding.allTimeButton.setBackgroundResource(R.drawable.bg_round1);
                    binding.todayButton.setBackgroundResource(R.drawable.bg_round1);
                    binding.monthButton.setTextColor(getContext().getResources().getColor(R.color.black));
                    binding.todayButton.setTextColor(getContext().getResources().getColor(R.color.color_white));
                    binding.allTimeButton.setTextColor(getContext().getResources().getColor(R.color.color_white));
                    monthSelected = true;
                    alltimeSelected = false;
                    todaySelected = false;
                }
            }
        });
        disposable = RxView.clicks(binding.allTimeButton).observeOn(AndroidSchedulers.mainThread()).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                if (!alltimeSelected) {
                    binding.monthButton.setBackgroundResource(R.drawable.bg_round1);
                    binding.allTimeButton.setBackgroundResource(R.drawable.bg_white_round);
                    binding.todayButton.setBackgroundResource(R.drawable.bg_round1);
                    binding.allTimeButton.setTextColor(getContext().getResources().getColor(R.color.black));
                    binding.todayButton.setTextColor(getContext().getResources().getColor(R.color.color_white));
                    binding.monthButton.setTextColor(getContext().getResources().getColor(R.color.color_white));
                    monthSelected = false;
                    alltimeSelected = true;
                    todaySelected = false;
                }
            }
        });
        disposable = RxView.clicks(binding.ivBack).observeOn(AndroidSchedulers.mainThread()).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                getBaseActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(getBaseActivity(), false);
        }
        if (isConnected) {
            internetDialog.dismiss();
        } else {
            internetDialog.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((HomeActivity)getBaseActivity()).getAppBar().toolbarMain.setVisibility(View.VISIBLE);
    }
}