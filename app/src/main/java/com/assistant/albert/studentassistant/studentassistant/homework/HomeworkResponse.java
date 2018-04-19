package com.assistant.albert.studentassistant.studentassistant.homework;

public class HomeworkResponse {
    private String subject;
    private String exercise;
    private int time;

    public HomeworkResponse(String subject, String exercise, int time) {
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
