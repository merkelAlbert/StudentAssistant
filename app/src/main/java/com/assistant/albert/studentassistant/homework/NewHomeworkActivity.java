package com.assistant.albert.studentassistant.homework;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewHomeworkActivity extends Activity {


    boolean isEditing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_homework);

        final EditText addSubject = findViewById(R.id.addSubject);
        final EditText addExercise = findViewById(R.id.addExercise);
        final EditText addWeek = findViewById(R.id.addWeek);
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
                    HomeworkItem homeworkItem = new HomeworkItem(id, addSubject.getText().toString(),
                            addExercise.getText().toString(), Integer.parseInt(addWeek.getText().toString()));
                    if (isEditing) {
                        SendDataToServer(Urls.changeHomework, homeworkItem);
                    } else {
                        SendDataToServer(Urls.addHomework, homeworkItem);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Заполните правильно все поля.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void SendDataToServer(String url, final HomeworkItem homeworkItem) {

        JSONObject jsonObject = new JSONObject();
        try {
            if (isEditing) {
                jsonObject.put("id", homeworkItem.Id());
            }
            jsonObject.put("subject", homeworkItem.Subject());
            jsonObject.put("exercise", homeworkItem.Exercise());
            jsonObject.put("time", Integer.toString(homeworkItem.Time()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NewHomeworkActivity.super.onBackPressed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "Сервер недоступен", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {

                    Toast.makeText(getApplicationContext(), "Ошибка аутентификации", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Внутрення ошибка сервера", Toast.LENGTH_LONG).show();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "Ошибка сети", Toast.LENGTH_LONG).show();

                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "Введены неверные данные", Toast.LENGTH_LONG).show();

                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        queue.add(jsonObjectRequest);
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
