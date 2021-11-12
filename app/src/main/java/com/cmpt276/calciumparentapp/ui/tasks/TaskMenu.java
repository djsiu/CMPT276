package com.cmpt276.calciumparentapp.ui.tasks;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.tasks.TaskManager;
import com.cmpt276.calciumparentapp.ui.coinflip.CoinFlip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskMenu extends AppCompatActivity {

    TaskManager taskManager;
    TasksRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_menu);

        taskManager = TaskManager.getInstance();

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        setupRecyclerView();
        setupAddTaskButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.tasks_recycler_view);
        adapter = new TasksRecyclerViewAdapter(this, taskManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupAddTaskButton() {
        FloatingActionButton addTaskBtn = findViewById(R.id.addTaskButton);
        addTaskBtn.setOnClickListener(v -> {
            Intent i = ConfigureTask.makeAddTaskIntent(TaskMenu.this);
            startActivity(i);
        });
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

    public static Intent makeIntent(Context context){
        return new Intent(context, TaskMenu.class);
    }
}