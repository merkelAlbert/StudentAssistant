package com.assistant.albert.studentassistant.authentification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.assistant.albert.studentassistant.authentification.LoginActivity;
import com.assistant.albert.studentassistant.schedule.ScheduleItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SessionManager {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "StudentAssistant";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_ID = "Id";
    public static final String KEY_SUBJECTS = "Subjects";
    public static final String KEY_SCHEDULE = "Schedule";

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
        editor.apply();
    }

    public void createLoginSession(String email, String id) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ID, id);

        editor.putString(KEY_SUBJECTS, null);
        editor.putString(KEY_SCHEDULE, null);
        editor.commit();
    }

    public void add(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void addSet(String key, ArrayList<String> list) {
        Set<String> set = new HashSet<>();
        set.addAll(list);
        editor.putStringSet(key, set);
        editor.commit();
    }


    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_EMAIL, preferences.getString(KEY_EMAIL, null));
        user.put(KEY_ID, preferences.getString(KEY_ID, null));
        return user;
    }

    public ArrayList<String> getUserSubjects() {
        return new ArrayList<>(preferences.getStringSet(KEY_SUBJECTS, new HashSet<String>()));
    }

    public String getUSerSchedule(){
        return preferences.getString(KEY_SCHEDULE,"");
    }

    public void checkLogin(Activity activity) {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(i);
            activity.finish();
        }
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
