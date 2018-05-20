package com.assistant.albert.studentassistant.instantinfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InstantInfoItem {
    private String id;
    private String userId;
    private String userName;
    private String group;
    private String startDate;
    private int currentWeek;
    private int totalHomework;
    private int totalPassed;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public InstantInfoItem() {
        id = "";
        userId = "";
        userName = "";
        group = "";
        startDate = "";
        currentWeek = 0;
        totalHomework = 0;
        totalPassed = 0;

    }

    public InstantInfoItem(String id, String userId, String userName, String group,
                           String startDate, int currentWeek, int totalHomework, int totalPassed) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.group = group;
        this.startDate = startDate;
        this.currentWeek = currentWeek;
        this.totalHomework = totalHomework;
        this.totalPassed = totalPassed;
    }

    public String Id() {
        return id;
    }

    public String UserId() {
        return userId;
    }

    public String UserName() {
        return userName;
    }

    public String Group() {
        return group;
    }

    public String StartDate() {
        return startDate;
    }

    public int CurrentWeek() {
        return currentWeek;
    }

    public int TotalHomework() {
        return totalHomework;
    }

    public int TotalPassed() {
        return totalPassed;
    }


}
