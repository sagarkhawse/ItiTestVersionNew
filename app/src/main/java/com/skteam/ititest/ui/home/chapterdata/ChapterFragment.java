package com.skteam.ititest.ui.home.chapterdata;

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
import com.skteam.ititest.databinding.FragmentChapterBinding;
import com.skteam.ititest.restModel.home.subjects.ChapterListItem;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.setting.dialog.SweetAlertDialog;
import com.skteam.ititest.ui.home.HomeNav;
import com.skteam.ititest.ui.home.HomeViewModel;
import com.skteam.ititest.ui.home.chapterdata.adapter.ChapterListAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.skteam.ititest.setting.AppConstance.ERROR;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChapterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChapterFragment extends BaseFragment<FragmentChapterBinding, HomeViewModel> implements HomeNav {
    private FragmentChapterBinding binding;
    private HomeViewModel viewModel;
    private static ChapterFragment instance;
    private Dialog internetDialog;
    private static List<ChapterListItem> chapterList = new ArrayList<>();
    private ChapterListAdapter chapterListAdapter;
    private SweetAlertDialog dialog;

    public ChapterFragment() {
        // Required empty public constructor
    }

    public static ChapterFragment newInstance(List<ChapterListItem> chapterListmain) {
        chapterList = chapterListmain;
        instance = instance == null ? new ChapterFragment() : new ChapterFragment();
        return instance;
    }

    @Override
    public String toString() {
        return ChapterFragment.class.getName();
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
        binding.name.setText("Chapter");
        chapterListAdapter = new ChapterListAdapter(getContext(), chapterList);
        CollectAllDataThroughAPI();
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

        if (chapterList != null && chapterList.size() > 0) {
            binding.recyclerView.setAdapter(chapterListAdapter);
            binding.emptyTxt.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setText(getContext().getResources().getString(R.string.no_chapter_found));
            binding.emptyTxt.setVisibility(View.VISIBLE);
            dialog = getBaseActivity().showAlertDialog(getActivity(), ERROR, "Chapter will be updated soon!", "ITI Test");
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

}