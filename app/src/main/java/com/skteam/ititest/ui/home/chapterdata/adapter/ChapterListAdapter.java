


package com.skteam.ititest.ui.home.chapterdata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.ititest.R;
import com.skteam.ititest.databinding.ItemTestSeriesBinding;
import com.skteam.ititest.restModel.home.subjects.ChapterListItem;
import com.skteam.ititest.ui.home.HomeActivity;
import com.skteam.ititest.ui.home.chapterdata.testseries.TestSeriesFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.chapterListHolder> {


    private final Context mContext;
    private List<ChapterListItem> chapterList = new ArrayList<>();

    public ChapterListAdapter(Context mContext, List<ChapterListItem> chapterList) {
        this.mContext = mContext;
        this.chapterList=chapterList;
    }

    @NonNull
    @Override
    public chapterListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTestSeriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_test_series, parent, false);
        return new chapterListHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull chapterListHolder holder, final int position){
        holder.OnBindView(chapterList.get(position));
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

     class chapterListHolder extends RecyclerView.ViewHolder {
        private ItemTestSeriesBinding binding;
        private Disposable disposable;

        public chapterListHolder(@NonNull ItemTestSeriesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

         public void OnBindView(final ChapterListItem chapterListItem) {
            binding.tvTitle.setText(chapterListItem.getTitle());
            disposable= RxView.clicks(binding.tvTitle).observeOn(AndroidSchedulers.mainThread()).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Unit>() {
                @Override
                public void accept(Unit unit) throws Exception {
                    if (chapterListItem.getTestList() != null && chapterListItem.getTestList().size() > 0) {
                        ((HomeActivity) mContext).startFragment(TestSeriesFragment.newInstance(chapterListItem.getTestList(),chapterListItem.getTitle()),true,TestSeriesFragment.newInstance(chapterListItem.getTestList(),chapterListItem.getTitle()).toString());
                    }else{
                        ((HomeActivity) mContext).showCustomAlert("No Test Series Found");
                        ((HomeActivity) mContext).startFragment(TestSeriesFragment.newInstance(null,null),true,TestSeriesFragment.newInstance(null,null).toString());
                    }
                }
            });
         }
     }


}