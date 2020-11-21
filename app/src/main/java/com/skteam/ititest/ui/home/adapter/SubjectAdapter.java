/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 *
 *
 */

package com.skteam.ititest.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.skteam.ititest.R;
import com.skteam.ititest.databinding.ItemSubjectBinding;
import com.skteam.ititest.restModel.home.subjects.ChapterListItem;
import com.skteam.ititest.restModel.home.subjects.ResItem;
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

        public void OnBinding(final ResItem re) {
            binding.tvTitle.setText(re.getTitle());
            if (Integer.parseInt(re.getChaptersCount()) > 0) {
                binding.chapterId.setText(re.getChaptersCount() + " Chapters");
            } else {
                binding.chapterId.setText("No Chapters");
            }

            Glide.with(context).load(IMG_URL + re.getImage()).into(binding.subjectImage);
            ChapterListItem chapterList0Item=new ChapterListItem();
            chapterList0Item.setTitle(context.getString(R.string.select_chapter));
            chapterList0Item.setTestList(null);
            List<ChapterListItem> chapterListItem=new ArrayList<>();
            if(re.getChapterList()!=null && re.getChapterList().size()>0 ){
                chapterListItem=re.getChapterList();
                chapterListItem.add(0,chapterList0Item);

            }else{
                chapterListItem.add(0,chapterList0Item);
            }
            chapterListAdapter=new ChapterListAdapter(context,R.layout.custom_spinner,chapterListItem);

            binding.spinnerChapters.setAdapter(chapterListAdapter);
            //spinner item selected listener
            List<ChapterListItem> finalChapterListItem = chapterListItem;
            binding.spinnerChapters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if(finalChapterListItem.get(i).getTestList()!=null && finalChapterListItem.get(i).getTestList().size()>0) {
                            testSeriesAdapter = new TestSeriesAdapter(context, finalChapterListItem.get(i).getTestList());
                            binding.rvTestSeries.setAdapter(testSeriesAdapter);
                        }else{
                            testSeriesAdapter=new TestSeriesAdapter(context,new ArrayList<>());
                            binding.rvTestSeries.setAdapter(testSeriesAdapter);
                        }
                        Toast.makeText(context, "" + finalChapterListItem.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }


    public void updateList(List<ResItem> subjectList) {
        this.subjectList = subjectList;
        notifyDataSetChanged();
    }
}
