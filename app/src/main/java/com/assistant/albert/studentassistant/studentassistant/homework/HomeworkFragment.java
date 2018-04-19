package com.assistant.albert.studentassistant.studentassistant.homework;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.assistant.albert.studentassistant.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeworkFragment extends Fragment {

    public static Comparator<HomeworkResponse> TimeComparator = new Comparator<HomeworkResponse>() {
        @Override
        public int compare(HomeworkResponse homeworkResponse, HomeworkResponse t1) {
            return Integer.compare(homeworkResponse.Time(), t1.Time());
        }
    };
    private final ArrayList<HomeworkResponse> arrayList = new ArrayList<>();
    private View view;

    public void setDataFromServer(String url) {
        final RecyclerView recyclerView = view.findViewById(R.id.homeworkRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                arrayList.add(new HomeworkResponse(jsonObject.getString("subject"),
                                        jsonObject.getString("exercise"),
                                        jsonObject.getInt("time")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        Collections.sort(arrayList, new HomeworkComparator());
                        RecyclerView.Adapter adapter = new HomeworkAdapter(arrayList);
                        recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "Server is not responding", Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homework, container, false);
        if (arrayList.isEmpty()) {
            this.setDataFromServer("http://192.168.1.5:8888/Homework");
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
