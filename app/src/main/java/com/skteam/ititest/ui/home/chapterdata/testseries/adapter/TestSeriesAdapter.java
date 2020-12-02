/*
 * Copyright (c) Ishant Sharma & Sagar Khawse
 * Advance Android Developer
 * SKTeam
 *
 *
 *
 */

package com.skteam.ititest.ui.home.chapterdata.testseries.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.skteam.ititest.R;
import com.skteam.ititest.databinding.ItemTestSeriesBinding;
import com.skteam.ititest.restModel.home.subjects.TestListItem;
import com.skteam.ititest.ui.home.HomeActivity;
import com.skteam.ititest.ui.home.chapterdata.testseries.TestSeriesFragment;
import com.skteam.ititest.ui.quiz.QuizFragment;

import java.util.ArrayList;
import java.util.List;

public class TestSeriesAdapter extends RecyclerView.Adapter<TestSeriesAdapter.TestSeriesViewHolder> {
    private List<TestListItem> testList = new ArrayList<>();
    private Context context;


    public TestSeriesAdapter(Context context,List<TestListItem> testList) {
        this.context = context;
        this.testList=testList;
    }

    @NonNull
    @Override
    public TestSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTestSeriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_test_series, parent, false);
        return new TestSeriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TestSeriesViewHolder holder, final int position) {
        holder.onBinding(testList.get(position));
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    class TestSeriesViewHolder extends RecyclerView.ViewHolder {
        private ItemTestSeriesBinding binding;

        public TestSeriesViewHolder(ItemTestSeriesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBinding(final TestListItem testListItem) {
            binding.tvTitle.setText(testListItem.getTitle() );
            binding.tvTitle.setOnClickListener(v -> {
                context.startActivity(new Intent(context,QuizFragment.class).putExtra("test_Id",testListItem.getTestId()));
            });
        }
    }


    public void updateList(List<TestListItem> testList) {
        this.testList = testList;
        notifyDataSetChanged();
    }
}