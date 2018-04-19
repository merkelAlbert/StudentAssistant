package com.assistant.albert.studentassistant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.assistant.albert.studentassistant.studentassistant.homework.HomeworkFragment;

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
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
