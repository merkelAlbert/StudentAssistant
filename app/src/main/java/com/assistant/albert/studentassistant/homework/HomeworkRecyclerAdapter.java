package com.assistant.albert.studentassistant.homework;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.assistant.albert.studentassistant.R;

import java.util.ArrayList;

public class HomeworkRecyclerAdapter extends RecyclerView.Adapter<HomeworkRecyclerAdapter.ViewHolder> {

    private final boolean[] firstCardSelected = {false};
    int selectedCounter;
    private ArrayList<HomeworkResponse> dataSet;
    private View view;

    public HomeworkRecyclerAdapter(ArrayList<HomeworkResponse> dataSet) {
        this.dataSet = dataSet;
        this.selectedCounter = 0;
    }

    private void handleClick(View view, final HomeworkRecyclerAdapter.ViewHolder holder, final int color) {
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (selectedCounter == 0) {
                    holder.cardView.setCardBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                    holder.selected = true;
                    selectedCounter++;
                    firstCardSelected[0] = true;
                }
                return true;
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstCardSelected[0]) {
                    if (selectedCounter > 0) {
                        if (!holder.selected) {
                            holder.selected = true;
                            selectedCounter++;
                            holder.cardView.setCardBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                        } else {
                            holder.selected = false;
                            selectedCounter--;
                            holder.cardView.setCardBackgroundColor(color);
                        }
                    }
                }
            }
        });
//        if (selectedCounter > 0) {
//            textView.setText(selectedCounter);
//        } else {
//            textView.setText(R.string.app_name);
//        }
    }

    private void setColor(View view, HomeworkRecyclerAdapter.ViewHolder holder, int time) {
        GradientDrawable gd = (GradientDrawable) view.getResources().getDrawable(R.drawable.card_time_circle);
        if (time <= 3) {
            gd.setColor(view.getResources().getColor(R.color.redItem));
        }
        if (time >= 7) {
            gd.setColor(view.getResources().getColor(R.color.greenItem));
        }
        if (time > 3 && time < 7) {
            gd.setColor(view.getResources().getColor(R.color.yellowItem));
        }
        holder.time.setBackground(gd);
    }

    @Override
    public HomeworkRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_homework,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeworkRecyclerAdapter.ViewHolder holder, int position) {
        holder.time.setText(Integer.toString(dataSet.get(position).Time()));
        holder.exercise.setText(dataSet.get(position).Exercise());
        holder.subject.setText(dataSet.get(position).Subject());
        holder.selected = false;
        setColor(view, holder, Integer.parseInt(holder.time.getText().toString()));
        handleClick(view, holder, holder.cardView.getCardBackgroundColor().getDefaultColor());
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
        TextView subject;
        TextView exercise;
        TextView time;
        boolean selected;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.homeworkCard);
            subject = itemView.findViewById(R.id.subject);
            exercise = itemView.findViewById(R.id.exercise);
            time = itemView.findViewById(R.id.time);
        }
    }

}
