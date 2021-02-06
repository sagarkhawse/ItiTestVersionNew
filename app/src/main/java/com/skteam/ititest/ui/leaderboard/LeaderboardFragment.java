
package com.skteam.ititest.ui.leaderboard;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentLeaderboardBinding;
import com.skteam.ititest.restModel.home.leaderboard.Re;
import com.skteam.ititest.setting.AppConstance;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.ui.home.HomeActivity;
import com.skteam.ititest.ui.leaderboard.adapter.LeaderBoardMainAdapter;
import com.skteam.ititest.ui.splash.SplashFragment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

import static com.skteam.ititest.setting.AppConstance.ALL_TIME;
import static com.skteam.ititest.setting.AppConstance.MONTH;
import static com.skteam.ititest.setting.AppConstance.TODAY;

public class LeaderboardFragment extends BaseFragment<FragmentLeaderboardBinding, LeaderBoardViewmodel> implements LeaderNav {
    private FragmentLeaderboardBinding binding;
    private LeaderBoardViewmodel viewmodel;
    private Dialog internetDialog;
    private static LeaderboardFragment instance;
    private Disposable disposable;
    private LeaderBoardMainAdapter adapter;
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
        adapter=new LeaderBoardMainAdapter(getContext());
        binding.participantsList.setAdapter(adapter);
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
                    CallApi(TODAY);
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
                    CallApi(MONTH);
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
                    CallApi(ALL_TIME);
                }
            }
        });
        disposable = RxView.clicks(binding.ivBack).observeOn(AndroidSchedulers.mainThread()).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
            @Override
            public void accept(Unit unit) throws Exception {
                getBaseActivity().onBackPressed();
            }
        });
        binding.allTimeButton.performClick();
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
    private void CallApi(String type){
        viewmodel.GetAllLeaderBoardNow(type).observe(getBaseActivity(), new Observer<List<Re>>() {
            @Override
            public void onChanged(List<Re> res) {
                if(res!=null &&res.size()>0){
                    if(res.get(0).getProfilePic()!=null && !res.get(0).getProfilePic().isEmpty()){
                        Uri uri = Uri.parse(res.get(0).getProfilePic());
                        String protocol = uri.getScheme();
                        String server = uri.getAuthority();
                        if(protocol!=null && server!=null){
                            Glide.with(getContext()).load(res.get(0).getProfilePic()).into(binding.top1);
                        }else{
                            Glide.with(getContext()).load(AppConstance.IMG_URL+res.get(0).getProfilePic()).into(binding.top1);
                        }

                    }
                    if(res.get(0).getName()!=null && !res.get(0).getName().isEmpty() ){
                        binding.nameTop1.setText(res.get(0).getName());
                    }

                    binding.pointTop1.setText(res.get(0).getPoints()+" Pro");
                  if(res.size()<2){
                        binding.layTop2.setVisibility(View.GONE);
                       binding.layTop3.setVisibility(View.GONE);
                    }
                  else  if(res.size()<3){
                      binding.layTop3.setVisibility(View.GONE);
                      binding.layTop2.setVisibility(View.VISIBLE);
                      if(res.get(1).getProfilePic()!=null && !res.get(1).getProfilePic().isEmpty()){
                          Uri uri2 = Uri.parse(res.get(1).getProfilePic());
                          String protocol2 = uri2.getScheme();
                          String server2 = uri2.getAuthority();
                          if(protocol2!=null && server2!=null){
                              Glide.with(getContext()).load(res.get(1).getProfilePic()).into(binding.top2);
                          }else{
                              Glide.with(getContext()).load(AppConstance.IMG_URL+res.get(1).getProfilePic()).into(binding.top2);
                          }
                      }
                      if(res.get(1).getName()!=null && !res.get(1).getName().isEmpty() ){
                          binding.nameTop2.setText(res.get(1).getName());
                      }

                      binding.pointTop2.setText(res.get(1).getPoints()+" Pro");
                  }
                  else{
                      if(res.get(1).getProfilePic()!=null && !res.get(1).getProfilePic().isEmpty()){
                          Uri uri2 = Uri.parse(res.get(1).getProfilePic());
                          String protocol2 = uri2.getScheme();
                          String server2 = uri2.getAuthority();
                          if(protocol2!=null && server2!=null){
                              Glide.with(getContext()).load(res.get(1).getProfilePic()).into(binding.top2);
                          }else{
                              Glide.with(getContext()).load(AppConstance.IMG_URL+res.get(1).getProfilePic()).into(binding.top2);
                          }
                      }
                      if(res.get(2).getProfilePic()!=null && !res.get(2).getProfilePic().isEmpty()){
                          Uri uri3 = Uri.parse(res.get(2).getProfilePic());
                          String protocol3 = uri3.getScheme();
                          String server3 = uri3.getAuthority();
                          if(protocol3!=null && server3!=null){
                              Glide.with(getContext()).load(res.get(2).getProfilePic()).into(binding.top3);
                          }else{
                              Glide.with(getContext()).load(AppConstance.IMG_URL+res.get(2).getProfilePic()).into(binding.top3);
                          }
                      }
                      if(res.get(1).getName()!=null && !res.get(1).getName().isEmpty() ){
                          binding.nameTop2.setText(res.get(1).getName());
                      }
                      if(res.get(2).getName()!=null && !res.get(2).getName().isEmpty() ){
                          binding.nameTop3.setText(res.get(2).getName());
                      }

                       binding.pointTop2.setText(res.get(1).getPoints()+" Pro");
                       binding.pointTop3.setText(res.get(2).getPoints()+" Pro");
                       binding.layTop2.setVisibility(View.VISIBLE);
                       binding.layTop3.setVisibility(View.VISIBLE);
                    }
                   adapter.updateList(res);

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
}