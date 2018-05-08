package com.assistant.albert.studentassistant.authentification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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
import com.assistant.albert.studentassistant.homework.NewHomeworkActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final EditText email = findViewById(R.id.registerEmail);
        final EditText password = findViewById(R.id.registerPassword);
        final EditText repeatPassword = findViewById(R.id.repeatPassword);
        Button register = findViewById(R.id.registerButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (password.getText().toString().isEmpty() ||
                        repeatPassword.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Заполните все поля!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email.getText().toString());
                if (!matcher.matches()) {
                    Toast.makeText(getApplicationContext(), "Введите корректный email!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Пароли не совпадают!!!", Toast.LENGTH_LONG).show();
                    return;
                }
                UserItem userItem = new UserItem(email.getText().toString(), password.getText().toString());

            }
        });

    }

    public void sendDataToServer(String url, UserItem userItem) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("email", userItem.Email());
            jsonObject.put("password", userItem.Password());
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
                RegistrationActivity.super.onBackPressed();
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
}

