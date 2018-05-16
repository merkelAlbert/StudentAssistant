package com.assistant.albert.studentassistant.instantinfo;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.assistant.albert.studentassistant.R;
import com.assistant.albert.studentassistant.Urls;
import com.assistant.albert.studentassistant.authentification.SessionManager;
import com.assistant.albert.studentassistant.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class InstantInfoFragment extends Fragment {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionManager session;
    private InstantInfoItem instantInfo;

    public void getInstantInfo(final String userId, final String url) {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + userId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (Integer.parseInt(response.getString("status")) == 404) {
                                Toast.makeText(getContext(), "Заполните информацию о себе", Toast.LENGTH_LONG).show();
                            } else if (Integer.parseInt(response.getString("status")) == 200) {
                                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.handleError(getActivity(), error);
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        session = new SessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        final String userId = user.get(SessionManager.KEY_ID);

        view = inflater.inflate(R.layout.fragment_instant_info, container, false);
        swipeRefreshLayout = view.findViewById(R.id.instantInfoContainer);

        swipeRefreshLayout.setColorSchemeColors(view.getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getInstantInfo(userId, Urls.instantInfo);

                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        if (instantInfo == null)
            getInstantInfo(userId, Urls.instantInfo);

        return view;
    }
}
