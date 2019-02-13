package com.assistant.albert.studentassistant.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.assistant.albert.studentassistant.MainActivity;
import com.assistant.albert.studentassistant.R;
import com.assistant.albert.studentassistant.authentification.SessionManager;
import com.assistant.albert.studentassistant.homework.HomeworkItem;
import com.assistant.albert.studentassistant.homework.HomeworkRecyclerAdapter;
import com.assistant.albert.studentassistant.instantinfo.InstantInfoItem;
import com.assistant.albert.studentassistant.schedule.ClassSchedule;
import com.assistant.albert.studentassistant.schedule.DaySchedule;
import com.assistant.albert.studentassistant.schedule.ScheduleItem;
import com.assistant.albert.studentassistant.teachers.TeachersItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static String md5(String in) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void newSchedule(final Activity activity, final Button button,
                                   final ProgressBar spinner, final String url, final JSONObject data) {
        setupViews(activity, button, spinner);
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                try {
                    if (!response.isNull("message")) {
                        Toast.makeText(activity.getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    if (Integer.parseInt(response.get("status").toString()) == 200) {
                        Toast.makeText(activity.getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                handleError(activity, error);
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

    public static void newTeachers(final Activity activity, final Button button,
                                   final ProgressBar spinner, final String url, final JSONObject data) {
        setupViews(activity, button, spinner);
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                try {
                    if (!response.isNull("message")) {
                        Toast.makeText(activity.getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    if (Integer.parseInt(response.get("status").toString()) == 200) {
                        Toast.makeText(activity.getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                handleError(activity, error);
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

    public static void passHomework(final Context context, final String url,
                                    final JSONArray data, final ArrayList<HomeworkItem> all,
                                    final ArrayList<HomeworkItem> passed,
                                    final boolean[] firstCardSelected,
                                    final HomeworkRecyclerAdapter adapter) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url,
                data, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (!response.getJSONObject(0).isNull("message")) {
                        Toast.makeText(context, response.getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();
                    }
                    if (Integer.parseInt(response.getJSONObject(0).get("status").toString()) == 200) {
                        firstCardSelected[0] = false;
                        all.removeAll(passed);
                        adapter.setData(all);
                        passed.clear();
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError((Activity) context, error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }


    public static void deleteHomework(final Context context, final String url,
                                      final JSONArray data, final ArrayList<HomeworkItem> all,
                                      final ArrayList<HomeworkItem> deleted,
                                      final boolean[] firstCardSelected,
                                      final HomeworkRecyclerAdapter adapter) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url,
                data, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (!response.getJSONObject(0).isNull("message")) {
                        Toast.makeText(context, response.getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();
                    }
                    if (Integer.parseInt(response.getJSONObject(0).get("status").toString()) == 200) {
                        firstCardSelected[0] = false;
                        all.removeAll(deleted);
                        adapter.setData(all);
                        deleted.clear();
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError((Activity) context, error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(jsonArrayRequest);
    }


    public static void clear(final Activity activity, final SessionManager session, final ArrayList<String> keys, final String url) {
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (!response.isNull("message")) {
                                Toast.makeText(activity.getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                if (Integer.parseInt(response.get("status").toString()) == 200) {
                                    if (keys.size() == 0) {
                                        session.logoutUser();
                                    } else
                                        for (String key : keys) {
                                            session.add(key, null);
                                        }
                                    Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    activity.startActivity(intent);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.handleError(activity, error);
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }


    public static void newInstantInfo(final Activity activity, final Button button,
                                      final ProgressBar spinner, final String url, final JSONObject data) {
        setupViews(activity, button, spinner);
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                try {
                    if (!response.isNull("message")) {
                        Toast.makeText(activity.getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    if (Integer.parseInt(response.get("status").toString()) == 200) {
                        Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                handleError(activity, error);

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

    public static void newHomework(final Activity activity, final Button button,
                                   final ProgressBar spinner, final String url, final JSONObject data) {
        setupViews(activity, button, spinner);
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                try {
                    if (!response.isNull("message")) {
                        Toast.makeText(activity.getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    if (Integer.parseInt(response.get("status").toString()) == 200) {
                        Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                handleError(activity, error);
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

    public static void register(final Activity activity, final Button button,
                                final ProgressBar spinner, final String url, final JSONObject data) {
        setupViews(activity, button, spinner);
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                try {
                    if (!response.isNull("message")) {
                        Toast.makeText(activity.getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    if (Integer.parseInt(response.get("status").toString()) == 200) {
                        activity.onBackPressed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                handleError(activity, error);
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

    public static void login(final Activity activity, final Button button,
                             final ProgressBar spinner, final String url, final JSONObject data) {
        setupViews(activity, button, spinner);
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                try {
                    if (!response.isNull("message")) {
                        Toast.makeText(activity.getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    if (Integer.parseInt(response.get("status").toString()) == 200) {
                        SessionManager session = new SessionManager(activity.getApplicationContext());
                        session.createLoginSession(data.getString("email"), response.getString("id"));
                        Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i);
                        activity.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                handleError(activity, error);
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

    public static void handleError(Activity activity, VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(activity.getApplicationContext(), "Сервер недоступен", Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {

            Toast.makeText(activity.getApplicationContext(), "Ошибка аутентификации", Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(activity.getApplicationContext(), "Внутрення ошибка сервера", Toast.LENGTH_LONG).show();

        } else if (error instanceof NetworkError) {
            Toast.makeText(activity.getApplicationContext(), "Ошибка сети", Toast.LENGTH_LONG).show();

        } else if (error instanceof ParseError) {
            Toast.makeText(activity.getApplicationContext(), "Введены неверные данные", Toast.LENGTH_LONG).show();
        }
    }

    public static void setupViews(Activity activity, Button button, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setX(button.getX() + button.getWidth() / 2 - activity.getResources().getDimension(R.dimen.progress_bar_size) / 2);
        progressBar.setY(button.getY());
        button.setVisibility(View.GONE);
    }

    public static TeachersItem getTeachersFromJson(JSONObject jsonObject) {
        TeachersItem teachersItem = new TeachersItem();

        try {
//            JSONObject teachersJson = jsonObject.getJSONObject("teachers");
            JSONArray teachersArray = jsonObject.getJSONArray("teachers");
            JSONArray subjectsArray = jsonObject.getJSONArray("subjects");
            List<String> teachers = new ArrayList<>();
            List<String> subjects = new ArrayList<>();
            for (int i = 0; i < subjectsArray.length(); i++) {
                teachers.add(teachersArray.getString(i));
                subjects.add(subjectsArray.getString(i));
            }
            teachersItem = new TeachersItem(
                    jsonObject.getString("id"),
                    subjects,
                    teachers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teachersItem;
    }

    public static InstantInfoItem getInstantInfoItemFromJson(JSONObject jsonObject) {
        InstantInfoItem instantInfo = new InstantInfoItem();

        try {
            JSONObject iiJson = jsonObject.getJSONObject("userInfo");
            instantInfo = new InstantInfoItem(
                    iiJson.getString("id"),
                    iiJson.getString("userId"),
                    iiJson.getString("userName"),
                    iiJson.getString("group"),
                    iiJson.getString("startDate"),
                    jsonObject.getInt("currentWeek"),
                    jsonObject.getInt("totalHomework"),
                    jsonObject.getInt("totalPassed"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return instantInfo;
    }

    public static ScheduleItem getScheduleFromJson(JSONObject jsonObject) {
        ScheduleItem schedule = new ScheduleItem();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("schedule");
            for (int i = 0; i < jsonArray.length(); i++) {
                DaySchedule daySchedule = new DaySchedule();
                JSONArray dayJsonArray = jsonArray.getJSONArray(i);
                for (int j = 0; j < dayJsonArray.length(); j++) {
                    ClassSchedule classSchedule = new ClassSchedule();
                    JSONArray classJsonArray = dayJsonArray.getJSONArray(j);
                    for (int k = 0; k < classJsonArray.length(); k++) {
                        classSchedule.Schedule().add(classJsonArray.getString(k));
                    }
                    daySchedule.Schedule().add(classSchedule.Schedule());
                }
                schedule.Schedule().add(daySchedule.Schedule());
            }
            schedule.setId(jsonObject.getString("id"));
            schedule.setUserId(jsonObject.getString("userId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return schedule;
    }

}
