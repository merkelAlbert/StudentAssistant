package com.assistant.albert.studentassistant.schedule;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ScheduleFragment extends Fragment {

    private ScheduleItem scheduleItem;
    private View view;
    private ProgressBar spinner;
    private SwipeRefreshLayout scheduleSwipeRefreshLayout;
    private Button reloadButton;
    private RecyclerView recyclerView;
    private SessionManager session;

    public void setDataFromServer(final String userId, final String url) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + userId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        spinner.setVisibility(View.GONE);
                        scheduleItem = new ScheduleItem();
                        ArrayList<String> subjectsList = new ArrayList<>();
                        try {
                            if (Integer.parseInt(response.getString("status")) == 404) {
                                Snackbar.make(view, "Заполните расписание", Snackbar.LENGTH_INDEFINITE)
                                        .setActionTextColor(getResources().getColor(R.color.colorAccent))
                                        .setAction("Заполнить", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent i = new Intent(getContext(), AddScheduleActivity.class);
                                                startActivity(i);
                                            }
                                        }).show();
                            } else if (Integer.parseInt(response.getString("status")) == 200) {
                                JSONArray schedule = response.getJSONObject("schedule").getJSONArray("schedule");
                                JSONArray subjects = response.getJSONArray("subjects");
                                for (int i = 0; i < subjects.length(); i++) {
                                    subjectsList.add(subjects.getString(i));
                                }
                                for (int i = 0; i < schedule.length(); i++) {
                                    DaySchedule daySchedule = new DaySchedule();
                                    JSONArray dayJsonArray = schedule.getJSONArray(i);
                                    for (int j = 0; j < dayJsonArray.length(); j++) {
                                        ClassSchedule classSchedule = new ClassSchedule();
                                        JSONArray classJsonArray = dayJsonArray.getJSONArray(j);
                                        for (int k = 0; k < classJsonArray.length(); k++) {
                                            classSchedule.Schedule().add(classJsonArray.getString(k));
                                        }
                                        daySchedule.Schedule().add(classSchedule.Schedule());
                                    }
                                    scheduleItem.Schedule().add(daySchedule.Schedule());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        scheduleItem.setUserId(userId);
                        session.addSet(SessionManager.KEY_SUBJECTS,subjectsList);
                        RecyclerView.Adapter adapter = new ScheduleRecyclerAdapter(scheduleItem);
                        recyclerView.setAdapter(adapter);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.GONE);

                        reloadButton.setVisibility(View.VISIBLE);
                        reloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reloadButton.setVisibility(View.GONE);
                                spinner.setVisibility(View.VISIBLE);
                                setDataFromServer(userId, url);
                            }
                        });
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

        view = inflater.inflate(R.layout.fragment_schedule, container, false);

        recyclerView = view.findViewById(R.id.scheduleRecycler);
        spinner = view.findViewById(R.id.progressBar);
        reloadButton = view.findViewById(R.id.reloadButton);
        scheduleSwipeRefreshLayout = view.findViewById(R.id.scheduleSwipeRefreshLayout);

        reloadButton.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        scheduleSwipeRefreshLayout.setColorSchemeColors(view.getResources().getColor(R.color.colorPrimary));

        scheduleSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scheduleSwipeRefreshLayout.setRefreshing(true);
                scheduleItem.Schedule().clear();
                setDataFromServer(userId, Urls.schedule);

                scheduleSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        scheduleSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        if (scheduleItem == null) {
            this.setDataFromServer(userId, Urls.schedule);
        }
        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
