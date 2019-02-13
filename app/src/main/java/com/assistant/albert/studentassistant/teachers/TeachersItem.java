package com.assistant.albert.studentassistant.teachers;

import java.util.ArrayList;
import java.util.List;

public class TeachersItem {
    private String id;
    private List<String> subjects;
    private List<String> teachers;

    public TeachersItem() {
        subjects = new ArrayList<>();
        teachers = new ArrayList<>();
    }

    public TeachersItem(String id, List<String> subjects, List<String> teachers) {
        this.id = id;
        this.subjects = subjects;
        this.teachers = teachers;
    }

    public List<String> Subjects() {
        return subjects;
    }

    public List<String> Teachers() {
        return teachers;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    public String Id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
