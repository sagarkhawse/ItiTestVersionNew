

package com.skteam.ititest.ui.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skteam.ititest.R;
import com.skteam.ititest.databinding.ItemSubjectBinding;
import com.skteam.ititest.restModel.home.subjects.ResItem;
import com.skteam.ititest.ui.home.chapterdata.ChapterFragment;
import com.skteam.ititest.ui.home.HomeActivity;
import com.skteam.ititest.ui.home.chapterdata.adapter.ChapterListAdapter;
import com.skteam.ititest.ui.home.chapterdata.testseries.adapter.TestSeriesAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.skteam.ititest.setting.AppConstance.IMG_URL;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {
    List<ResItem> subjectList = new ArrayList<>();
    Context context;
    private ChapterListAdapter chapterListAdapter;
    private TestSeriesAdapter testSeriesAdapter;


    public SubjectAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubjectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_subject, parent, false);
        return new SubjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectHolder holder, final int position) {
        holder.OnBinding(subjectList.get(position));
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    class SubjectHolder extends RecyclerView.ViewHolder {
        ItemSubjectBinding binding;

        public SubjectHolder(ItemSubjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void OnBinding(final ResItem re) {
            binding.tvTitle.setText(re.getTitle());
            if (Integer.parseInt(re.getChaptersCount()) > 0) {
                binding.chapterId.setText(re.getChaptersCount() + " Chapters");
            } else {
                binding.chapterId.setText("No Chapters");
            }

            binding.itemView.setOnClickListener(view -> {
                if(re.getChapterList()!=null && re.getChapterList().size()>0) {
                    ((HomeActivity) context).startFragment(ChapterFragment.newInstance(re.getChapterList()),true,ChapterFragment.newInstance(re.getChapterList()).toString());
                }
                else{
                    ((HomeActivity) context).showCustomAlert("Chapter Not Found");
                    ((HomeActivity) context).startFragment(ChapterFragment.newInstance(null),true,ChapterFragment.newInstance(null).toString());
                }
            });

            Glide.with(context).load(IMG_URL + re.getImage()).into(binding.subjectImage);

        }
    }


    public void updateList(List<ResItem> subjectList) {
        this.subjectList = subjectList;
        notifyDataSetChanged();
    }
}
