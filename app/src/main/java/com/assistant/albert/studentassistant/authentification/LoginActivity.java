package com.assistant.albert.studentassistant.authentification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.assistant.albert.studentassistant.R;
import com.assistant.albert.studentassistant.Urls;
import com.assistant.albert.studentassistant.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final Button enter = findViewById(R.id.enter);
        final ProgressBar spinner = findViewById(R.id.progressBar);
        final TextView register = findViewById(R.id.registerTextView);
        spinner.setVisibility(View.GONE);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Заполните все поля!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email.getText().toString());
                if (!matcher.matches()) {
                    Toast.makeText(getApplicationContext(), "Введите корректный email!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email.getText().toString());
                    jsonObject.put("password", Utils.md5(password.getText().toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.login(LoginActivity.this, enter, spinner, Urls.login, jsonObject);
            }
        });
    }
}
