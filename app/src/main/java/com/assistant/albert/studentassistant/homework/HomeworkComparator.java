package com.assistant.albert.studentassistant.homework;

import java.util.Comparator;

public class HomeworkComparator implements Comparator<HomeworkItem> {

    @Override
    public int compare(HomeworkItem homeworkItem, HomeworkItem t1) {
        if (homeworkItem.RemainedDays() > t1.RemainedDays()) return 1;
        else if (homeworkItem.RemainedDays() == t1.RemainedDays()) return 0;
        else return -1;
    }
}
