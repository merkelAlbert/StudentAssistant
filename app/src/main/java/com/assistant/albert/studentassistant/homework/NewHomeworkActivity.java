package com.assistant.albert.studentassistant.homework;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.assistant.albert.studentassistant.R;
import com.assistant.albert.studentassistant.Urls;
import com.assistant.albert.studentassistant.authentification.SessionManager;
import com.assistant.albert.studentassistant.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewHomeworkActivity extends Activity {


    boolean isEditing;
    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_homework);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String userId = user.get(SessionManager.KEY_ID);

        final EditText addSubject = findViewById(R.id.addSubject);
        final EditText addExercise = findViewById(R.id.addExercise);
        final EditText addWeek = findViewById(R.id.addWeek);
        final ProgressBar spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        isEditing = getIntent().getStringExtra("editing").equals("true");
        String tempId = "";
        if (isEditing) {
            tempId = getIntent().getStringExtra("id");
            String subject = getIntent().getStringExtra("subject");
            String exercise = getIntent().getStringExtra("exercise");
            String week = getIntent().getStringExtra("week");
            addSubject.setText(subject);
            addExercise.setText(exercise);
            addWeek.setText(week);
        }

        final String id = tempId;
        final Button submitButton = findViewById(R.id.submitHomework);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWeekValid(addWeek.getText().toString())) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        if (isEditing) {
                            jsonObject.put("id", id);
                        }
                        jsonObject.put("userId", userId);
                        jsonObject.put("subject", addSubject.getText().toString());
                        jsonObject.put("exercise", addExercise.getText().toString());
                        jsonObject.put("time", addWeek.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (isEditing) {
                        Utils.newHomework(NewHomeworkActivity.this, submitButton, spinner, Urls.changeHomework, jsonObject);
                    } else {
                        Utils.newHomework(NewHomeworkActivity.this, submitButton, spinner, Urls.addHomework, jsonObject);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Заполните правильно все поля.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private boolean isWeekValid(String week) {
        try {
            Integer integer = Integer.parseInt(week);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
