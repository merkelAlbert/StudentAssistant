package com.assistant.albert.studentassistant.homework;

import java.util.Comparator;

public class HomeworkComparator implements Comparator<HomeworkItem> {

    @Override
    public int compare(HomeworkItem homeworkItem, HomeworkItem t1) {
        if (homeworkItem.Time() > t1.Time()) return 1;
        else if (homeworkItem.Time() == t1.Time()) return 0;
        else return -1;
    }
}
