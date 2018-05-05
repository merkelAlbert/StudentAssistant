package com.assistant.albert.studentassistant.schedule;

import java.util.ArrayList;


class ClassChedule {
    private ArrayList<String> classSchedule;

    public ClassChedule() {
        classSchedule = new ArrayList<>();
    }

    public ArrayList<String> getClassSchedule() {
        return classSchedule;
    }
}

class DaySchedule {
    private ArrayList<ArrayList<String>> daySchedule;

    public DaySchedule() {
        daySchedule = new ArrayList<>();
    }

    public ArrayList<ArrayList<String>> getDaySchedule() {
        return daySchedule;
    }
}

public class ScheduleResponse {

    private ArrayList<ArrayList<ArrayList<String>>> schedule;

    public ScheduleResponse() {
        this.schedule = new ArrayList<>();
    }

    public ArrayList<ArrayList<ArrayList<String>>> Schedule() {
        return this.schedule;
    }
}
