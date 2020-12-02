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

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private List<ResItem> resItems = new ArrayList<>();
    private Context context;
    private boolean isSubmited = false;
    private boolean equationFound = false;
    private int i=0;
    private NoQuestionSelected noQuestionSelected;

    public QuizAdapter(Context context, NoQuestionSelected noQuestionSelected) {
        this.context = context;
        this.noQuestionSelected = noQuestionSelected;
    }

    public void UpdateList(List<ResItem> resItems) {
        this.resItems = resItems;
        notifyDataSetChanged();
    }

    public void UpdateSubmit(boolean isSubmited) {
        this.isSubmited = isSubmited;
        for ( i = 0; i < resItems.size(); i++) {
            if (resItems.get(i).getSelectQuestion() != null) {
                equationFound=true;
                break;
            }
        }
        if(isSubmited && !equationFound ){
            noQuestionSelected.NoQuestionSelected();
        }else{
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuizBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_quiz, parent, false);
        return new QuizViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, final int position) {
        holder.onBindidView(position, resItems.get(position));
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
            this.binding = binding;
        }

        public void onBindidView(final int position, final ResItem resItem) {
            binding.questionText.setText(resItem.getQuestion());
            binding.answer1.setText(resItem.getOption1());
            binding.answer2.setText(resItem.getOption2());
            binding.answer3.setText(resItem.getOption3());
            binding.answer4.setText(resItem.getOption4());
            if (resItem.getSelectQuestion() != null && !resItem.getSelectQuestion().isEmpty() && isSubmited) {
                switch (resItem.getSelectQuestion()) {
                    case "a": {
                        if (resItem.getAnswer().equalsIgnoreCase(resItem.getSelectQuestion())) {
                            binding.answerLay.setBackgroundResource(R.drawable.question_select_green);
                        } else {
                            binding.answerLay.setBackgroundResource(R.drawable.question_select_red);
                        }
                        break;
                    }
                    case "b": {
                        if (resItem.getAnswer().equalsIgnoreCase(resItem.getSelectQuestion())) {
                            binding.answerLay2.setBackgroundResource(R.drawable.question_select_green);
                        } else {
                            binding.answerLay2.setBackgroundResource(R.drawable.question_select_red);
                        }
                        break;
                    }
                    case "c": {
                        if (resItem.getAnswer().equalsIgnoreCase(resItem.getSelectQuestion())) {
                            binding.answer3lay.setBackgroundResource(R.drawable.question_select_green);
                        } else {
                            binding.answer3lay.setBackgroundResource(R.drawable.question_select_red);
                        }
                        break;
                    }
                    case "d": {
                        if (resItem.getAnswer().equalsIgnoreCase(resItem.getSelectQuestion())) {
                            binding.answer4lay.setBackgroundResource(R.drawable.question_select_green);
                        } else {
                            binding.answer4lay.setBackgroundResource(R.drawable.question_select_red);
                        }
                        break;
                    }
                }
            } else if ((resItem.getSelectQuestion() == null || resItem.getSelectQuestion().isEmpty()) && isSubmited) {

            } else {
                binding.answer1.setOnClickListener(v -> {
                    binding.answer1.setOnClickListener(null);
                    binding.answer2.setOnClickListener(null);
                    binding.answer3.setOnClickListener(null);
                    binding.answer4.setOnClickListener(null);
                    resItem.setSelectQuestion("a");
                    if ("a".equalsIgnoreCase(resItem.getAnswer())) {
                        binding.answerLay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answer1.setTextColor(Color.WHITE);
                        resItem.setCorrectAnswerSelected(true);
                    } else {
                        binding.answerLay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answer1.setTextColor(Color.WHITE);
                        resItem.setCorrectAnswerSelected(false);
                    }
                });
                binding.answer2.setOnClickListener(v -> {
                    binding.answer1.setOnClickListener(null);
                    binding.answer2.setOnClickListener(null);
                    binding.answer3.setOnClickListener(null);
                    binding.answer4.setOnClickListener(null);
                    resItem.setSelectQuestion("b");
                    if ("b".equalsIgnoreCase(resItem.getAnswer())) {
                        binding.answerLay2.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answer2.setTextColor(Color.WHITE);
                        resItem.setCorrectAnswerSelected(true);
                    } else {
                        binding.answerLay2.setBackgroundResource(R.drawable.question_select_blue);
                        resItem.setCorrectAnswerSelected(false);
                        binding.answer2.setTextColor(Color.WHITE);
                    }
                });
                binding.answer3.setOnClickListener(v -> {
                    binding.answer1.setOnClickListener(null);
                    binding.answer2.setOnClickListener(null);
                    binding.answer3.setOnClickListener(null);
                    binding.answer4.setOnClickListener(null);
                    resItem.setSelectQuestion("c");
                    if ("c".equalsIgnoreCase(resItem.getAnswer())) {
                        resItem.setCorrectAnswerSelected(true);
                        binding.answer3lay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answer3.setTextColor(Color.WHITE);
                    } else {
                        resItem.setCorrectAnswerSelected(false);
                        binding.answer3lay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answer3.setTextColor(Color.WHITE);
                    }
                });
                binding.answer4.setOnClickListener(v -> {
                    binding.answer1.setOnClickListener(null);
                    binding.answer2.setOnClickListener(null);
                    binding.answer3.setOnClickListener(null);
                    binding.answer4.setOnClickListener(null);
                    resItem.setSelectQuestion("d");
                    if ("d".equalsIgnoreCase(resItem.getAnswer())) {
                        binding.answer4lay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answer4.setTextColor(Color.WHITE);
                        resItem.setCorrectAnswerSelected(true);
                    } else {
                        binding.answer4lay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answer4.setTextColor(Color.WHITE);
                        resItem.setCorrectAnswerSelected(false);
                    }
                });
            }

        }
    }

    public interface NoQuestionSelected {
        void NoQuestionSelected();
    }
}
