package com.assistant.albert.studentassistant.studentassistant.homework;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.assistant.albert.studentassistant.R;

import java.util.ArrayList;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.ViewHolder> {

    private final boolean[] firstcardSelected = {false};
    private ArrayList<HomeworkResponse> dataSet;
    private View view;

    public HomeworkAdapter(ArrayList<HomeworkResponse> dataSet) {
        this.dataSet = dataSet;
    }

    private void handleClick(View view, final CardView cardView, int color) {
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cardView.setCardBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                firstcardSelected[0] = true;
                return true;
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(cardView.getContext(), Boolean.toString(firstcardSelected[0]),
                        Toast.LENGTH_LONG).show();
                if (firstcardSelected[0]) {
                    cardView.setCardBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                }
            }
        });
    }

    private void setColor(View view, CardView cardView, int time) {
        if (time <= 3) {
            cardView.setCardBackgroundColor(view.getResources().getColor(R.color.redItem));
        } else if (time >= 7) {
            cardView.setCardBackgroundColor(view.getResources().getColor(R.color.greenItem));
        } else {
            cardView.setCardBackgroundColor(view.getResources().getColor(R.color.yellowItem));
        }
    }

    @Override
    public HomeworkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homeworkcard,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeworkAdapter.ViewHolder holder, int position) {
        holder.time.setText(Integer.toString(dataSet.get(position).Time()));
        holder.exercise.setText(dataSet.get(position).Exercise());
        holder.subject.setText(dataSet.get(position).Subject());
        setColor(view, holder.cardView, Integer.parseInt(holder.time.getText().toString()));
        handleClick(view, holder.cardView, holder.cardView.getCardBackgroundColor().getDefaultColor());
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

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.homeworkCard);
            subject = itemView.findViewById(R.id.subject);
            exercise = itemView.findViewById(R.id.exercise);
            time = itemView.findViewById(R.id.time);
        }
    }
}
