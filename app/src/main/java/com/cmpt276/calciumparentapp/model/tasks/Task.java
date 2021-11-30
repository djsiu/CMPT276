package com.cmpt276.calciumparentapp.model.tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * The class representing a Task.
 * This class should never be directly accessed aside from the TaskManager class
 */
public class Task {
    private String taskName;
    private int childID; // The ID of the child currently responsible for the task
    private final List<TaskIteration> taskHistory; // Holds each time a task was done

    // protected to prevent creating new tasks outside of the TaskManager
    protected Task(String taskName) {
        this.taskName = taskName;
        taskHistory = new ArrayList<>();
    }

    protected String getTaskName() {
        return taskName;
    }

    protected void setChildID(int childID) {
        taskHistory.add(new TaskIteration(childID));
        this.childID = childID;
    }

    protected List<TaskIteration> getTaskHistory(){
        return taskHistory;
    }

    protected void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    protected int getChildID() {
        return childID;
    }
}
