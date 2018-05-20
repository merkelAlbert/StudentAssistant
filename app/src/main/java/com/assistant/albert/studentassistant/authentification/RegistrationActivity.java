package com.assistant.albert.studentassistant.authentification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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
import com.assistant.albert.studentassistant.homework.NewHomeworkActivity;
import com.assistant.albert.studentassistant.utils.Utils;

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
        final Button register = findViewById(R.id.registerButton);
        final ProgressBar spinner = findViewById(R.id.progressBar);

        spinner.setVisibility(View.GONE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().trim().isEmpty() ||
                        repeatPassword.getText().toString().trim().isEmpty() || email.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Заполните все поля!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim());
                if (!matcher.matches()) {
                    Toast.makeText(getApplicationContext(), "Введите корректный email!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.getText().toString().equals(repeatPassword.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Пароли не совпадают!!!", Toast.LENGTH_LONG).show();
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email.getText().toString().trim());
                    jsonObject.put("password", Utils.md5(password.getText().toString().trim()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.register(RegistrationActivity.this, register, spinner, Urls.register, jsonObject);

            }
        });
    }
}

