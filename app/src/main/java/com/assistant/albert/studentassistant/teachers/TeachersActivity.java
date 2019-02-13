package com.assistant.albert.studentassistant.teachers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.assistant.albert.studentassistant.R;
import com.assistant.albert.studentassistant.Urls;
import com.assistant.albert.studentassistant.authentification.SessionManager;
import com.assistant.albert.studentassistant.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeachersActivity extends AppCompatActivity {
    TeachersItem teachersItem;
    SessionManager session;
    Button submitButton;
    ProgressBar spinner;
    LinearLayout teachersContainer;
    EditText teacherEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String userId = user.get(SessionManager.KEY_USER_ID);
        final ArrayList<String> subjects = session.getUserSubjects();
        try {
            teachersItem = Utils.getTeachersFromJson(new JSONObject(session.getUserTeachers()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        submitButton = findViewById(R.id.submitTeachers);
        teachersContainer = findViewById(R.id.teachersContainer);
        spinner = findViewById(R.id.progressBar);
        for (int i = 0; i < subjects.size(); i++) {
            teacherEditText = (EditText) LayoutInflater.from(getApplicationContext()).
                    inflate(R.layout.content_teachers, teachersContainer, false);
            teacherEditText.setHint(subjects.get(i));
            int index = teachersItem.Subjects().indexOf(subjects.get(i));
            if (index != -1) {
                teacherEditText.setText(teachersItem.Teachers().get(index));
            }
            teachersContainer.addView(teacherEditText);
        }
        spinner.setVisibility(View.GONE);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeachersItem teachers = getTeachers(teachersContainer);
                JSONObject jsonObject = new JSONObject();
                JSONArray teachersJsonArray = new JSONArray();
                JSONArray subjectsJsonArray = new JSONArray();
                for (int i = 0; i < teachers.Subjects().size(); i++) {
                    if (!teachers.Teachers().get(i).isEmpty()) {
                        teachersJsonArray.put(teachers.Teachers().get(i));
                        subjectsJsonArray.put(teachers.Subjects().get(i));
                    }
                }
                try {
                    jsonObject.put("id", teachersItem.Id());
                    jsonObject.put("userId", userId);
                    jsonObject.put("teachers", teachersJsonArray);
                    jsonObject.put("subjects", subjectsJsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.newTeachers(TeachersActivity.this, submitButton, spinner, Urls.changeTeachers, jsonObject);
            }
        });

    }

    private TeachersItem getTeachers(ViewGroup container) {
        TeachersItem item = new TeachersItem();
        List<String> subjects = session.getUserSubjects();
        List<String> teachers = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            EditText teacherEditText = (EditText) container.getChildAt(i);
            teachers.add(teacherEditText.getText().toString());
        }
        item.setSubjects(subjects);
        item.setTeachers(teachers);
        return item;
    }

}
