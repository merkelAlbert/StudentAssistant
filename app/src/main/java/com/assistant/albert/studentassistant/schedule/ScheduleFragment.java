package com.assistant.albert.studentassistant.schedule;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.assistant.albert.studentassistant.R;
import com.assistant.albert.studentassistant.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ScheduleFragment extends Fragment {

    private ScheduleResponse scheduleResponse;
    private View view;
    private ProgressBar spinner;
    private SwipeRefreshLayout scheduleSwipeRefreshLayout;
    private Button reloadButton;
    private RecyclerView recyclerView;

    public void setDataFromServer(String url) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        spinner.setVisibility(View.GONE);
                        scheduleResponse = new ScheduleResponse();
                        try {
                            JSONArray schedule = response.getJSONArray("schedule");
                            for (int i = 0; i < schedule.length(); i++) {
                                DaySchedule daySchedule = new DaySchedule();
                                JSONArray dayJsonArray = schedule.getJSONArray(i);
                                for (int j = 0; j < dayJsonArray.length(); j++) {
                                    ClassSchedule classSchedule = new ClassSchedule();
                                    JSONArray classJsonArray = dayJsonArray.getJSONArray(j);
                                    for (int k = 0; k < classJsonArray.length(); k++) {
                                        classSchedule.getClassSchedule().add(classJsonArray.getString(k));
                                    }
                                    daySchedule.getDaySchedule().add(classSchedule.getClassSchedule());
                                }
                                scheduleResponse.Schedule().add(daySchedule.getDaySchedule());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RecyclerView.Adapter adapter = new ScheduleRecyclerAdapter(scheduleResponse);
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
                                setDataFromServer(Urls.schedule);
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
        view = inflater.inflate(R.layout.fragment_schedule, container, false);

        recyclerView = view.findViewById(R.id.scheduleRecycler);
        spinner = view.findViewById(R.id.progressBar);
        reloadButton = view.findViewById(R.id.reloadButton);
        scheduleSwipeRefreshLayout = view.findViewById(R.id.scheduleOfDay);

        reloadButton.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        scheduleSwipeRefreshLayout.setColorSchemeColors(view.getResources().getColor(R.color.colorPrimary));

        scheduleSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scheduleSwipeRefreshLayout.setRefreshing(true);
                scheduleResponse.Schedule().clear();
                setDataFromServer(Urls.schedule);

                scheduleSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        scheduleSwipeRefreshLayout.setRefreshing(false);

                    }
                });
            }
        });
        if (scheduleResponse == null) {
            this.setDataFromServer(Urls.schedule);
        }
        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
