package com.assistant.albert.studentassistant.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.assistant.albert.studentassistant.R;

import java.util.ArrayList;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity {

    private final int ONEDAYSUBJECTSNUMBER = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        final LinearLayout container = findViewById(R.id.addScheduleContainer);
        Button submitButton = findViewById(R.id.submitSchedule);


        saveSchedule(container);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleItem schedule = getSchedule(container);
                Toast.makeText(getApplicationContext(), schedule.Schedule().get(0).get(1).get(1).toString(), Toast.LENGTH_SHORT).show();
            }
        });

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

                classSchedule.Schedule().add(numerator.getText().toString());
                classSchedule.Schedule().add(denominator.getText().toString());
                daySchedule.Schedule().add(classSchedule.Schedule());
            }
            scheduleItem.Schedule().add(daySchedule.Schedule());
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
}
