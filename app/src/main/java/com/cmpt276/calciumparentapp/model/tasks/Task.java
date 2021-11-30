package com.cmpt276.calciumparentapp.model.tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * The class representing a Task.
 * This class should never be directly accessed aside from the TaskManager class
 */
public class Task {
    private String taskName;
    // The ID of the child currently responsible for the task
    private int childID;
    private List<Integer> taskChildIDHistory;

    // protected to prevent creating new tasks outside of the TaskManager
    protected Task(String taskName) {
        this.taskName = taskName;
        taskChildIDHistory = new ArrayList<>();
    }

    protected String getTaskName() {
        return taskName;
    }

    protected void setChildID(int childID) {
        taskChildIDHistory.add(childID);
        this.childID = childID;
    }

    protected List<Integer> getTaskChildIDHistory(){
        return taskChildIDHistory;
    }

    protected void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    protected int getChildID() {
        return childID;
    }
}
