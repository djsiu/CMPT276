package com.cmpt276.calciumparentapp.model.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * The class that handles all interactions with tasks.
 * The class is singleton and must be provided a context in order to obtain an instance
 * Tasks and their properties are all accessed by their index.
 * When a task is removed it is deleted and all tasks after them have their index reduced by 1
 */
public class TaskManager {
    private static final String TASK_MANAGER_KEY = "TaskManagerKey";
    private final List<Task> taskList;

    // The application context is used so there is no memory leak
    @SuppressLint("StaticFieldLeak")
    private static TaskManager instance;
    private transient Context context;

    private TaskManager() {
        taskList = new ArrayList<>();
    }

    /**
     * Provides the singleton instance of the TaskManager class
     * @param context A context
     * @return The singleton instance of the TaskManager class
     */
    public static TaskManager getInstance(Context context) {
        if(instance == null){
            generateInstance(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Sets the static instance field to an instance of the class
     * Attempts to load the instance from shared preferences if one exists
     * @param context A context
     */
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
     * Gets the total number of tasks in the manager
     * @return The number of tasks
     */
    public int getSize() {
        return taskList.size();
    }

    /**
     * Creates a new task with the given name and assigns the responsible child to the first child
     * in the order obtained by the FamilyMemberManager
     * @param taskName The name of the new task
     */
    public void createNewTask(String taskName) {
        Task task = new Task(taskName);
        setDefaultChildID(task);
        taskList.add(task);
        saveInstance();
    }

    /**
     * Deletes a task from taskList using given index
     * @param taskIndex index of task to be removed
     */
    public void deleteTask(int taskIndex) {
        taskList.remove(taskIndex);
        saveInstance();
    }

    /**
     * Gets the string representing the child's name for the task at a given index
     * @param i The index of the task
     * @return The name of the child responsible for the given task.
     */
    public String getChildName(int i) {
        FamilyMembersManager familyMembersManager = FamilyMembersManager.getInstance(context);
        if(!familyMembersManager.getFamilyMemberFromID(getChildID(i)).isDeleted()) {
            return familyMembersManager.getFamilyMemberNameFromID(getChildID(i));
        }
        completeTask(i); // get the next family member in the list

        return familyMembersManager.getFamilyMemberNameFromID(getChildID(i));
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
     * Changes the name of the given task
     * @param newName The new name of the task
     * @param i The index of the task
     */
    public void editTaskName(String newName, int i) {
        taskList.get(i).setTaskName(newName);
        saveInstance();
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
