package com.assistant.albert.studentassistant.instantinfo;

import java.util.Date;

public class InstantInfoItem {
    private String id;
    private String userId;
    private String userName;
    private String group;
    private Date startDate;

    public InstantInfoItem(String id, String userId, String userName, String group, Date startDate) {
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

    public Date StartDate() {
        return startDate;
    }

}
