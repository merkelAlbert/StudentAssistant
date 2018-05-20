package com.assistant.albert.studentassistant.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.assistant.albert.studentassistant.R;
import com.assistant.albert.studentassistant.Urls;
import com.assistant.albert.studentassistant.authentification.SessionManager;
import com.assistant.albert.studentassistant.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NewScheduleActivity extends AppCompatActivity {

    private final int ONEDAYSUBJECTSNUMBER = 7;
    private SessionManager session;
    private boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_schedule);
        final LinearLayout container = findViewById(R.id.addScheduleContainer);
        final Button submitButton = findViewById(R.id.submitSchedule);
        final ProgressBar spinner = findViewById(R.id.progressBar);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String userId = user.get(SessionManager.KEY_USER_ID);
        spinner.setVisibility(View.GONE);
        isEditing = getIntent().getBooleanExtra("isEditing", false);
        if (isEditing) {
            ScheduleItem currentSchedule = new ScheduleItem();
            if (!session.getUserSchedule().isEmpty()) {
                try {
                    currentSchedule = Utils.getScheduleFromJson(new JSONObject(session.getUserSchedule()));
                    setSchedule(container, currentSchedule);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            saveSchedule(container);
        }
        submitButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                ScheduleItem newSchedule = getSchedule(container);
                JSONObject jsonObject = new JSONObject();
                try {
                    JSONArray scheduleArray = new JSONArray();
                    for (int i = 0; i < newSchedule.Schedule().size(); i++) {
                        DaySchedule daySchedule = new DaySchedule();
                        daySchedule.Schedule().addAll(newSchedule.Schedule().get(i));
                        JSONArray dayArray = new JSONArray();
                        for (int j = 0; j < daySchedule.Schedule().size(); j++) {
                            ClassSchedule classSchedule = new ClassSchedule();
                            classSchedule.Schedule().addAll(daySchedule.Schedule().get(j));
                            JSONArray classArray = new JSONArray();
                            for (int k = 0; k < classSchedule.Schedule().size(); k++) {
                                classArray.put(classSchedule.Schedule().get(k));
                            }
                            dayArray.put(classArray);
                        }
                        scheduleArray.put(dayArray);
                    }
                    if (isEditing)
                        jsonObject.put("id", Utils.getScheduleFromJson(new JSONObject(session.getUserSchedule())).Id());
                    jsonObject.put("schedule", scheduleArray);
                    jsonObject.put("userId", userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isEditing)
                    Utils.newSchedule(NewScheduleActivity.this, submitButton, spinner, Urls.changeSchedule, jsonObject);
                else
                    Utils.newSchedule(NewScheduleActivity.this, submitButton, spinner, Urls.addSchedule, jsonObject);
            }
        });
    }


    private DaySchedule trimSchedule(DaySchedule daySchedule) {
        DaySchedule temp = new DaySchedule();
        int lastIndex = daySchedule.Schedule().size();
        for (int i = daySchedule.Schedule().size() - 1; i >= 0; i--) {
            ClassSchedule classSchedule = new ClassSchedule();
            classSchedule.Schedule().addAll(daySchedule.Schedule().get(i));
            if (classSchedule.Schedule().get(0).equals("-") && classSchedule.Schedule().get(1).equals("-")) {
                lastIndex = i;
                continue;
            }
            break;
        }
        for (int i = 0; i < lastIndex; i++) {
            temp.Schedule().add(daySchedule.Schedule().get(i));
        }
        return temp;
    }

    private ScheduleItem getSchedule(ViewGroup container) {
        ScheduleItem scheduleItem = new ScheduleItem();
        DaySchedule daySchedule;
        for (int i = 0; i < container.getChildCount(); i++) {
            LinearLayout scheduleOfDay = container.getChildAt(i).findViewById(R.id.scheduleOfDay);

            daySchedule = new DaySchedule();
            for (int j = 0; j < scheduleOfDay.getChildCount(); j++) {
                ClassSchedule classSchedule = new ClassSchedule();

                EditText numerator = scheduleOfDay.getChildAt(j).findViewById(R.id.numerator);
                EditText denominator = scheduleOfDay.getChildAt(j).findViewById(R.id.denominator);
                if (numerator.getText().toString().isEmpty()) {
                    classSchedule.Schedule().add("-");
                } else {
                    classSchedule.Schedule().add(numerator.getText().toString());
                }
                if (denominator.getText().toString().isEmpty()) {
                    classSchedule.Schedule().add("-");
                } else {
                    classSchedule.Schedule().add(denominator.getText().toString());
                }
                daySchedule.Schedule().add(classSchedule.Schedule());
            }

            DaySchedule trimmed = new DaySchedule();
            trimmed.Schedule().addAll(trimSchedule(daySchedule).Schedule());

            scheduleItem.Schedule().add(trimmed.Schedule());
        }
        return scheduleItem;
    }

    private void saveSchedule(ViewGroup container) {
        final ArrayList<View> days = new ArrayList<>();

        for (int i = 0; i < ScheduleDays.list.size(); i++) {
            View dayLayout = getLayoutInflater().
                    inflate(R.layout.content_schedule, container, false);

            LinearLayout day = dayLayout.findViewById(R.id.scheduleOfDay);
            TextView weekDay = dayLayout.findViewById(R.id.weekDay);

            weekDay.setText(ScheduleDays.list.get(i).toString());

            ArrayList<View> subjects = new ArrayList<>();
            for (int j = 0; j < ONEDAYSUBJECTSNUMBER; j++) {
                View subjectLayout = getLayoutInflater().
                        inflate(R.layout.add_schedule_subject, day, false);
                TextView subjectNumber = subjectLayout.findViewById(R.id.subjectNumber);
                subjectNumber.setText(Integer.toString(j + 1));
                if (i == 0 && j == 0) {
                    EditText numerator = subjectLayout.findViewById(R.id.numerator);
                    numerator.setHint("Числитель");
                    EditText denominator = subjectLayout.findViewById(R.id.denominator);
                    denominator.setHint("Знаменатель");
                }
                subjects.add(subjectLayout);
            }

            for (int k = 0; k < subjects.size(); k++) {
                day.addView(subjects.get(k));
            }
            days.add(dayLayout);
        }
        for (int i = 0; i < days.size(); i++) {
            container.addView(days.get(i));
        }
    }

    private void setSchedule(ViewGroup container, ScheduleItem schedule) {
        final ArrayList<View> days = new ArrayList<>();

        for (int i = 0; i < ScheduleDays.list.size(); i++) {
            for (int j = schedule.Schedule().get(i).size(); j < ONEDAYSUBJECTSNUMBER; j++) {
                ClassSchedule classSchedule = new ClassSchedule();
                classSchedule.Schedule().add("");
                classSchedule.Schedule().add("");
                schedule.Schedule().get(i).add(classSchedule.Schedule());
            }
        }


        for (int i = 0; i < ScheduleDays.list.size(); i++) {
            View dayLayout = getLayoutInflater().
                    inflate(R.layout.content_schedule, container, false);

            LinearLayout day = dayLayout.findViewById(R.id.scheduleOfDay);
            TextView weekDay = dayLayout.findViewById(R.id.weekDay);

            weekDay.setText(ScheduleDays.list.get(i).toString());

            ArrayList<View> subjects = new ArrayList<>();
            for (int j = 0; j < ONEDAYSUBJECTSNUMBER; j++) {
                View subjectLayout = getLayoutInflater().
                        inflate(R.layout.add_schedule_subject, day, false);
                TextView subjectNumber = subjectLayout.findViewById(R.id.subjectNumber);
                subjectNumber.setText(Integer.toString(j + 1));
                EditText numerator = subjectLayout.findViewById(R.id.numerator);
                EditText denominator = subjectLayout.findViewById(R.id.denominator);

                if (!schedule.Schedule().get(i).get(j).get(0).equals("-"))
                    numerator.setText(schedule.Schedule().get(i).get(j).get(0));
                else
                    numerator.setText("");
                if (!schedule.Schedule().get(i).get(j).get(1).equals("-"))
                    denominator.setText(schedule.Schedule().get(i).get(j).get(1));
                else denominator.setText("");
                subjects.add(subjectLayout);
            }

            for (int k = 0; k < subjects.size(); k++) {
                day.addView(subjects.get(k));
            }
            days.add(dayLayout);
        }
        for (int i = 0; i < days.size(); i++) {
            container.addView(days.get(i));
        }

    }
}
