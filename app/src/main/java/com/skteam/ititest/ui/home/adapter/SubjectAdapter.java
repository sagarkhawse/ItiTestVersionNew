/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.skteam.ititest.R;
import com.skteam.ititest.databinding.ItemSubjectBinding;
import com.skteam.ititest.restModel.home.subjects.Re;

import java.util.ArrayList;
import java.util.List;

import static com.skteam.ititest.setting.AppConstance.IMG_URL;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {
    List<Re> subjectList = new ArrayList<>();
    Context context;


    public SubjectAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_subject, parent, false);
        return new SubjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectHolder holder, final int position) {
        holder.OnBinding(subjectList.get(position));
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    class SubjectHolder extends RecyclerView.ViewHolder {
        ItemSubjectBinding binding;

        public SubjectHolder(ItemSubjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void OnBinding(final Re re) {
            binding.tvTitle.setText(re.getTitle());
            if(Integer.parseInt(re.getChapters())>0){
                binding.chapterId.setText(re.getChapters()+" Chapters");
            }else{
                binding.chapterId.setText("No Chapters");
            }

            Glide.with(context).load(IMG_URL + re.getImage()).into(binding.subjectImage);
        }
    }


    public void updateList(List<Re> subjectList) {
        this.subjectList = subjectList;
        notifyDataSetChanged();
    }
}
