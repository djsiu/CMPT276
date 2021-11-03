package com.cmpt276.calciumparentapp.ui.timer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.TimerLogic;

import java.util.Objects;

public class TimerCustomTime extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_custom_time);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setupButtons();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupButtons() {
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Make sure a number is entered
                setTime();
                startTimerActivity();
            }
        });
    }

    private void setTime() {
        EditText customTimeView = (EditText) findViewById(R.id.customTimeEditText);
        long time = Integer.parseInt(customTimeView.getText().toString());
        time = time * 1000 * 60; // minutes to milliseconds
        TimerLogic.getInstance().setTimerLength(time);
    }

    private void startTimerActivity() {
        Intent intent = new Intent(this, Timer.class);
        finish();
        startActivity(intent);
    }




}