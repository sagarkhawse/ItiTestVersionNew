package com.skteam.ititest.ui.quiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.skteam.ititest.R;
import com.skteam.ititest.databinding.ItemQuizBinding;
import com.skteam.ititest.databinding.ItemQuizStatusBinding;
import com.skteam.ititest.restModel.quiz.ResItem;

import java.util.ArrayList;
import java.util.List;

public class PostionAdapter extends RecyclerView.Adapter<PostionAdapter.PostionViewHolder> {
    private Context context;
    private List<ResItem> resItems=new ArrayList<>();
    private onClickQuistionStatus  onClickQuistionStatus;

    public PostionAdapter(Context context,onClickQuistionStatus quistionStatus) {
        this.context = context;
        onClickQuistionStatus=quistionStatus;
    }
    public void UpdateList(List<ResItem> resItems){
        this.resItems=resItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuizStatusBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_quiz_status, parent, false);
        return new PostionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostionViewHolder holder,final int position) {
        holder.binding.statusText.setText(String.valueOf(position+1));
        holder.binding.statusText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickQuistionStatus.onClickOnStatus(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resItems.size();
    }

    class PostionViewHolder extends RecyclerView.ViewHolder {
        private ItemQuizStatusBinding binding;
        public PostionViewHolder(@NonNull ItemQuizStatusBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
    public interface onClickQuistionStatus {
        void onClickOnStatus(final int pos);
    }
}
