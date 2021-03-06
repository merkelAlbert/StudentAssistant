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

public class ScheduleItem {

    private String id;
    private String userId;
    private ArrayList<ArrayList<ArrayList<String>>> schedule;
    private Integer currentDay;

    public ScheduleItem() {
        this.schedule = new ArrayList<>();
    }

    public String Id() {
        return id;
    }

    public String UserId() {
        return userId;
    }

    public Integer CurrentDay() {
        return currentDay;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void SetCurrentDay(Integer day) {
        this.currentDay = day;
    }

    public ArrayList<ArrayList<ArrayList<String>>> Schedule() {
        return this.schedule;
    }
}
