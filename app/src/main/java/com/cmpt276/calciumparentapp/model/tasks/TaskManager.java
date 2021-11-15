package com.cmpt276.calciumparentapp.model.tasks;

import android.content.Context;
import android.content.SharedPreferences;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private List<Task> taskList;

    private static TaskManager instance;
    private int taskIDCounter; // Used to generate new task IDs. Incremented everytime a new task is made

    private static final String TASK_MANAGER_KEY = "TaskManagerKey";

    private transient Context context;

    private TaskManager() {
        taskList = new ArrayList<>();
        taskIDCounter = 0;
    }

    public static TaskManager getInstance(Context context) {
        if(instance == null){
            generateInstance(context.getApplicationContext());
        }
        return instance;
    }

    private static void generateInstance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.app_shared_preferences_key),
                Context.MODE_PRIVATE);

        String json = sharedPreferences.getString(TASK_MANAGER_KEY, "");

        if(json.equals("")){
            instance = new TaskManager();
        }
        else{
            Gson gson = new Gson();
            instance = gson.fromJson(json, TaskManager.class);
        }

        instance.context = context;
    }

    /**
     * Saves the current instance to the shared preferences.
     * Should be called whenever there is a change to the TaskManager
     */
    private void saveInstance() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                context.getString(R.string.app_shared_preferences_key),
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this);
        editor.putString(TASK_MANAGER_KEY, json);
        editor.apply();
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

    /**
     * Gets the total number of tasks in the manager
     * @return The number of tasks
     */
    public int getSize() {
        return taskList.size();
    }

    public void createNewTask(String taskName) {
        Task task = new Task(taskName, generateNewTaskID());
        setDefaultChildID(task);
        taskList.add(task);
        saveInstance();
    }

    /**
     * Gets the string representing the child's name for the task at a given index
     * @param i The index of the task
     * @return The name of the child responsible for the given task.
     */
    public String getChildName(int i) {
        FamilyMembersManager familyMembersManager = FamilyMembersManager.getInstance(context);
        return familyMembersManager.getFamilyMemberNameFromID(taskList.get(i).getChildID());
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
     * Generates a new ID for a task. Increments the taskIDCounter.
     * @return A new task ID
     */
    private int generateNewTaskID() {
        int id = taskIDCounter;
        taskIDCounter++;
        saveInstance();
        return id;
    }

    /**
     * Changes the name of the given task
     * @param newName The new name of the task
     * @param i The index of the task
     */
    public void editTaskName(String newName, int i) {
        taskList.get(i).setTaskName(newName);
        saveInstance();
    }


    private Task getTaskByID(int taskID) {
        for(Task task : taskList) {
            if(task.getTaskID() == taskID) {
                return task;
            }
        }

        throw new IllegalArgumentException("Invalid task ID in getTaskByID");
    }

    /**
     * Gets the task ID of the task at a given index
     * @param i The index of the task
     * @return The task ID
     */
    public int getTaskID(int i) {
        return taskList.get(i).getTaskID();
    }


    /**
     * Sets the childID for the given task to the default (first child).
     * @param task The task which will have its childID set
     */
    private void setDefaultChildID(Task task) {
        FamilyMembersManager familyMembersManager = FamilyMembersManager.getInstance(context);
        task.setChildID(familyMembersManager.getNextFamilyMemberInOrder(0));
        saveInstance();
    }

    /**
     * Gets the ID of the child responsible for the given task
     * @param i The index of the task
     * @return The ID of the child
     */
    public int getChildID(int i) {
        return taskList.get(i).getChildID();
    }



    /**
     * Completes the given task and advances the responsible child to the next one.
     * @param i The index of the task
     */
    public void completeTask(int i) {
        FamilyMembersManager familyMembersManager = FamilyMembersManager.getInstance(context);
        int childID = taskList.get(i).getChildID();
        // Sets the childID to the next one in the list
        taskList.get(i).setChildID(familyMembersManager.getNextFamilyMemberInOrder(childID));
        saveInstance();
    }





}
