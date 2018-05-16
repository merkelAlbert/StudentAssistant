package com.assistant.albert.studentassistant.homework;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.assistant.albert.studentassistant.R;

import java.util.ArrayList;

public class HomeworkRecyclerAdapter extends RecyclerView.Adapter<HomeworkRecyclerAdapter.ViewHolder> {

    private final boolean[] firstCardSelected = {false};
    private int selectedCounter;
    private ArrayList<HomeworkItem> dataSet;
    private View view;

    private final int EDIT = 0;
    private final int PASSED = 1;
    private final int DELETE = 2;

    public HomeworkRecyclerAdapter(ArrayList<HomeworkItem> dataSet) {
        this.dataSet = dataSet;
        this.selectedCounter = 0;
    }


    private void passHomework(ViewHolder holder){

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
                } else if (holder.selected) {
                    holder.cardView.showContextMenu();
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
                    } else {
                        firstCardSelected[0] = false;
                    }
                } else {
                    holder.cardView.showContextMenu();
                }
            }
        });
        holder.cardView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, final View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                menu.setHeaderTitle("Домашнее задание");
                menu.add(0, PASSED, 0, "Сдано");
                menu.add(0, EDIT, 1, "Изменить");//groupId, itemId, order, title
                menu.add(0, DELETE, 2, "Удалить");

                for (int i = 0; i < menu.size(); i++) {
                    menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case EDIT: {
                                    Intent editHomework = new Intent(view.getContext(), NewHomeworkActivity.class);
                                    editHomework.putExtra("id", holder.id);
                                    editHomework.putExtra("subject", holder.subject.getText());
                                    editHomework.putExtra("exercise", holder.exercise.getText());
                                    editHomework.putExtra("week", holder.time.getText());
                                    editHomework.putExtra("editing", true);
                                    view.getContext().startActivity(editHomework);
                                    break;
                                }
                                case PASSED: {

                                    break;
                                }
                                case DELETE: {
                                    Toast.makeText(view.getContext(), "d", Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                            return true;
                        }
                    });
                }

            }
        });
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_homework,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeworkRecyclerAdapter.ViewHolder holder, int position) {
        holder.id = dataSet.get(position).Id();
        holder.time.setText(Integer.toString(dataSet.get(position).RemainedDays()));
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
        String id;
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
