package com.skteam.ititest.ui.quiz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.view.RxView;
import com.skteam.ititest.R;
import com.skteam.ititest.databinding.ItemQuizBinding;
import com.skteam.ititest.databinding.ItemTestSeriesBinding;
import com.skteam.ititest.restModel.quiz.ResItem;
import com.skteam.ititest.ui.home.chapterdata.adapter.ChapterListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder>  {
    private List<ResItem> resItems=new ArrayList<>();
    private Context context;

    public QuizAdapter(Context context) {
        this.context = context;
    }
    public void UpdateList(List<ResItem> resItems){
        this.resItems=resItems;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuizBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_quiz, parent, false);
        return new QuizViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, final int position) {
        holder.onBindidView(position,resItems.get(position));
    }

    @Override
    public int getItemCount() {
        return resItems.size();
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {
        private ItemQuizBinding binding;
        private Disposable disposable;
        public QuizViewHolder(@NonNull ItemQuizBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void onBindidView(final int position,final ResItem resItem) {
            binding.questionText.setText(resItem.getQuestion());
            binding.answer1.setText(resItem.getOption1());
            binding.answer2.setText(resItem.getOption2());
            binding.answer3.setText(resItem.getOption3());
            binding.answer4.setText(resItem.getOption4());
            binding.answer1.setOnClickListener(v -> {
                binding.answer1.setOnClickListener(null);
                binding.answer2.setOnClickListener(null);
                binding.answer3.setOnClickListener(null);
                binding.answer4.setOnClickListener(null);
                if(binding.answer1.getText().toString().equalsIgnoreCase(resItem.getAnswer())){
                    binding.answerLay.setBackgroundResource(R.drawable.question_select_green);
                    binding.answer1.setTextColor(Color.WHITE);
                }else{
                    binding.answerLay.setBackgroundResource(R.drawable.question_select_red);
                    binding.answer1.setTextColor(Color.WHITE);
                }
            });
            binding.answer2.setOnClickListener(v -> {
                binding.answer1.setOnClickListener(null);
                binding.answer2.setOnClickListener(null);
                binding.answer3.setOnClickListener(null);
                binding.answer4.setOnClickListener(null);
                if(binding.answer2.getText().toString().equalsIgnoreCase(resItem.getAnswer())){
                    binding.answerLay2.setBackgroundResource(R.drawable.question_select_green);
                    binding.answer2.setTextColor(Color.WHITE);
                }else{
                    binding.answerLay2.setBackgroundResource(R.drawable.question_select_red);
                    binding.answer2.setTextColor(Color.WHITE);
                }
            });
            binding.answer3.setOnClickListener(v -> {
                binding.answer1.setOnClickListener(null);
                binding.answer2.setOnClickListener(null);
                binding.answer3.setOnClickListener(null);
                binding.answer4.setOnClickListener(null);
                if(binding.answer3.getText().toString().equalsIgnoreCase(resItem.getAnswer())){
                    binding.answer3lay.setBackgroundResource(R.drawable.question_select_green);
                    binding.answer3.setTextColor(Color.WHITE);
                }else{
                    binding.answer3lay.setBackgroundResource(R.drawable.question_select_red);
                    binding.answer3.setTextColor(Color.WHITE);
                }
            });
            binding.answer4.setOnClickListener(v -> {
                binding.answer1.setOnClickListener(null);
                binding.answer2.setOnClickListener(null);
                binding.answer3.setOnClickListener(null);
                binding.answer4.setOnClickListener(null);
                if(binding.answer4.getText().toString().equalsIgnoreCase(resItem.getAnswer())){
                    binding.answer4lay.setBackgroundResource(R.drawable.question_select_green);
                    binding.answer4.setTextColor(Color.WHITE);
                }else{
                    binding.answer4lay.setBackgroundResource(R.drawable.question_select_red);
                    binding.answer4.setTextColor(Color.WHITE);
                }
            });
        }
    }
}
