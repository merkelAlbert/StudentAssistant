package com.assistant.albert.studentassistant.studentassistant.homework;

import java.util.Comparator;

public class HomeworkComparator implements Comparator<HomeworkResponse> {

    @Override
    public int compare(HomeworkResponse homeworkResponse, HomeworkResponse t1) {
        if (homeworkResponse.Time() > t1.Time()) return 1;
        else if (homeworkResponse.Time() == t1.Time()) return 0;
        else return -1;
    }
}
