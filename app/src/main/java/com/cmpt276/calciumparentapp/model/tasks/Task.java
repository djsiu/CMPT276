package com.cmpt276.calciumparentapp.model.tasks;

public class Task {

    private String taskName;
    // The ID of the child currently responsible for the task
    private int childID;

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setChildID(int childID) {
        this.childID = childID;
    }

    public int getChildID() {
        return childID;
    }


}
