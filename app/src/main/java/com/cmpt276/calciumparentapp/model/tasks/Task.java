package com.cmpt276.calciumparentapp.model.tasks;

/**
 * The class representing a Task.
 * This class should never be directly accessed aside from the TaskManager class
 */
public class Task {

    private String taskName;
    // The ID of the child currently responsible for the task
    private int childID;

    // protected to prevent creating new tasks outside of the TaskManager
    protected Task(String taskName) {
        this.taskName = taskName;
    }

    protected String getTaskName() {
        return taskName;
    }

    protected void setChildID(int childID) {
        this.childID = childID;
    }

    protected void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    protected int getChildID() {
        return childID;
    }

}
