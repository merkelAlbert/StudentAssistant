package com.assistant.albert.studentassistant.homework;

public class HomeworkItem {
    private String subject;
    private String exercise;
    private int time;

    public HomeworkItem(String subject, String exercise, int time) {
        this.subject = subject;
        this.exercise = exercise;
        this.time = time;
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
