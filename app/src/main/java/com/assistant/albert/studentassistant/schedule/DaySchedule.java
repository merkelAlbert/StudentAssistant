package com.assistant.albert.studentassistant.schedule;

import java.util.ArrayList;

public class DaySchedule {
    private ArrayList<ArrayList<String>> daySchedule;

    public DaySchedule() {
        daySchedule = new ArrayList<>();
    }

    public ArrayList<ArrayList<String>> Schedule() {
        return daySchedule;
    }
}
