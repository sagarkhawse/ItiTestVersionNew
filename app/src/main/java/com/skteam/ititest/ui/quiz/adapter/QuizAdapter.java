package com.skteam.ititest.ui.quiz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private static final String TAG = "QuizAdapterTest";
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

        @SuppressLint("SetTextI18n")
        public void onBindidView(final int position, final ResItem resItem) {
            binding.previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Prev button clicked ");
                    noQuestionSelected.UpdatePreviousButton();
                }
            });
            binding.next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Next button clicked ");
                    noQuestionSelected.UpdateNextButton();
                }
            });
            binding.questionOrder.setText(String.valueOf((position+1)+"/"+resItems.size()));
            binding.questionText.setText(resItem.getQuestion());
            binding.answer1.setText("A) "+resItem.getOption1());
            binding.answer2.setText("B) "+resItem.getOption2());
            binding.answer3.setText("C) "+resItem.getOption3());
            binding.answer4.setText("D) "+resItem.getOption4());
            pos=getAdapterPosition();
            if (resItem.getSelectQuestion() != null && !resItem.getSelectQuestion().isEmpty() && !resItem.isSkipQuestion()&& isSubmited) {
                if(resItem.isCorrectAnswerSelected()){
                    binding.answerLay.setBackgroundResource(R.drawable.question_select_green);
                }else{
                    binding.answerLay.setBackgroundResource(R.drawable.question_select_red);
                }
            }
            else if ((resItem.getSelectQuestion() == null || resItem.getSelectQuestion().isEmpty()) && isSubmited) {
                binding.answerLay.setOnClickListener(null);
                binding.answerLay2.setOnClickListener(null);
                binding.answer3lay.setOnClickListener(null);
                binding.answer4lay.setOnClickListener(null);
            } else {
                binding.answerLay.setOnClickListener(v -> {
                    Log.d(TAG, "onBindidView: Anser option 1 selected ");
                    resItem.setSelectQuestion("a");
                    resItem.setSkipQuestion(false);
                    binding.answerLay.setBackgroundResource(R.drawable.question_select_blue);
                    binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                    binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                    binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                    binding.answer1.setTextColor(Color.WHITE);
                    binding.answer2.setTextColor(Color.BLACK);
                    binding.answer3.setTextColor(Color.BLACK);
                    binding.answer4.setTextColor(Color.BLACK);
                    if ("a".equalsIgnoreCase(resItem.getAnswer())) {
                        resItem.setCorrectAnswerSelected(true);
                    } else {
                        resItem.setCorrectAnswerSelected(false);
                    }
                });
                binding.answerLay2.setOnClickListener(v -> {
                    resItem.setSelectQuestion("b");
                    resItem.setSkipQuestion(false);
                    binding.answerLay2.setBackgroundResource(R.drawable.question_select_blue);
                    binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                    binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                    binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                    binding.answer1.setTextColor(Color.BLACK);
                    binding.answer2.setTextColor(Color.WHITE);
                    binding.answer3.setTextColor(Color.BLACK);
                    binding.answer4.setTextColor(Color.BLACK);
                    if ("b".equalsIgnoreCase(resItem.getAnswer())) {
                        resItem.setCorrectAnswerSelected(true);
                    } else {
                        resItem.setCorrectAnswerSelected(false);
                    }
                });
                binding.answer3lay.setOnClickListener(v -> {
                    resItem.setSelectQuestion("c");
                    resItem.setSkipQuestion(false);
                    binding.answer3lay.setBackgroundResource(R.drawable.question_select_blue);
                    binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                    binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                    binding.answer4lay.setBackgroundResource(R.drawable.button_border_black);
                    binding.answer1.setTextColor(Color.BLACK);
                    binding.answer2.setTextColor(Color.BLACK);
                    binding.answer3.setTextColor(Color.WHITE);
                    binding.answer4.setTextColor(Color.BLACK);
                    if ("c".equalsIgnoreCase(resItem.getAnswer())) {
                        resItem.setCorrectAnswerSelected(true);
                    } else {
                        resItem.setCorrectAnswerSelected(false);
                    }
                });
                binding.answer4lay.setOnClickListener(v -> {
                    resItem.setSelectQuestion("d");
                    resItem.setSkipQuestion(false);
                    binding.answer4lay.setBackgroundResource(R.drawable.question_select_blue);
                    binding.answerLay.setBackgroundResource(R.drawable.button_border_black);
                    binding.answerLay2.setBackgroundResource(R.drawable.button_border_black);
                    binding.answer3lay.setBackgroundResource(R.drawable.button_border_black);
                    binding.answer1.setTextColor(Color.BLACK);
                    binding.answer2.setTextColor(Color.BLACK);
                    binding.answer3.setTextColor(Color.BLACK);
                    binding.answer4.setTextColor(Color.WHITE);
                    if ("d".equalsIgnoreCase(resItem.getAnswer())) {
                        resItem.setCorrectAnswerSelected(true);
                    } else {
                        resItem.setCorrectAnswerSelected(false);
                    }
                });
            }

        }
    }

    public interface NoQuestionSelected {
        void NoQuestionSelected();
        void getResult(List<ResItem> resItems);
        void UpdatePreviousButton();
        void UpdateNextButton();
    }
}
