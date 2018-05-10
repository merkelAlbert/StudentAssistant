package com.assistant.albert.studentassistant.authentification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.assistant.albert.studentassistant.authentification.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "StudentAssistant";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_ID = "Id";
    public static final String KEY_SCHEDULE="Schedule";

    public SessionManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void createLoginSession(String email, String id){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ID, id);
        editor.commit();
    }

    public void add(String key, String value){
        editor.putString(key,value);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_EMAIL, preferences.getString(KEY_EMAIL, null));
        user.put(KEY_ID, preferences.getString(KEY_ID, null));
        return user;
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
