package com.cmpt276.calciumparentapp.ui.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.TimerLogic;
import com.cmpt276.calciumparentapp.model.timer.TimerService;

import java.util.Objects;

/**
 * UI for Timer
 */
public class Timer extends AppCompatActivity {
    private final TimerLogic timerLogic = TimerLogic.getInstance();
    private long timeRemaining;
    private TextView countdownText;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver timerRunningBroadcastReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        countdownText = findViewById(R.id.countdown_text);

        setup();
    }

    @Override
    protected void onResume() {
        setupBroadcastReceiver();
        if(timerLogic.isTimerServiceRunning(this)){
            broadcastTimeRequest();
        }
        else{
            resetCountdownText();
            Button btn = findViewById(R.id.btnStartPause);
            btn.setText(R.string.btnStart);
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterBroadcastReceiver();
        super.onPause();
    }

    // BUTTONS

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


    private void setup(){
        if(timerLogic.isTimerServiceRunning(this)){
            broadcastTimerRunningRequest();
            broadcastTimeRequest();
        }
        else{
            resetCountdownText();
            setupButtons(false, false);
        }

    }

    private void setupButtons(boolean timerServiceRunning, boolean timerRunning){
        Button controlButton = findViewById(R.id.btnStartPause);
        controlButton.setOnClickListener(v -> onStartPauseButtonClick((Button) v));

        if(timerServiceRunning && timerRunning){
            controlButton.setText(R.string.btnPause);
        }

        if(timerServiceRunning && !timerRunning) {
            controlButton.setText(R.string.btnResume);
        }

        Button resetButton = findViewById(R.id.btnReset);
        resetButton.setOnClickListener(v -> {
            stopTimerService();
            resetCountdownText();
            controlButton.setText(R.string.btnStart);
        });
    }

    private void resetCountdownText() {
        countdownText.setText(timerLogic.getTimerText(timerLogic.getTimerLength()));
    }

    private void onStartPauseButtonClick(Button btn) {
        // Check what the button is supposed to do
        if(btn.getText().equals(getString(R.string.btnStart))){
            btn.setText(R.string.btnPause);
            startTimer();
        }

        else if(btn.getText().equals(getString(R.string.btnPause))){
            broadcastPauseIntent();
            btn.setText(R.string.btnResume);
        }

        else if(btn.getText().equals(getString(R.string.btnResume))){
            broadcastResumeIntent();
            btn.setText(R.string.btnPause);
        }
    }

    private void startTimer() {
        Button btn = findViewById(R.id.btnStartPause);
        btn.setText(R.string.btnPause);
        startTimerService();
    }


    // TIMER SERVICE

    private void startTimerService(){
        Intent serviceIntent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(getString(R.string.timer_length_extra), timerLogic.getTimerLength());
        startService(serviceIntent);
    }

    private void stopTimerService() {
        Intent serviceIntent = new Intent(this, TimerService.class);
        stopService(serviceIntent);
    }

    private void setupBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                timeRemaining = intent.getLongExtra(TimerService.TIME_REMAINING_BROADCAST, -1);
                countdownText.setText(timerLogic.getTimerText(timeRemaining));
            }
        };

        timerRunningBroadcastReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setupButtons(true, intent.getBooleanExtra(TimerService.TIMER_RUNNING_BROADCAST, false));
            }
        };

        IntentFilter filter = new IntentFilter(TimerService.TIME_BROADCAST_FILTER);
        this.registerReceiver(broadcastReceiver, filter);

        IntentFilter timerRunningFilter = new IntentFilter(TimerService.TIMER_RUNNING_BROADCAST_FILTER);
        this.registerReceiver(timerRunningBroadcastReciever, timerRunningFilter);
    }

    private void unregisterBroadcastReceiver() {
        unregisterReceiver(broadcastReceiver);
    }

    private void broadcastPauseIntent() {
        Intent i = new Intent();
        i.putExtra(TimerService.PAUSE_TIMER_INTENT, true);
        i.setAction(TimerService.TIMER_SERVICE_REQUEST_FILTER);
        sendBroadcast(i);
    }

    private void broadcastResumeIntent() {
        Intent i = new Intent();
        i.putExtra(TimerService.RESUME_TIMER_INTENT, true);
        i.setAction(TimerService.TIMER_SERVICE_REQUEST_FILTER);
        sendBroadcast(i);
    }

    private void broadcastTimeRequest() {
        Intent i = new Intent();
        i.putExtra(TimerService.REFRESH_TIME_INTENT, true);
        i.setAction(TimerService.TIMER_SERVICE_REQUEST_FILTER);
        sendBroadcast(i);
    }

    private void broadcastTimerRunningRequest() {
        Intent i = new Intent();
        i.putExtra(TimerService.TIMER_RUNNING_REQUEST_INTENT, true);
        i.setAction(TimerService.TIMER_SERVICE_REQUEST_FILTER);
        sendBroadcast(i);
    }

}