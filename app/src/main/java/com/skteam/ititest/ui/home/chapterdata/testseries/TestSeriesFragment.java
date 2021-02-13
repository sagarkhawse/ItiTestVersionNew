package com.skteam.ititest.ui.home.chapterdata.testseries;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseFragment;
import com.skteam.ititest.databinding.FragmentChapterBinding;
import com.skteam.ititest.restModel.home.subjects.TestListItem;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.setting.dialog.SweetAlertDialog;
import com.skteam.ititest.ui.home.HomeActivity;
import com.skteam.ititest.ui.home.HomeNav;
import com.skteam.ititest.ui.home.HomeViewModel;
import com.skteam.ititest.ui.home.chapterdata.testseries.adapter.TestSeriesAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.skteam.ititest.setting.AppConstance.ERROR;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestSeriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestSeriesFragment extends BaseFragment<FragmentChapterBinding, HomeViewModel> implements HomeNav {
    private FragmentChapterBinding binding;
    private HomeViewModel viewModel;
    private static TestSeriesFragment instance;
    private Dialog internetDialog;
    private static List<TestListItem> testList = new ArrayList<>();
    private TestSeriesAdapter testSeriesAdapter;
    private SweetAlertDialog dialog;
    private static String chapterName;
    public TestSeriesFragment() {
        // Required empty public constructor
    }
    @Override
    public String toString() {
        return TestSeriesFragment.class.getName();
    }

    public static TestSeriesFragment newInstance(List<TestListItem> testListMain,String chapter) {
        testList = testListMain;
        chapterName=chapter;
        instance = instance == null ? new TestSeriesFragment() : instance;
        return instance;
    }

    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chapter;
    }

    @Override
    public HomeViewModel getViewModel() {
        return viewModel = new HomeViewModel(getContext(), getSharedPre(), getActivity());
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
        viewModel.setNavigator(this);
        binding.name.setText("Test Series");
        ((HomeActivity)getContext()).getToolbar().setText(chapterName);
        testSeriesAdapter = new TestSeriesAdapter(getContext(), testList);
        CollectAllDataThroughAPI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((HomeActivity)getContext()).getToolbar().setText(getString(R.string.app_name));
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

    private void CollectAllDataThroughAPI() {
        if (testList != null && testList.size() > 0) {
                binding.recyclerView.setAdapter(testSeriesAdapter);
                binding.emptyTxt.setVisibility(View.GONE);
            } else {
                binding.emptyTxt.setText("No Test Series Found");
                binding.emptyTxt.setVisibility(View.VISIBLE);
            dialog = getBaseActivity().showAlertDialog(getActivity(), ERROR, "Test Series  will be updated soon!", "ITI Test");
            dialog.setConfirmText("Go Back")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        dialog.dismissWithAnimation();
                        getBaseActivity().onBackPressed();
                    });
            //dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            dialog.show();
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
    public void setMessage(String s) {
        showCustomAlert(s);
    }

    @Override
    public void LogOut() {

    }
}