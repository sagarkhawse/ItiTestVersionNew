
package com.skteam.ititest.ui.home;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentHomeBinding;
import com.skteam.ititest.restModel.signup.Re;
import com.skteam.ititest.setting.AppConstance;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.ui.home.adapter.LeaderBoardAdapter;
import com.skteam.ititest.ui.home.adapter.SubjectAdapter;

import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements HomeNav {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private static HomeFragment instance;
    private LeaderBoardAdapter leaderBoardAdapter;
    private SubjectAdapter subjectAdapter;
    private Dialog internetDialog;
    private SnapHelper bestPlayerHelper=new PagerSnapHelper();
    private SnapHelper subjectHelper=new PagerSnapHelper();

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
        leaderBoardAdapter = new LeaderBoardAdapter(getContext());
        subjectAdapter = new SubjectAdapter(getContext());
        bestPlayerHelper.attachToRecyclerView(  binding.rvBestPlayers);
        subjectHelper.attachToRecyclerView( binding.rvSubjects);
        binding.rvSubjects.setAdapter(subjectAdapter);
        binding.rvBestPlayers.setAdapter(leaderBoardAdapter);
        CollectAllDataThroughAPI();
    }

    private void CollectAllDataThroughAPI() {
        viewModel.GetAllSubjectNow().observe(getBaseActivity(), res -> {
            if (res != null && res.size() > 0) {
                subjectAdapter.updateList(res);
            }
        });
        viewModel.GetAllLeaderBoardNow().observe(getBaseActivity(), res -> {
            if (res != null && res.size() > 0) {
                leaderBoardAdapter.updateList(res);
            }
        });
        viewModel.GetAllUserDetails().observe(getBaseActivity(), res -> {
            if(res!=null && res.size()>0){
                binding.name.setText(res.get(0).getName());
                binding.emailAddress.setText(res.get(0).getEmail());
                if(res.get(0).getProfilePic()!=null) {
                    Uri uri = Uri.parse(res.get(0).getProfilePic());
                    String protocol = uri.getScheme();
                    String server = uri.getAuthority();
                    if(protocol!=null && server!=null){
                        Glide.with(getContext()).load(res.get(0).getProfilePic()).into(binding.userDp);
                    }else{
                        Glide.with(getContext()).load(AppConstance.IMG_URL+res.get(0).getProfilePic()).into(binding.userDp);
                    }
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