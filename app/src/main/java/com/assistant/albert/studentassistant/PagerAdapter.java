package com.assistant.albert.studentassistant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.assistant.albert.studentassistant.homework.HomeworkFragment;
import com.assistant.albert.studentassistant.instantinfo.InstantInfoFragment;
import com.assistant.albert.studentassistant.schedule.ScheduleFragment;
import com.assistant.albert.studentassistant.settings.SettingsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int tabsNumber;

    public PagerAdapter(FragmentManager fragmentManager, int tabsNumber) {
        super(fragmentManager);
        this.tabsNumber = tabsNumber;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("pos", String.valueOf(position));
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
                fragment = new HomeworkFragment();
                break;
            case 1:
                fragment = new ScheduleFragment();
                break;
            case 2:
                fragment = new InstantInfoFragment();
                break;
            case 3:
                fragment = new SettingsFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
