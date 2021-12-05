package com.cmpt276.calciumparentapp.model.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A specific iteration of a task
 * Holds an individual time a task is done by a child
 */
public class TaskIteration {
    private final int childID;
    private final String date;

    public TaskIteration(int childID) {
        this.childID = childID;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss");
        date = LocalDateTime.now().format(formatter);
    }

    public int getChildID() {
        return childID;
    }

    public String getDate() {
        return date;
    }
}
