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

import com.assistant.albert.studentassistant.MainActivity;
import com.assistant.albert.studentassistant.R;

import com.assistant.albert.studentassistant.Urls;
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
    private Button clearScheduleButton;
    private Button clearDataButton;
    private Button removeAccountButton;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        session = new SessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        final String userId = user.get(SessionManager.KEY_USER_ID);

        ScheduleItem schedule = new ScheduleItem();

        if (!session.getUserSchedule().isEmpty()) {
            try {
                schedule = Utils.getScheduleFromJson(new JSONObject(session.getUserSchedule()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final ScheduleItem finalSchedule = schedule;
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        exitButton = view.findViewById(R.id.exitButton);
        changeScheduleButton = view.findViewById(R.id.changeScheduleButton);
        changeInstantInfoButton = view.findViewById(R.id.changeInstantInfoButton);
        clearScheduleButton = view.findViewById(R.id.clearScheduleButton);
        clearDataButton = view.findViewById(R.id.clearDataButton);
        removeAccountButton = view.findViewById(R.id.removeAccountButton);

        changeScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NewScheduleActivity.class);
                if (!session.getUserSchedule().isEmpty()) {
                    i.putExtra("isEditing", true);
                    i.putExtra("id", finalSchedule.Id());
                } else
                    i.putExtra("isEditing", false);
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


        clearScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.clear_schedule_question);
                builder.setPositiveButton("Очистить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utils.clear(getActivity(), Urls.clearSchedule + userId);
                        session.add(SessionManager.KEY_SCHEDULE, null);
                        session.add(SessionManager.KEY_SUBJECTS, null);
                        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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


        clearDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.clear_data_question);
                builder.setPositiveButton("Очистить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utils.clear(getActivity(), Urls.clearData + userId);
                        session.add(SessionManager.KEY_SCHEDULE, null);
                        session.add(SessionManager.KEY_SUBJECTS, null);
                        session.add(SessionManager.KEY_INSTANT_INFO, null);
                        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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

        removeAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.remove_account_question);
                builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utils.clear(getActivity(), Urls.removeAccount + userId);
                        session.logoutUser();
                        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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
