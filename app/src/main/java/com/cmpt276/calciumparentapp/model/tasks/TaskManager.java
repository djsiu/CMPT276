package com.cmpt276.calciumparentapp.model.tasks;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private List<Task> taskList;

    private static TaskManager instance;

    private TaskManager() {
        taskList = new ArrayList<>();
    }

    public static TaskManager getInstance() {
        if(instance == null){
            instance = new TaskManager();
        }
        return instance;
    }

    /**
     * Gets the task at a given index
     * @param i The index of the task
     * @return A task object for the given index
     */
    public Task getTask(int i) {
        // Make sure the index isn't out of bounds
        assert(i < taskList.size());

        return taskList.get(i);

    }

    public void addTask(Task task) {
        setChildID(task);
        taskList.add(task);
    }

    /**
     * Gets the string representing the child's name for the task at a given index
     * @param i The index of the task
     * @return The name of the child responsible for the given task.
     */
    public String getChildName(int i) {
        return "TEST STRING";
    }

    /**
     * Gets the name of a task at a given index
     * @param i The index of the task
     * @return The name of the task
     */
    public String getTaskName(int i ) {
        return taskList.get(i).getTaskName();
    }


    /**
     * Sets the childID for the given task.
     * @param task The task which will have its childID set
     */
    private void setChildID(Task task) {

    }





}
