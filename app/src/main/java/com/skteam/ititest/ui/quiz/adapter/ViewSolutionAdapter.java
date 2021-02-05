package com.skteam.ititest.ui.quiz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.skteam.ititest.R;
import com.skteam.ititest.databinding.ItemQuizBinding;
import com.skteam.ititest.databinding.ViewSolutionItemBinding;
import com.skteam.ititest.restModel.quiz.ResItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ViewSolutionAdapter extends RecyclerView.Adapter<ViewSolutionAdapter.ViewSolutionViewHolder> {
    private List<ResItem> resItems = new ArrayList<>();
    private Context context;
    private boolean isSubmited = true;
    private boolean equationFound = false;


    public ViewSolutionAdapter(Context context,List<ResItem> resItems) {
        this.context = context;
        this.resItems = resItems;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewSolutionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewSolutionItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_solution_item, parent, false);
        return new ViewSolutionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewSolutionViewHolder holder, final int position) {
        holder.onBindidView(position, resItems.get(position));
    }

    @Override
    public int getItemCount() {
        return resItems.size();
    }


    class ViewSolutionViewHolder extends RecyclerView.ViewHolder {
        private ViewSolutionItemBinding binding;
        private Disposable disposable;

        public ViewSolutionViewHolder(@NonNull ViewSolutionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void onBindidView(final int position, final ResItem resItem) {

            binding.questionOrder.setText(String.valueOf((position+1)+"/"+resItems.size()));
            binding.questionText.setText(resItem.getQuestion());
            binding.answer1.setText("A) "+resItem.getOption1());
            binding.answer2.setText("B) "+resItem.getOption2());
            binding.answer3.setText("C) "+resItem.getOption3());
            binding.answer4.setText("D) "+resItem.getOption4());

            if (resItem.getSelectQuestion() != null && !resItem.getSelectQuestion().isEmpty() && !resItem.isSkipQuestion()&& isSubmited) {
                switch (resItem.getAnswer().toLowerCase()){
                        case "a":{
                            binding.answerLay.setBackgroundResource(R.drawable.question_select_green);
                                if ("a".equalsIgnoreCase(resItem.getSelectQuestion().trim())) {
                                    binding.answerLay.setBackgroundResource(R.drawable.question_select_green);
                                    binding.answer1.setTextColor(Color.WHITE);
                                    binding.answer2.setTextColor(Color.BLACK);
                                    binding.answer3.setTextColor(Color.BLACK);
                                    binding.answer4.setTextColor(Color.BLACK);
                                }else  if ("b".equalsIgnoreCase(resItem.getSelectQuestion().trim())) {
                                    binding.answerLay2.setBackgroundResource(R.drawable.question_select_red);
                                    binding.answer1.setTextColor(Color.BLACK);
                                    binding.answer2.setTextColor(Color.WHITE);
                                    binding.answer3.setTextColor(Color.BLACK);
                                    binding.answer4.setTextColor(Color.BLACK);
                                }else if ("c".equalsIgnoreCase(resItem.getSelectQuestion().trim())) {
                                    binding.answer3lay.setBackgroundResource(R.drawable.question_select_red);
                                    binding.answer1.setTextColor(Color.BLACK);
                                    binding.answer2.setTextColor(Color.BLACK);
                                    binding.answer3.setTextColor(Color.WHITE);
                                    binding.answer4.setTextColor(Color.BLACK);
                                }else if ("d".equalsIgnoreCase(resItem.getSelectQuestion().trim())) {
                                    binding.answer4lay.setBackgroundResource(R.drawable.question_select_red);
                                    binding.answer1.setTextColor(Color.BLACK);
                                    binding.answer2.setTextColor(Color.BLACK);
                                    binding.answer3.setTextColor(Color.BLACK);
                                    binding.answer4.setTextColor(Color.WHITE);
                                }
                            break;
                        }
                        case "b":{
                            binding.answerLay2.setBackgroundResource(R.drawable.question_select_green);
                            if ("a".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answerLay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1.setTextColor(Color.WHITE);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else  if ("b".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answerLay2.setBackgroundResource(R.drawable.question_select_green);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.WHITE);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("c".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer3lay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.WHITE);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("d".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer4lay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.WHITE);
                            }
                            break;
                        }
                        case "c":{
                            binding.answer3lay.setBackgroundResource(R.drawable.question_select_green);
                            if ("a".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answerLay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1.setTextColor(Color.WHITE);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else  if ("b".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answerLay2.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.WHITE);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("c".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer3lay.setBackgroundResource(R.drawable.question_select_green);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.WHITE);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("d".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer4lay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.WHITE);
                            }
                            break;
                        }
                        case "d":{
                            binding.answer4lay.setBackgroundResource(R.drawable.question_select_green);
                            if ("a".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answerLay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1.setTextColor(Color.WHITE);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else  if ("b".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answerLay2.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.WHITE);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("c".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer3lay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.WHITE);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("d".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer4lay.setBackgroundResource(R.drawable.question_select_green);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.WHITE);
                            }
                            break;
                        }
                }
            }else{
                switch (resItem.getAnswer().toLowerCase()){
                    case "a":{
                        binding.answerLay.setBackgroundResource(R.drawable.question_select_green);
                        binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer1.setTextColor(Color.WHITE);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.BLACK);
                        break;
                    }
                    case "b":{
                        binding.answerLay2.setBackgroundResource(R.drawable.question_select_green);
                        binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.WHITE);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.BLACK);
                        break;
                    }
                    case "c":{
                        binding.answer3lay.setBackgroundResource(R.drawable.question_select_green);
                        binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.WHITE);
                        binding.answer4.setTextColor(Color.BLACK);
                        break;
                    }
                    case "d":{
                        binding.answer4lay.setBackgroundResource(R.drawable.question_select_green);
                        binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.WHITE);
                        break;
                    }

                }

            }
        }
    }


}
