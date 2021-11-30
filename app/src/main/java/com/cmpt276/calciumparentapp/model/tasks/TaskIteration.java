package com.cmpt276.calciumparentapp.model.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A specific iteration of a class
 * Holds each individual time each task is done
 */
public class TaskIteration {
    int childID;
    String date;

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
