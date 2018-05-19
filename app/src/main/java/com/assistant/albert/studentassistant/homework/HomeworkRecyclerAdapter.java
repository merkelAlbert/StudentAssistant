package com.assistant.albert.studentassistant.homework;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.provider.SyncStateContract;
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
import com.assistant.albert.studentassistant.Urls;
import com.assistant.albert.studentassistant.authentification.SessionManager;
import com.assistant.albert.studentassistant.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeworkRecyclerAdapter extends RecyclerView.Adapter<HomeworkRecyclerAdapter.ViewHolder> {

    private final boolean[] firstCardSelected = {false};
    private ArrayList<HomeworkItem> dataSet;
    private ArrayList<HomeworkRecyclerAdapter.ViewHolder> selectedHomework;
    private View view;
    private SessionManager session;

    private final int EDIT = 0;
    private final int PASSED = 1;
    private final int DELETE = 2;

    public HomeworkRecyclerAdapter(ArrayList<HomeworkItem> dataSet) {
        this.dataSet = dataSet;
        this.selectedHomework = new ArrayList<>();
    }


    private void handleClick(View view, final HomeworkRecyclerAdapter.ViewHolder holder, final int color) {
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (selectedHomework.size() == 0) {
                    holder.cardView.setCardBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                    holder.selected = true;
                    selectedHomework.add(holder);
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
                    if (selectedHomework.size() > 0) {
                        if (!holder.selected) {
                            holder.selected = true;
                            selectedHomework.add(holder);
                            holder.cardView.setCardBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                        } else {
                            holder.selected = false;
                            selectedHomework.remove(holder);
                            holder.cardView.setCardBackgroundColor(color);
                        }
                    } else {
                        firstCardSelected[0] = false;
                    }
                } else {
                    selectedHomework.add(holder);
                    holder.cardView.showContextMenu();
                }
            }
        });


        holder.cardView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, final View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                menu.setHeaderTitle("Домашнее задание");
                menu.add(0, PASSED, 0, "Сдано");
                if (selectedHomework.size() == 0 || selectedHomework.size() == 1)
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
                                    editHomework.putExtra("week", holder.week);
                                    editHomework.putExtra("editing", true);
                                    view.getContext().startActivity(editHomework);
                                    break;
                                }
                                case PASSED: {
                                    session = new SessionManager(view.getContext());
                                    HashMap<String, String> user = session.getUserDetails();
                                    final String userId = user.get(SessionManager.KEY_USER_ID);
                                    JSONArray jsonArray = new JSONArray();
                                    try {
                                        for (int i = 0; i < selectedHomework.size(); i++) {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("id", selectedHomework.get(i).id);
                                            jsonObject.put("userId", userId);
                                            jsonObject.put("subject", selectedHomework.get(i)
                                                    .subject.getText().toString());
                                            jsonObject.put("exercise", selectedHomework.get(i)
                                                    .exercise.getText().toString());
                                            jsonObject.put("week", selectedHomework.get(i).week);
                                            jsonObject.put("remainedDays", selectedHomework.get(i)
                                                    .remainedDays.getText().toString());
                                            jsonObject.put("passed", true);

                                            jsonArray.put(i, jsonObject);
                                        }
                                        Utils.passHomework(view.getContext(), Urls.passHomework, jsonArray);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
        holder.remainedDays.setBackground(gd);
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
        holder.remainedDays.setText(Integer.toString(dataSet.get(position).RemainedDays()));
        holder.exercise.setText(dataSet.get(position).Exercise());
        holder.subject.setText(dataSet.get(position).Subject());
        holder.selected = false;
        holder.week = dataSet.get(position).Week();
        setColor(view, holder, Integer.parseInt(holder.remainedDays.getText().toString()));
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
        TextView remainedDays;
        String id;
        int week;
        boolean selected;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.homeworkCard);
            subject = itemView.findViewById(R.id.subject);
            exercise = itemView.findViewById(R.id.exercise);
            remainedDays = itemView.findViewById(R.id.remainedDays);
        }
    }

}
