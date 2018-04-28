package com.assistant.albert.studentassistant.schedule;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.assistant.albert.studentassistant.R;

import java.util.ArrayList;

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {

    private ArrayList<ScheduleResponse> dataSet;
    private View view;

    public ScheduleRecyclerAdapter(ArrayList<ScheduleResponse> dataSet) {
        this.dataSet = dataSet;
    }


    @Override
    public ScheduleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_homework,
//                parent, false);
        //ViewHolder viewHolder = new ViewHolder();
        return null;
    }

    @Override
    public void onBindViewHolder(ScheduleRecyclerAdapter.ViewHolder holder, int position) {
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.homeworkCard);
        }
    }

}
