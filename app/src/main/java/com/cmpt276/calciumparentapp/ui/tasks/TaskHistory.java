package com.cmpt276.calciumparentapp.ui.tasks;

import static com.cmpt276.calciumparentapp.ui.tasks.TaskView.VIEW_TASK_TASK_ID_EXTRA;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.cmpt276.calciumparentapp.model.tasks.TaskManager;

/**
 * Activity that displays the history of current task
 */
public class TaskHistory extends AppCompatActivity {
    FamilyMembersManager familyManager;
    TaskHistoryRecyclerViewAdapter recyclerViewAdapter;
    TaskManager taskManager;
    int taskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);

        taskManager = TaskManager.getInstance(this);
        familyManager = FamilyMembersManager.getInstance(this);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        loadIntentExtra();
        setupRecyclerView();
    }

    private void loadIntentExtra() {
        taskIndex = getIntent().getIntExtra(VIEW_TASK_TASK_ID_EXTRA, -1);
        if (taskIndex == -1) {
            throw new IllegalStateException("TaskHistory activity created without passing taskID extra");
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.task_history_recycler_view);
        recyclerViewAdapter = new TaskHistoryRecyclerViewAdapter(this, taskManager, familyManager, taskIndex);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Adds logic to action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){  // Top left back arrow
            finish();
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context, int index) {
        Intent intent = new Intent(context, TaskHistory.class);
        intent.putExtra(VIEW_TASK_TASK_ID_EXTRA, index);
        return intent;
    }
}