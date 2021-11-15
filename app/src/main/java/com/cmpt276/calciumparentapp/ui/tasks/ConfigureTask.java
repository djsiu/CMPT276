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
import android.widget.TextView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.tasks.TaskManager;

/**
 * The activity that handles configuring tasks.
 * This includes both adding new tasks and editing existing ones.
 */
public class ConfigureTask extends AppCompatActivity {

    public static final String EDIT_TASK_INTENT = "EDIT_TASK_INTENT";
    private boolean addTask;
    private int taskIndex;
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

        taskManager = TaskManager.getInstance(this);

        loadIntentExtras();
        configureButtons();
        configureText();

    }

    /**
     * loads the extras from the launching intent.
     * Sets the taskIndex and addTask fields accordingly
     */
    private void loadIntentExtras() {
        taskIndex = getIntent().getIntExtra(EDIT_TASK_INTENT, -1);
        addTask = taskIndex == -1;

    }

    private void configureText() {
        TextView configureTasksText = findViewById(R.id.configure_tasks_text);
        taskNameEditText = findViewById(R.id.edit_text_task_name);

        if(addTask) {
            configureTasksText.setText(R.string.add_task_text);
        }
        else {
            taskNameEditText.setText(taskManager.getTaskName(taskIndex));
            configureTasksText.setText(R.string.edit_task_text);
        }


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
        if(!isTaskNameValid()) {
            // TODO: display error toast
            return;
        }

        String taskName = taskNameEditText.getText().toString();
        if(addTask){
            taskManager.createNewTask(taskName);

        }
        else{
            taskManager.editTaskName(taskName, taskIndex);
        }
        finish();
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
     * @param index The index of the task
     * @return An intent to launch The ConfigureTask activity
     */
    public static Intent makeEditTaskIntent(Context context, int index) {
        Intent i = new Intent(context, ConfigureTask.class);
        i.putExtra(EDIT_TASK_INTENT, index);
        return i;
    }
}