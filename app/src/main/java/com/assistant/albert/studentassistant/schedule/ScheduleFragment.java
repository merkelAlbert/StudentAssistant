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
import android.widget.Toast;

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


    public void setDataFromServer(String url) {
        final RecyclerView recyclerView = view.findViewById(R.id.scheduleRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            scheduleResponse = new ScheduleResponse();
                            JSONArray schedule = response.getJSONArray("schedule");
                            for (int i = 0; i < schedule.length(); i++) {
                                DaySchedule daySchedule = new DaySchedule();
                                JSONArray dayJsonArray = schedule.getJSONArray(i);
                                for (int j = 0; j < dayJsonArray.length(); j++) {
                                    ClassChedule classChedule = new ClassChedule();
                                    JSONArray classJsonArray = dayJsonArray.getJSONArray(j);
                                    for (int k = 0; k < classJsonArray.length(); k++) {
                                        classChedule.getClassSchedule().add(classJsonArray.getString(k));
                                    }
                                    daySchedule.getDaySchedule().add(classChedule.getClassSchedule());
                                }
                                scheduleResponse.Schedule().add(daySchedule.getDaySchedule());
                            }
                            Toast.makeText(getActivity().getApplicationContext(),
                                    scheduleResponse.Schedule().get(1).get(1).get(1).toString(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "Server is not responding", Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);

        final SwipeRefreshLayout scheduleSwipeRefreshLayout = view.findViewById(R.id.scheduleContainer);
        scheduleSwipeRefreshLayout.setColorSchemeColors(view.getResources().getColor(R.color.colorPrimary));

        scheduleSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scheduleSwipeRefreshLayout.setRefreshing(true);
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
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Уже есть", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
