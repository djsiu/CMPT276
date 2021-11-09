package com.cmpt276.calciumparentapp.ui.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.TimerLogic;

import java.util.Objects;

/**
 * UI for Selecting Length of Timer
 */
public class TimerSelectTime extends AppCompatActivity {

    TimerLogic timerLogic = TimerLogic.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_select_time);

        if(timerLogic.isTimerServiceRunning(this)){
            startTimerActivity();
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setupButtons();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Top left back arrow
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // BUTTON SETUP

    private void setupButtons(){
        Button min1 = findViewById(R.id.btnOneMinute);
        Button min2 = findViewById(R.id.btnTwoMinute);
        Button min3 = findViewById(R.id.btnThreeMinute);
        Button min5 = findViewById(R.id.btnFiveMinute);
        Button min10 = findViewById(R.id.btnTenMinute);
        Button custom = findViewById(R.id.btnCustomMinute);

        min1.setOnClickListener(new ButtonListener());
        min2.setOnClickListener(new ButtonListener());
        min3.setOnClickListener(new ButtonListener());
        min5.setOnClickListener(new ButtonListener());
        min10.setOnClickListener(new ButtonListener());
        custom.setOnClickListener(new ButtonListener());
    }

    @SuppressLint("NonConstantResourceId") // Stops IDE from complaining about switch statement
    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnOneMinute:
                    timerLogic.setTimerLength(60000);
                    startTimerActivity();
                    break;
                case R.id.btnTwoMinute:
                    timerLogic.setTimerLength(60000 * 2);
                    startTimerActivity();
                    break;
                case R.id.btnThreeMinute:
                    timerLogic.setTimerLength(60000 * 3);
                    startTimerActivity();
                    break;
                case R.id.btnFiveMinute:
                    timerLogic.setTimerLength(60000 * 5);
                    startTimerActivity();
                    break;
                case R.id.btnTenMinute:
                    timerLogic.setTimerLength(60000 * 10);
                    startTimerActivity();
                    break;
                case R.id.btnCustomMinute:
                    startCustomTimeActivity();
                    break;
            }
        }
    }

    private void startCustomTimeActivity(){
        Intent intent = new Intent(this, TimerCustomTime.class);
        finish();
        startActivity(intent);
    }

    private void startTimerActivity(){
        Intent intent = new Intent(this, Timer.class);
        finish();
        startActivity(intent);
    }

    // UTILITY

    public static Intent makeIntent(Context context){
        return new Intent(context, TimerSelectTime.class);
    }
}