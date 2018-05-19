package com.assistant.albert.studentassistant.instantinfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InstantInfoItem {
    private String id;
    private String userId;
    private String userName;
    private String group;
    private String startDate;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public InstantInfoItem()
    {
    }

    public InstantInfoItem(String id, String userId, String userName, String group, String startDate) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.group = group;
        this.startDate = startDate;
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

}
