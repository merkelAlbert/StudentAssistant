package com.assistant.albert.studentassistant.homework;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class HomeworkFragment extends Fragment {

    public static Comparator<HomeworkItem> TimeComparator = new Comparator<HomeworkItem>() {
        @Override
        public int compare(HomeworkItem homeworkItem, HomeworkItem t1) {
            return Integer.compare(homeworkItem.Week(), t1.Week());
        }
    };
    private ArrayList<HomeworkItem> arrayList;
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private ProgressBar spinner;
    private Button reloadButton;
    private SessionManager session;

    public void getHomework(final String userId, final String url) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recyclerView);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + userId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        spinner.setVisibility(View.GONE);
                        arrayList = new ArrayList<>();
                        try {
                            if (!response.isNull("message")) {
                                Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            if (response.getInt("status") == 200) {
                                JSONArray homeworkArray = response.getJSONArray("homework");
                                JSONArray remainedDaysArray = response.getJSONArray("remainedDays");
                                for (int i = 0; i < homeworkArray.length(); i++) {
                                    JSONObject homeworkObject = homeworkArray.getJSONObject(i);
                                    int remainedDays = remainedDaysArray.getInt(i);
                                    arrayList.add(new HomeworkItem(
                                            homeworkObject.getString("id"),
                                            userId,
                                            homeworkObject.getString("subject"),
                                            homeworkObject.getString("exercise"),
                                            homeworkObject.getInt("week"),
                                            remainedDays,
                                            homeworkObject.getBoolean("passed")));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Collections.sort(arrayList, new HomeworkComparator());
                        RecyclerView.Adapter adapter = new HomeworkRecyclerAdapter(arrayList);
                        recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.handleError(getActivity(), error);
                        spinner.setVisibility(View.GONE);
                        reloadButton.setVisibility(View.VISIBLE);
                        reloadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reloadButton.setVisibility(View.GONE);
                                spinner.setVisibility(View.VISIBLE);
                                getHomework(userId, url);
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
        final String userId = user.get(SessionManager.KEY_USER_ID);

        view = inflater.inflate(R.layout.fragment_homework, container, false);
        recyclerView = view.findViewById(R.id.homeworkRecycler);
        swipeRefreshLayout = view.findViewById(R.id.homeworkContainer);
        floatingActionButton = view.findViewById(R.id.homeworkFAB);
        spinner = view.findViewById(R.id.progressBar);
        reloadButton = view.findViewById(R.id.reloadButton);


        reloadButton.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newHomework = new Intent(getActivity(), NewHomeworkActivity.class);
                getActivity().startActivity(newHomework);

            }
        });

        swipeRefreshLayout.setColorSchemeColors(view.getResources().getColor(R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                arrayList.clear();
                getHomework(userId, Urls.homework);

                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        if (arrayList == null)
            this.getHomework(userId, Urls.homework);
        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
