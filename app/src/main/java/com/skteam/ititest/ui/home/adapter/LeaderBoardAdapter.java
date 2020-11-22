/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

package com.skteam.ititest.ui.home.adapter;

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
import com.skteam.ititest.restModel.home.leaderboard.Re;
import com.skteam.ititest.setting.AppConstance;

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

            Uri uri = Uri.parse(re.getProfilePic());
            String protocol = uri.getScheme();
            String server = uri.getAuthority();
          if(protocol!=null && server!=null){
              Glide.with(context).load(re.getProfilePic()).into(binding.userDp);
          }else{
              Glide.with(context).load(AppConstance.IMG_URL+re.getProfilePic()).into(binding.userDp);
          }
            binding.userName.setText(re.getName()+"\n"+re.getPoints());
        }
    }



    public void updateList(List<Re> lederboardList) {
        this.lederboardList = lederboardList;
        notifyDataSetChanged();
    }
}
