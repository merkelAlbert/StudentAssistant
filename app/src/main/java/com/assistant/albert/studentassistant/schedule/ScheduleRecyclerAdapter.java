package com.assistant.albert.studentassistant.schedule;

import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assistant.albert.studentassistant.R;
import com.assistant.albert.studentassistant.authentification.SessionManager;
import com.assistant.albert.studentassistant.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

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
        if (holder.scheduleOfDay.getChildCount() == 0) {
            holder.weekDay.setText(ScheduleDays.list.get(position).toString());
            ArrayList daySchedule = dataSet.Schedule().get(position);
            ArrayList<CardView> subjects = new ArrayList<>();

            SessionManager sessionManager = new SessionManager(scheduleCardView.getContext());

            Integer currentWeek = 0;
            try {
                currentWeek = Utils.getInstantInfoItemFromJson(new JSONObject(sessionManager.getInstantInfo())).CurrentWeek();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < daySchedule.size(); i++) {

                ArrayList<String> classSchedule = (ArrayList<String>) daySchedule.get(i);

                CardView subjectCardView = (CardView) LayoutInflater.from(holder.scheduleOfDay.getContext()).
                        inflate(R.layout.content_schedule_subject, holder.scheduleOfDay, false);

                TextView subjectNumber = subjectCardView.findViewById(R.id.subjectNumber);
                TextView numeratorSubject = subjectCardView.findViewById(R.id.numeratorSubject);
                TextView denominatorSubject = subjectCardView.findViewById(R.id.denomenatorSubject);
                TextView subjectDivider = subjectCardView.findViewById(R.id.subjectDivider);

                if (currentWeek % 2 != 0) {
                    numeratorSubject.setTypeface(null, Typeface.BOLD);
                } else {
                    denominatorSubject.setTypeface(null, Typeface.BOLD);
                }

                if (dataSet.CurrentDay() == position)
                    subjectCardView.setBackgroundColor(subjectCardView.getResources().getColor(R.color.currentDay));

                String subjectIndex = Integer.toString(i + 1) + ". ";
                String numeratorString = classSchedule.get(0);
                String denominatorString = classSchedule.get(1);

                subjectNumber.setText(subjectIndex);
                if ((!numeratorString.equals(denominatorString)) && (!numeratorString.isEmpty() && !denominatorString.isEmpty())) {
                    numeratorSubject.setText(numeratorString);
                    denominatorSubject.setText(denominatorString);
                    subjectDivider.setText("|");

                } else if (numeratorString.isEmpty() && !denominatorString.isEmpty()) {
                    numeratorSubject.setText("-");
                    subjectDivider.setText("|");
                    denominatorSubject.setText(denominatorString);

                } else if (!numeratorString.isEmpty() && denominatorString.isEmpty()) {
                    numeratorSubject.setText(numeratorString);
                    denominatorSubject.setText("-");
                    subjectDivider.setText("|");

                } else if ((numeratorString.equals(denominatorString)) && (!numeratorString.isEmpty())) {
                    numeratorSubject.setText(numeratorString);
                    if (currentWeek % 2 == 0) {
                        numeratorSubject.setTypeface(null, Typeface.BOLD);
                    }
                }

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
