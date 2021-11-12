package com.cmpt276.calciumparentapp.ui.tasks;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.tasks.TaskManager;

/**
 * The activity that handles configuring tasks.
 * This includes both adding new tasks and editing existing ones.
 */
public class ConfigureTask extends AppCompatActivity {

    public static final String EDIT_TASK_INTENT = "EDIT_TASK_INTENT";
    private boolean addTask;
    private int taskID;
    private TaskManager taskManager;
    private EditText taskNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_task);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        taskManager = TaskManager.getInstance();

        loadIntentExtras();
        configureButtons();
        configureText();

    }

    /**
     * loads the extras from the launching intent.
     * Sets the taskID and addTask fields accordingly
     */
    private void loadIntentExtras() {
        // taskIDs are always positive so a value of -1 means that no extra was passed
        // meaning configure tasks was created to add a task rather than edit one.
        int taskID = getIntent().getIntExtra(EDIT_TASK_INTENT, -1);
        if(taskID == -1){
            addTask = true;
        }
        else {
            addTask = false;
        }
    }

    private void configureText() {


        taskNameEditText = findViewById(R.id.edit_text_task_name);
    }

    private void configureButtons() {
        Button cancelBtn = findViewById(R.id.configure_tasks_cancel_btn);
        Button saveBtn = findViewById(R.id.configure_tasks_save_btn);
        Button deleteBtn = findViewById(R.id.configure_tasks_delete_btn);

        // The delete button only exists if we are editing a task
        if(addTask){
            deleteBtn.setVisibility(View.GONE);
        }

        // Leave the activity and change nothing
        cancelBtn.setOnClickListener(v -> finish());

        saveBtn.setOnClickListener(v -> saveButtonOnClick());
        deleteBtn.setOnClickListener(v -> deleteButtonOnClick());
    }

    private void saveButtonOnClick() {
        if(isTaskNameValid()){
            String taskName = taskNameEditText.getText().toString();
            taskManager.createNewTask(taskName);
            finish();
        }
        else{
            // TODO: display error toast
        }
    }

    private boolean isTaskNameValid() {
        // TODO: Implement checking of task name
        return true;
    }

    private void deleteButtonOnClick() {

    }


    /**
     * Adds logic to action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Top left back arrow
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeAddTaskIntent(Context context){
        return new Intent(context, ConfigureTask.class);
    }

    /**
     * Makes an intent to launch the configure task activity specifying
     * that it should edit an already existing task.
     * @param context A context
     * @param taskID The ID of the task to be edited
     * @return An intent to launch The ConfigureTask activity
     */
    public static Intent makeEditTaskIntent(Context context, int taskID) {
        Intent i = new Intent(context, ConfigureTask.class);
        i.putExtra(EDIT_TASK_INTENT, taskID);
        return i;
    }
}