package com.assistant.albert.studentassistant.homework;

public class HomeworkItem {
    private String id;
    private String userId;
    private String subject;
    private String exercise;
    private int time;

    public HomeworkItem(String id, String userId, String subject, String exercise, int time) {
        this.id = id;
        this.userId = userId;
        this.subject = subject;
        this.exercise = exercise;
        this.time = time;
    }

    public String Id() {
        return id;
    }

    public String UserId() {
        return userId;
    }

    public String Subject() {
        return subject;
    }

    public String Exercise() {
        return exercise;
    }

    public int Time() {
        return time;
    }
}
