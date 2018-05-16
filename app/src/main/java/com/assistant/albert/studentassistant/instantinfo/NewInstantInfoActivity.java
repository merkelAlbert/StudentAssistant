package com.assistant.albert.studentassistant.instantinfo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.assistant.albert.studentassistant.R;
import com.assistant.albert.studentassistant.authentification.SessionManager;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NewInstantInfoActivity extends AppCompatActivity {

    private EditText userName;
    private EditText group;
    private CalendarView startDate;
    private Button submit;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instant_info);
        userName = findViewById(R.id.userName);
        group = findViewById(R.id.group);
        startDate = findViewById(R.id.startDate);
        submit = findViewById(R.id.submitInstantInfo);

        final Date[] date = {new Date(startDate.getDate())};
        startDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Calendar calendar = new GregorianCalendar(year,month,day);
                date[0]=calendar.getTime();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
            }
        });
    }
}
