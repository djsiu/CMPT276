package com.cmpt276.calciumparentapp.ui.tasks;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.cmpt276.calciumparentapp.model.tasks.TaskManager;

/**
 * Activity for displaying task details
 */
public class ViewTask extends AppCompatActivity {
    public static final String VIEW_TASK_TASK_ID_EXTRA = "VIEW_TASK_TASK_ID_EXTRA";
    private FamilyMembersManager familyManager;
    TaskManager taskManager;
    int taskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        taskManager = TaskManager.getInstance(this);
        familyManager = FamilyMembersManager.getInstance(this);
        loadIntentExtra();
        setupText();
        setupButtons();
        setupChildIcon();
    }

    private void loadIntentExtra() {
        taskIndex = getIntent().getIntExtra(VIEW_TASK_TASK_ID_EXTRA, -1);
        if (taskIndex == -1) {
            throw new IllegalStateException("ViewTask activity created without passing taskID extra");
        }
    }

    private void setupButtons() {
        Button cancelBtn = findViewById(R.id.view_task_cancel);
        Button completeBtn = findViewById(R.id.view_task_confirm_button);
        cancelBtn.setOnClickListener(v -> finish());
        completeBtn.setOnClickListener(v -> {
            taskManager.completeTask(taskIndex);
            finish();
        });
    }

    /**
     * Sets all the text fields accordingly. This includes the name of the task and the name of the child
     */
    private void setupText() {
        TextView taskNameTextView = findViewById(R.id.view_task_task_name);
        taskNameTextView.setText(taskManager.getTaskName(taskIndex));

        TextView childNameTextView = findViewById(R.id.view_task_child_name);
        childNameTextView.setText(taskManager.getChildName(taskIndex));
    }

    private void setupChildIcon() {
        ImageView childIconImageView = findViewById(R.id.view_task_child_icon);
        int childID = taskManager.getChildID(taskIndex);
        childIconImageView.setImageResource(familyManager.getFamilyMemberFromID(childID).getIconID());
    }

    /**
     * Adds logic to action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Top left back arrow
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_edit_task) {
            editTask();
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the currently selected task is edited.
     * Creates a new ConfigureTask activity set to edit the current task
     * Finishes the current activity the ConfigureTask activity returns
     */
    private void editTask() {
        Intent i = ConfigureTask.makeEditTaskIntent(this, taskIndex);
        finish();
        startActivity(i);
    }

    /**
     * Displays actionbar buttons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_view_task, menu);
        return true;
    }

    public static Intent makeIntent(Context context, int index) {
        Intent intent = new Intent(context, ViewTask.class);
        intent.putExtra(VIEW_TASK_TASK_ID_EXTRA, index);
        return intent;
    }
}