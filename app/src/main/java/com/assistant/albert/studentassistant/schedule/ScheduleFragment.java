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
import com.assistant.albert.studentassistant.utils.Utils;

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

    public void getSchedule(final String userId, final String url) {


        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + userId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        spinner.setVisibility(View.GONE);
                        scheduleItem = new ScheduleItem();
                        ArrayList<String> subjectsList = new ArrayList<>();
                        try {
                            if (!response.isNull("message")) {
                                Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            if (Integer.parseInt(response.getString("status")) == 404) {
                                Snackbar.make(view, "Заполните расписание", Snackbar.LENGTH_INDEFINITE)
                                        .setActionTextColor(getResources().getColor(R.color.colorAccent))
                                        .setAction("Заполнить", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent i = new Intent(getContext(), NewScheduleActivity.class);
                                                startActivity(i);
                                            }
                                        }).show();
                            } else if (Integer.parseInt(response.getString("status")) == 200) {
                                JSONArray subjects = response.getJSONArray("subjects");
                                Integer currentDay = response.getInt("currentDay");
                                for (int i = 0; i < subjects.length(); i++) {
                                    subjectsList.add(subjects.getString(i));
                                }
                                scheduleItem = Utils.getScheduleFromJson(response.getJSONObject("schedule"));
                                scheduleItem.SetCurrentDay(currentDay);
                                session.add(SessionManager.KEY_SCHEDULE, response.getJSONObject("schedule").toString());
                                session.add(SessionManager.KEY_TEACHERS, response.getJSONObject("teachers").toString());
                                session.addInt(SessionManager.KEY_CURRENT_DAY, currentDay);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        session.addSet(SessionManager.KEY_SUBJECTS, subjectsList);
                        RecyclerView.Adapter adapter = new ScheduleRecyclerAdapter(scheduleItem);
                        recyclerView.setAdapter(adapter);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.handleError(getActivity(), error);
                        if (!session.getUserSchedule().isEmpty()) {
                            spinner.setVisibility(View.GONE);
                            try {
                                scheduleItem = Utils.getScheduleFromJson(new JSONObject(session.getUserSchedule()));
                                scheduleItem.SetCurrentDay(session.getCurrentDay());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            RecyclerView.Adapter adapter = new ScheduleRecyclerAdapter(scheduleItem);
                            recyclerView.setAdapter(adapter);
                        } else {
                            spinner.setVisibility(View.GONE);
                            reloadButton.setVisibility(View.VISIBLE);
                            reloadButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    reloadButton.setVisibility(View.GONE);
                                    spinner.setVisibility(View.VISIBLE);
                                    getSchedule(userId, url);
                                }
                            });
                        }
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

        view = inflater.inflate(R.layout.fragment_schedule, container, false);

        recyclerView = view.findViewById(R.id.scheduleRecycler);
        spinner = view.findViewById(R.id.progressBar);
        reloadButton = view.findViewById(R.id.reloadButton);
        scheduleSwipeRefreshLayout = view.findViewById(R.id.scheduleSwipeRefreshLayout);

        reloadButton.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        if (scheduleItem == null) {
            spinner.setVisibility(View.VISIBLE);
            getSchedule(userId, Urls.schedule);
        }

        scheduleSwipeRefreshLayout.setColorSchemeColors(view.getResources().getColor(R.color.colorPrimary));

        scheduleSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scheduleSwipeRefreshLayout.setRefreshing(true);
                scheduleItem = null;
                getSchedule(userId, Urls.schedule);

                scheduleSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        scheduleSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}