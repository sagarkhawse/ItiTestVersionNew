/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
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
import com.skteam.ititest.databinding.ItemBestPlayersBinding;
import com.skteam.ititest.restModel.home.leaderboard.Re;
import java.util.ArrayList;
import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardViewHolder> {
   private  List<Re> lederboardList=new ArrayList<>();
   private  Context context;


    public LeaderBoardAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public LeaderBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       ItemBestPlayersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_best_players, parent, false);
        return new LeaderBoardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardViewHolder holder, final int position) {
       holder.onBinding(lederboardList.get(position));
    }

    @Override
    public int getItemCount() {
        return lederboardList.size();
    }

    class LeaderBoardViewHolder extends RecyclerView.ViewHolder {
        ItemBestPlayersBinding binding;

        public LeaderBoardViewHolder( ItemBestPlayersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBinding(final Re re) {
            Glide.with(context).load("https://lh3.googleusercontent.com/a-/AOh14Gj2G6zOxvhbf1kNH-2vUwt21HPJl-nyxuBJwk9bEhM=s96-c").into(binding.userDp);
            binding.userName.setText(re.getName()+"\n"+re.getPoints());
        }
    }



    public void updateList(List<Re> lederboardList) {
        this.lederboardList = lederboardList;
        notifyDataSetChanged();
    }
}
