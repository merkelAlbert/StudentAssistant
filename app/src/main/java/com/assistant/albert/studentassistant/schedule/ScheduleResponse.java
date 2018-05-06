package com.assistant.albert.studentassistant.schedule;

import java.util.ArrayList;


class ClassSchedule {
    private ArrayList<String> classSchedule;

    public ClassSchedule() {
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

    private String id;
    private ArrayList<ArrayList<ArrayList<String>>> schedule;

    public ScheduleResponse() {
        this.schedule = new ArrayList<>();
    }

    public String Id() {
        return id;
    }

    public ArrayList<ArrayList<ArrayList<String>>> Schedule() {
        return this.schedule;
    }
}
