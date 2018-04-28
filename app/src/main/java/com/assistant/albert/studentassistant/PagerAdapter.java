package com.assistant.albert.studentassistant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.assistant.albert.studentassistant.homework.HomeworkFragment;
import com.assistant.albert.studentassistant.instantinfo.InstantInfoFragment;
import com.assistant.albert.studentassistant.schedule.ScheduleFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int tabsNumber;

    public PagerAdapter(FragmentManager fragmentManager, int tabsNumber) {
        super(fragmentManager);
        this.tabsNumber = tabsNumber;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeworkFragment();
            case 1:
                return new ScheduleFragment();
            case 2:
                return new InstantInfoFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
