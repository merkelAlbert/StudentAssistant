package com.assistant.albert.studentassistant.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.assistant.albert.studentassistant.R;

import com.assistant.albert.studentassistant.authentification.SessionManager;
import com.assistant.albert.studentassistant.instantinfo.NewInstantInfoActivity;
import com.assistant.albert.studentassistant.schedule.NewScheduleActivity;
import com.assistant.albert.studentassistant.schedule.ScheduleItem;
import com.assistant.albert.studentassistant.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class SettingsFragment extends Fragment {

    private View view;
    private SessionManager session;
    private Button exitButton;
    private Button changeScheduleButton;
    private Button changeInstantInfoButton;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        session = new SessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        final String userId = user.get(SessionManager.USER_ID);

        ScheduleItem schedule = new ScheduleItem();
        try {
            schedule = Utils.getScheduleFromJson(new JSONObject(session.getUSerSchedule()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        exitButton = view.findViewById(R.id.exitButton);
        changeScheduleButton = view.findViewById(R.id.changeScheduleButton);
        changeInstantInfoButton = view.findViewById(R.id.changeInstantInfoButton);

        final ScheduleItem finalSchedule = schedule;
        changeScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NewScheduleActivity.class);
                i.putExtra("isEditing",true);
                i.putExtra("id", finalSchedule.Id());
                startActivity(i);
            }
        });

        changeInstantInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NewInstantInfoActivity.class);
                startActivity(i);
            }
        });


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.exit_question);
                builder.setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        session.logoutUser();
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        return view;
    }

}
