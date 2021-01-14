package com.skteam.ititest.ui.quiz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.skteam.ititest.R;
import com.skteam.ititest.databinding.ItemQuizBinding;
import com.skteam.ititest.restModel.quiz.ResItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private List<ResItem> resItems = new ArrayList<>();
    private Context context;
    private boolean isSubmited = false;
    private boolean equationFound = false;
    private int i=0;
    private NoQuestionSelected noQuestionSelected;
    private int pos=0;

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
        for (i = 0; i < resItems.size(); i++) {
            if (resItems.get(i).getSelectQuestion() != null) {
                equationFound = true;
                break;
            }
        }
        if (isSubmited && !equationFound) {
            noQuestionSelected.NoQuestionSelected();
        } else {
            noQuestionSelected.getResult(resItems);

        }
    }
    public int getCurrenQuiz(){
        return pos;
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
            pos=getAdapterPosition();
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
            }
            else if ((resItem.getSelectQuestion() == null || resItem.getSelectQuestion().isEmpty()) && isSubmited) {
                binding.answerLay.setOnClickListener(null);
                binding.answerLay2.setOnClickListener(null);
                binding.answer3lay.setOnClickListener(null);
                binding.answer4lay.setOnClickListener(null);
            } else {
                binding.answerLay.setOnClickListener(v -> {
                    resItem.setSelectQuestion("a");
                    resItem.setSkipQuestion(false);
                    if ("a".equalsIgnoreCase(resItem.getAnswer())) {
                        binding.answerLay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer1.setTextColor(Color.WHITE);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.BLACK);
                        resItem.setCorrectAnswerSelected(true);
                    } else {
                        binding.answerLay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer1.setTextColor(Color.WHITE);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.BLACK);
                        resItem.setCorrectAnswerSelected(false);
                    }
                });
                binding.answerLay2.setOnClickListener(v -> {
                    resItem.setSelectQuestion("b");
                    resItem.setSkipQuestion(false);
                    if ("b".equalsIgnoreCase(resItem.getAnswer())) {
                        binding.answerLay2.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.WHITE);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.BLACK);
                        resItem.setCorrectAnswerSelected(true);
                    } else {
                        binding.answerLay2.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                        resItem.setCorrectAnswerSelected(false);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.WHITE);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.BLACK);
                    }
                });
                binding.answer3lay.setOnClickListener(v -> {
                    resItem.setSelectQuestion("c");
                    resItem.setSkipQuestion(false);
                    if ("c".equalsIgnoreCase(resItem.getAnswer())) {
                        resItem.setCorrectAnswerSelected(true);
                        binding.answer3lay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.WHITE);
                        binding.answer4.setTextColor(Color.BLACK);
                    } else {
                        resItem.setCorrectAnswerSelected(false);
                        binding.answer3lay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.WHITE);
                        binding.answer4.setTextColor(Color.BLACK);
                    }
                });
                binding.answer4lay.setOnClickListener(v -> {

                    resItem.setSelectQuestion("d");
                    resItem.setSkipQuestion(false);
                    if ("d".equalsIgnoreCase(resItem.getAnswer())) {
                        binding.answer4lay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.WHITE);
                        resItem.setCorrectAnswerSelected(true);
                    } else {
                        binding.answer4lay.setBackgroundResource(R.drawable.question_select_blue);
                        binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.WHITE);
                        resItem.setCorrectAnswerSelected(false);
                    }
                });
            }

        }
    }

    public interface NoQuestionSelected {
        void NoQuestionSelected();
        void getResult(List<ResItem> resItems);
    }
}
