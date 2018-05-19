package com.assistant.albert.studentassistant.instantinfo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class InstantInfoFragment extends Fragment {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionManager session;
    private InstantInfoItem instantInfo;
    private TextView currentWeek;
    private TextView userName;
    private TextView group;
    private TextView totalHomework;


    public void getInstantInfo(final String userId, final String url) {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + userId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.isNull("message")) {
                                Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            if (Integer.parseInt(response.getString("status")) == 200) {
                                JSONObject jsonObject = response.getJSONObject("userInfo");

                                instantInfo = new InstantInfoItem(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("userId"),
                                        jsonObject.getString("userName"),
                                        jsonObject.getString("group"),
                                        jsonObject.getString("startDate"),
                                        response.getInt("currentWeek"),
                                        response.getInt("totalHomework")
                                );
                                session.add(SessionManager.KEY_INSTANT_INFO, response.toString());
                                String weekStr = "Текущая неделя: " + String.valueOf(instantInfo.CurrentWeek());
                                String totalHomeworkStr = "Всего ДЗ: " + String.valueOf(instantInfo.TotalHomework());
                                currentWeek.setText(weekStr);
                                userName.setText(instantInfo.UserName());
                                group.setText(instantInfo.Group());
                                totalHomework.setText(totalHomeworkStr);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!session.getInstantInfo().isEmpty()) {
                            try {
                                instantInfo = Utils.getInstantInfoItemFromJson(new JSONObject(session.getInstantInfo()));
                                String weekStr = "Текущая неделя: " + String.valueOf(instantInfo.CurrentWeek());
                                String totalHomeworkStr = "Всего ДЗ: " + String.valueOf(instantInfo.TotalHomework());
                                currentWeek.setText(weekStr);
                                userName.setText(instantInfo.UserName());
                                group.setText(instantInfo.Group());
                                totalHomework.setText(totalHomeworkStr);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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
        final String userId = user.get(SessionManager.KEY_USER_ID);

        view = inflater.inflate(R.layout.fragment_instant_info, container, false);
        currentWeek = view.findViewById(R.id.currentWeek);
        userName = view.findViewById(R.id.userName);
        group = view.findViewById(R.id.group);
        totalHomework = view.findViewById(R.id.totalHomework);


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
        if (instantInfo == null) {
            getInstantInfo(userId, Urls.instantInfo);
        }
        return view;
    }
}
