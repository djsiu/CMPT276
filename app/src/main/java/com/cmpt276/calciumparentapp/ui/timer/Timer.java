package com.cmpt276.calciumparentapp.ui.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
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
    private TimerProgressBar progressBar;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver timerRunningBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        countdownText = findViewById(R.id.countdown_text);
        progressBar = findViewById(R.id.timerProgressBar);

        setup();
    }

    @Override
    protected void onResume() {
        setupBroadcastReceiver();
        if(timerLogic.isTimerServiceRunning(this)){
            broadcastTimeRequest();
        }
        else{
            resetTimerUI();
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

        progressBar.startTimerProgress(timerLogic.getTimerLength());

        if(timerLogic.isTimerServiceRunning(this)){
            // The function called when this broadcast is received calls setup buttons
            broadcastTimerRunningRequest();
            broadcastTimeRequest();
        }
        else{
            resetTimerUI();
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
            resetTimerUI();
            controlButton.setText(R.string.btnStart);
        });
    }

    private void resetTimerUI() {
        countdownText.setText(timerLogic.getTimerText(timerLogic.getTimerLength()));
        progressBar.startTimerProgress(timerLogic.getTimerLength());
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
        // On timer update
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Need to check the service is currently running to prevent the timer being updated
                // after stopService has been called but before the service actually stops
                if(timerLogic.isTimerServiceRunning(getApplicationContext())) {
                    timeRemaining = intent.getLongExtra(TimerService.TIME_REMAINING_BROADCAST, -1);
                    countdownText.setText(timerLogic.getTimerText(timeRemaining));
                    progressBar.updateTimerProgress(timeRemaining);
                }
            }
        };

        timerRunningBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setupButtons(true, intent.getBooleanExtra(TimerService.TIMER_RUNNING_BROADCAST, false));
            }
        };

        IntentFilter filter = new IntentFilter(TimerService.TIME_BROADCAST_FILTER);
        this.registerReceiver(broadcastReceiver, filter);

        IntentFilter timerRunningFilter = new IntentFilter(TimerService.TIMER_RUNNING_BROADCAST_FILTER);
        this.registerReceiver(timerRunningBroadcastReceiver, timerRunningFilter);
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