package com.assistant.albert.studentassistant.schedule;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assistant.albert.studentassistant.R;

import java.util.ArrayList;

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ViewHolder> {

    private ScheduleItem dataSet;
    private View scheduleCardView;

    public ScheduleRecyclerAdapter(ScheduleItem dataSet) {
        this.dataSet = dataSet;
    }


    @Override
    public ScheduleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        scheduleCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_schedule,
                parent, false);
        return new ViewHolder(scheduleCardView);
    }

    @Override
    public void onBindViewHolder(ScheduleRecyclerAdapter.ViewHolder holder, int position) {

        if(holder.scheduleOfDay.getChildCount()==0) {
            holder.weekDay.setText(ScheduleDays.list.get(position).toString());

            ArrayList daySchedule = dataSet.Schedule().get(position);
            ArrayList<CardView> subjects = new ArrayList<>();

            for (int i = 0; i < daySchedule.size(); i++) {

                ArrayList<String> classSchedule = (ArrayList<String>) daySchedule.get(i);

                CardView subjectCardView = (CardView) LayoutInflater.from(holder.scheduleOfDay.getContext()).
                        inflate(R.layout.content_schedule_subject, holder.scheduleOfDay, false);
                TextView subjectTextView = subjectCardView.findViewById(R.id.scheduleSubjectTextView);

                String subjectIndex = Integer.toString(i + 1) + ". ";
                String numeratorString = classSchedule.get(0);
                String denominatorString = classSchedule.get(1);
                String subjectString = subjectIndex;

                if ((!numeratorString.equals(denominatorString)) && (!numeratorString.isEmpty() && !denominatorString.isEmpty())) {
                    subjectString += numeratorString + " / " + denominatorString;

                } else if (numeratorString.isEmpty() && !denominatorString.isEmpty()) {
                    subjectString += "- / " + denominatorString;

                } else if (!numeratorString.isEmpty() && denominatorString.isEmpty()) {
                    subjectString += numeratorString + " / -";

                } else if ((numeratorString.equals(denominatorString)) && (!numeratorString.isEmpty())) {
                    subjectString += numeratorString;
                }


                subjectTextView.setText(subjectString);
                subjects.add(subjectCardView);
            }

            for (int i = 0; i < subjects.size(); i++) {
                holder.scheduleOfDay.addView(subjects.get(i));
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return dataSet.Schedule().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout scheduleOfDay;
        TextView weekDay;

        public ViewHolder(View scheduleView) {
            super(scheduleView);
            weekDay = scheduleView.findViewById(R.id.weekDay);
            scheduleOfDay = scheduleView.findViewById(R.id.scheduleOfDay);
        }
    }

}
