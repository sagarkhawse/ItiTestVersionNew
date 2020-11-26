
package com.skteam.ititest.ui.leaderboard;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentLeaderboardBinding;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.ui.splash.SplashFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends BaseFragment<FragmentLeaderboardBinding, LeaderBoardViewmodel> implements LeaderNav {
    private FragmentLeaderboardBinding binding;
    private LeaderBoardViewmodel viewmodel;
    private Dialog internetDialog;
    private static LeaderboardFragment instance;

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
}