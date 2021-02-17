
package com.skteam.ititest.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentHomeBinding;
import com.skteam.ititest.restModel.home.leaderboard.Re;
import com.skteam.ititest.restModel.home.subjects.ResItem;
import com.skteam.ititest.setting.AppConstance;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.ui.home.adapter.LeaderBoardAdapter;
import com.skteam.ititest.ui.home.adapter.SubjectAdapter;
import com.skteam.ititest.ui.leaderboard.LeaderboardFragment;
import com.skteam.ititest.ui.splash.SplashActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements HomeNav {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private static HomeFragment instance;
    private LeaderBoardAdapter leaderBoardAdapter;
    private SubjectAdapter subjectAdapter;
    private Dialog internetDialog;
    private boolean isApicalled = false;
    private List<ResItem> subjectList = new ArrayList<>();
    private List<Re> leaderboardList = new ArrayList<>();
    private List<com.skteam.ititest.restModel.signup.Re> profileData = new ArrayList<>();
    private SnapHelper bestPlayerHelper = new PagerSnapHelper();
    private SnapHelper subjectHelper = new PagerSnapHelper();

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment getInstance() {
        instance = instance == null ? new HomeFragment() : instance;
        return instance;
    }


    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public HomeViewModel getViewModel() {
        return viewModel = new HomeViewModel(getContext(), getSharedPre(), getActivity());
    }

    @Override
    public String toString() {
        return HomeFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        viewModel.setNavigator(this);

        leaderBoardAdapter = new LeaderBoardAdapter(getContext(), leaderboardList);
        subjectAdapter = new SubjectAdapter(getContext(), subjectList);
        bestPlayerHelper.attachToRecyclerView(binding.rvBestPlayers);
        subjectHelper.attachToRecyclerView(binding.rvSubjects);
        binding.rvSubjects.setAdapter(subjectAdapter);
        binding.rvBestPlayers.setAdapter(leaderBoardAdapter);
        CollectAllDataThroughAPI();


        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipe.setRefreshing(true);
                CollectAllDataThroughAPI();
            }
        });
        binding.tvViewAllPlayers.setOnClickListener(v -> getBaseActivity().startFragment(LeaderboardFragment.newInstance(), true, LeaderboardFragment.newInstance().toString()));
    }

    private void setProfileData(List<com.skteam.ititest.restModel.signup.Re> profileData) {
        isApicalled = true;
        binding.name.setText(profileData.get(0).getName());
        binding.emailAddress.setText(profileData.get(0).getEmail());
        if (profileData.get(0).getProfilePic() != null) {
            Uri uri = Uri.parse(profileData.get(0).getProfilePic());
            String protocol = uri.getScheme();
            String server = uri.getAuthority();
            if (protocol != null && server != null) {
                Glide.with(getContext()).load(profileData.get(0).getProfilePic()).into(binding.userDp);
            } else {
                Glide.with(getContext()).load(AppConstance.IMG_URL + profileData.get(0).getProfilePic()).into(binding.userDp);
            }
        }
    }

    private void CollectAllDataThroughAPI() {

        viewModel.GetAllSubjectNow().observe(getBaseActivity(), res -> {
            binding.swipe.setRefreshing(false);
            if (res != null && res.size() > 0) {
                subjectAdapter.updateList(res);
                subjectList = res;
            }
        });
        viewModel.GetAllLeaderBoardNow().observe(getBaseActivity(), res -> {
            binding.swipe.setRefreshing(false);
            if (res != null && res.size() > 0) {
                leaderBoardAdapter.updateList(res);
                leaderboardList = res;
            }
        });
        viewModel.GetAllUserDetails().observe(getBaseActivity(), res -> {
            binding.swipe.setRefreshing(false);
            if (res != null && res.size() > 0) {
                profileData = res;
                setProfileData(res);
            }
        });
        viewModel.getScore().observe(getBaseActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.swipe.setRefreshing(false);
                if (s != null && !s.isEmpty()) {
                    binding.score.setText(s);
                } else {
                    binding.score.setText("00.00");
                }
            }
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void LogOut() {
        if (getSharedPre().isGoogleLoggedIn()) {
            viewModel.getGoogleClient().signOut().addOnCompleteListener(getBaseActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (viewModel.getmAuth() != null) {
                        viewModel.getmAuth().signOut();
                    }
                    getSharedPre().Logout();
                    startActivity(new Intent(getBaseActivity(), SplashActivity.class));
                    getBaseActivity().finish();
                }
            });
        } else if (getSharedPre().isFaceboobkLoggedIn()) {
            try {
                GraphRequestAsyncTask graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, graphResponse -> {
                    LoginManager.getInstance().logOut();
                    viewModel.getmAuth().signOut();
                    getSharedPre().Logout();
                    startActivity(new Intent(getBaseActivity(), SplashActivity.class));
                    getBaseActivity().finish();
                }).executeAsync();
            } catch (Exception e) {

            }

        } else {
            viewModel.getmAuth().signOut();
            getSharedPre().Logout();
            startActivity(new Intent(getBaseActivity(), SplashActivity.class));
            getBaseActivity().finish();
        }


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(getActivity(), false);
        }
        if (isConnected) {
            internetDialog.dismiss();
            CollectAllDataThroughAPI();
        } else {
            internetDialog.show();
        }
    }
}