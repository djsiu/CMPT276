package com.cmpt276.calciumparentapp.model.tasks;

/**
 * The class representing a Task.
 * New tasks should only be created via the TaskManager.createNewTask method
 */
public class Task {

    private String taskName;
    // The ID of the child currently responsible for the task
    private int childID;
    private int taskID; // A unique, unchanging ID for this task

    // protected to prevent creating new tasks outside of the TaskManager
    protected Task(String taskName, int taskID) {
        this.taskName = taskName;
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    protected void setChildID(int childID) {
        this.childID = childID;
    }

    public int getChildID() {
        return childID;
    }


    public int getTaskID() {
        return taskID;
    }
}
