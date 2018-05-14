package com.assistant.albert.studentassistant.schedule;

import java.util.ArrayList;

class Subjects {
    private ArrayList<String> list;

    public Subjects() {
        list = new ArrayList<>();
    }

    public ArrayList<String> List() {
        return list;
    }
}


class ClassSchedule {
    private ArrayList<String> classSchedule;

    public ClassSchedule() {
        classSchedule = new ArrayList<>();
    }

    public ArrayList<String> Schedule() {
        return classSchedule;
    }
}

class DaySchedule {
    private ArrayList<ArrayList<String>> daySchedule;

    public DaySchedule() {
        daySchedule = new ArrayList<>();
    }

    public ArrayList<ArrayList<String>> Schedule() {
        return daySchedule;
    }
}

public class ScheduleItem {

    private String id;
    private String userId;
    private ArrayList<ArrayList<ArrayList<String>>> schedule;

    public ScheduleItem() {
        this.schedule = new ArrayList<>();
    }

    public String Id() {
        return id;
    }

    public String UserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<ArrayList<ArrayList<String>>> Schedule() {
        return this.schedule;
    }
}
