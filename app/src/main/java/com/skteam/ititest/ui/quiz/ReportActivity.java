package com.skteam.ititest.ui.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skteam.ititest.BuildConfig;
import com.skteam.ititest.R;
import com.skteam.ititest.baseclasses.BaseActivity;
import com.skteam.ititest.databinding.ActivityReportBinding;
import com.skteam.ititest.restModel.quiz.ResItem;
import com.skteam.ititest.setting.CommonUtils;
import com.skteam.ititest.ui.quiz.adapter.QuizAdapter;

import java.lang.reflect.Type;
import java.util.List;

import static com.skteam.ititest.setting.AppConstance.ERROR;

public class ReportActivity extends BaseActivity<ActivityReportBinding, QuizViewModel> implements QuizNav {
    private QuizViewModel viewmodel;
    private ActivityReportBinding binding;
    private Dialog internetDialog;
    private String time;
    int Correctcount = 0;
    int wrongecount = 0;
    int skipCount = 0;
    int completeCount = 0;
    double accuracy = 0;
    double complete = 0;


    @Override
    public int getBindingVariable() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_report;
    }

    @Override
    public QuizViewModel getViewModel() {
        return viewmodel = new QuizViewModel(this, getSharedPre(), this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewmodel.setNavigator(this);
        Gson gson = new Gson();
        String json = getIntent().getStringExtra("data");
        time = getIntent().getStringExtra("timeMain");
        String title = getIntent().getStringExtra("testName");
        if (json.isEmpty()) {
            Toast.makeText(this, "There is something error", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Type type = new TypeToken<List<ResItem>>() {
            }.getType();
            List<ResItem> restItems = gson.fromJson(json, type);
            binding.time.setText(time);
            binding.totalQuestions.setText("" + restItems.size());
            Correctcount = 0;
            for (int i = 0; i < restItems.size(); i++) {
                if (restItems.get(i).isCorrectAnswerSelected()) {
                    Correctcount = Correctcount + 1;
                    completeCount = completeCount + 1;
                } else {
                    wrongecount = wrongecount + 1;
                    completeCount = completeCount + 1;
                }
                if (restItems.get(i).isSkipQuestion()) {
                    skipCount = skipCount + 1;
                }
            }
            completeCount = completeCount - skipCount;
            wrongecount = wrongecount - skipCount;
            complete = (completeCount * 100) / restItems.size();
            accuracy = (Correctcount * 100) / restItems.size();
            binding.score.setText("" + Correctcount);
            int Score = 0;
            if (getSharedPre().getUserScore() != null && !getSharedPre().getUserScore().isEmpty()) {
                Score = Integer.parseInt(getSharedPre().getUserScore()) + Correctcount;
            } else {
                Score = Correctcount;
            }
            binding.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            viewmodel.UpdateTheScore(String.valueOf(Score));
            binding.scoreTotal.setText("" + Correctcount + "/" + restItems.size());
            binding.correctTotal.setText("" + Correctcount + "/" + restItems.size());
            binding.skippedTotal.setText("" + skipCount + "/" + restItems.size());
            binding.wrongTotal.setText("" + wrongecount + "/" + restItems.size());
            binding.completePersent.setText("" + complete + " %");
            binding.accuracyPercent.setText("" + accuracy + " %");
            binding.title.setText(title);
            binding.shareScoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String ShareSub = " Hey!! My Score is "+Correctcount +" Out of "+restItems.size() +" in ITI " +title+" Download Now and Beat My Score  ";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub+uri);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, ShareSub+uri);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });
        }

        binding.overViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.overviewLay.setVisibility(View.VISIBLE);
                binding.reportLay.setVisibility(View.GONE);
                binding.overViewBtn.setTextColor(getResources().getColor(R.color.colorTheme2));
                binding.reportBtn.setTextColor(getResources().getColor(R.color.black));
            }
        });
        binding.reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.overviewLay.setVisibility(View.GONE);
                binding.reportLay.setVisibility(View.VISIBLE);
                binding.overViewBtn.setTextColor(getResources().getColor(R.color.black));
                binding.reportBtn.setTextColor(getResources().getColor(R.color.colorTheme2));
            }
        });
        binding.ViewSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ReportActivity.this, SubmitActivity.class).putExtra("data", json), 001);
            }
        });

        binding.back.setOnClickListener(view -> {
           finish();
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (internetDialog == null) {
            internetDialog = CommonUtils.InternetConnectionAlert(this, false);
        }
        if (isConnected) {
            internetDialog.dismiss();

        } else {
            internetDialog.show();
        }
    }

    @Override
    public void setLoading(boolean b) {
        if (b) {
            showLoadingDialog("");
        } else {
            hideLoadingDialog();
        }
    }

    @Override
    public void setMessage(String s) {
        showCustomAlert("Quiz will be updated soon!");
    }
}