package com.assistant.albert.studentassistant.instantinfo;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.assistant.albert.studentassistant.R;


public class InstantInfoFragment extends Fragment {

    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar spinner;
    private Button reloadButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_instant_info, container, false);
        swipeRefreshLayout = view.findViewById(R.id.instantInfoContainer);
        spinner = view.findViewById(R.id.progressBar);
        reloadButton = view.findViewById(R.id.reloadButton);

        spinner.setVisibility(View.GONE);
        reloadButton.setVisibility(View.GONE);

        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
