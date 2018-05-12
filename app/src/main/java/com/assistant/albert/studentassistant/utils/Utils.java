package com.assistant.albert.studentassistant.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.assistant.albert.studentassistant.MainActivity;
import com.assistant.albert.studentassistant.R;
import com.assistant.albert.studentassistant.Urls;
import com.assistant.albert.studentassistant.authentification.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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
                                   final ProgressBar spinner, final String url, final JSONObject data){
        setupViews(activity, button, spinner);
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                try {
                    Toast.makeText(activity.getApplicationContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity.getApplicationContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity.getApplicationContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
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
                             final ProgressBar spinner, final String url, final JSONObject data){
        setupViews(activity, button, spinner);
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                button.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                try {
                    Toast.makeText(activity.getApplicationContext(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                    if (Integer.parseInt(response.get("status").toString()) == 200) {
                        SessionManager session = new SessionManager(activity.getApplicationContext());
                        session.createLoginSession(data.getString("email"),response.getString("id"));
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

    private static void handleError(Activity activity, VolleyError error) {
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

    private static void setupViews(Activity activity, Button button, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setX(button.getX() + button.getWidth() / 2 - activity.getResources().getDimension(R.dimen.progress_bar_size) / 2);
        progressBar.setY(button.getY());
        button.setVisibility(View.GONE);
    }


}
