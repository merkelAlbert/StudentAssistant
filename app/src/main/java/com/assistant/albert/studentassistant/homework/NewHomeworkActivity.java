package com.assistant.albert.studentassistant.homework;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewHomeworkActivity extends AppCompatActivity {


    boolean isEditing;
    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_homework);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String userId = user.get(SessionManager.KEY_ID);

        final Spinner addSubjectSpinner = findViewById(R.id.addSubject);
        final EditText addExercise = findViewById(R.id.addExercise);
        final EditText addWeek = findViewById(R.id.addWeek);
        final ProgressBar spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        isEditing = getIntent().getBooleanExtra("editing", false);
        String tempId = "";
        ArrayList<String> subjects = session.getUserSubjects();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, subjects);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        addSubjectSpinner.setAdapter(spinnerAdapter);
        addSubjectSpinner.setPrompt("Предмет");
        if (isEditing) {
            tempId = getIntent().getStringExtra("id");
            String exercise = getIntent().getStringExtra("exercise");
            String week = getIntent().getStringExtra("week");
            String subject = getIntent().getStringExtra("subject");
            addSubjectSpinner.setSelection(getIndex(subjects, subject));
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
                        jsonObject.put("subject", addSubjectSpinner.getSelectedItem());
                        jsonObject.put("exercise", addExercise.getText().toString());
                        jsonObject.put("time", addWeek.getText().toString());
                        jsonObject.put("passed", false);
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

    private int getIndex(ArrayList<String> list, String subject) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(subject))
                return i;
        }
        return 0;
    }
}
