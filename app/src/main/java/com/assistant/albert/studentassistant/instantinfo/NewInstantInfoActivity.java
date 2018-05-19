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
import com.assistant.albert.studentassistant.Urls;
import com.assistant.albert.studentassistant.authentification.SessionManager;
import com.assistant.albert.studentassistant.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class NewInstantInfoActivity extends AppCompatActivity {

    private EditText userName;
    private EditText group;
    private CalendarView startDate;
    private Button submit;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();
        final String userId = user.get(SessionManager.KEY_USER_ID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instant_info);
        userName = findViewById(R.id.addUserName);
        group = findViewById(R.id.addGroup);
        startDate = findViewById(R.id.startDate);
        submit = findViewById(R.id.submitInstantInfo);


        InstantInfoItem instantInfo = new InstantInfoItem();
        Date dateFromStr = new Date();

        if (!session.getInstantInfo().isEmpty()) {
            try {
                instantInfo = Utils.getInstantInfoItemFromJson(new JSONObject(session.getInstantInfo()));
                try {
                    dateFromStr = InstantInfoItem.dateFormat.parse(instantInfo.StartDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                userName.setText(instantInfo.UserName());
                group.setText(instantInfo.Group());
                startDate.setDate(dateFromStr.getTime());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final String[] date = {InstantInfoItem.dateFormat.format(startDate.getDate())};
        startDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Calendar calendar = new GregorianCalendar(year, month, day);
                date[0] = InstantInfoItem.dateFormat.format(calendar.getTime());
            }
        });

        final InstantInfoItem finalInstantInfo = instantInfo;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), date[0], Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject();
                try {
                    if (!finalInstantInfo.Id().isEmpty())
                        jsonObject.put("id", finalInstantInfo.Id());
                    jsonObject.put("userId", userId);
                    jsonObject.put("userName", userName.getText().toString());
                    jsonObject.put("group", group.getText().toString());
                    jsonObject.put("startDate", date[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.newInstantInfo(NewInstantInfoActivity.this, Urls.changeInstantInfo, jsonObject);
            }
        });
    }
}
