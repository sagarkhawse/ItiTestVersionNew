/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

package com.skteam.ititest.ui.leaderboard.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skteam.ititest.R;
import com.skteam.ititest.databinding.ItemBestPlayersBinding;
import com.skteam.ititest.databinding.ParticipantItemBinding;
import com.skteam.ititest.restModel.home.leaderboard.Re;
import com.skteam.ititest.setting.AppConstance;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardMainAdapter extends RecyclerView.Adapter<LeaderBoardMainAdapter.LeaderBoardViewHolder> {
   private  List<Re> lederboardList=new ArrayList<>();
   private  Context context;


    public LeaderBoardMainAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public LeaderBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       ParticipantItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.participant_item, parent, false);
        return new LeaderBoardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardViewHolder holder, final int position) {
       holder.onBinding(position,lederboardList.get(position));
    }

    @Override
    public int getItemCount() {
        return lederboardList.size();
    }

    class LeaderBoardViewHolder extends RecyclerView.ViewHolder {
        ParticipantItemBinding binding;

        public LeaderBoardViewHolder( ParticipantItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBinding(final int position ,final Re re) {

            Uri uri = Uri.parse(re.getProfilePic());
            String protocol = uri.getScheme();
            String server = uri.getAuthority();
          if(protocol!=null && server!=null){
              Glide.with(context).load(re.getProfilePic()).into(binding.profile);
          }else{
              Glide.with(context).load(AppConstance.IMG_URL+re.getProfilePic()).into(binding.profile);
          }
            binding.name.setText(re.getName());
            binding.score.setText(re.getPoints());
            binding.score.setText(re.getPoints());
            binding.order.setText(String.valueOf(position+1));
        }
    }



    public void updateList(List<Re> lederboardList) {
        this.lederboardList = lederboardList;
        notifyDataSetChanged();
    }
}
