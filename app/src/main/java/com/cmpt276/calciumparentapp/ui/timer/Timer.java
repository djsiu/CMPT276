package com.cmpt276.calciumparentapp.ui.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.NotificationHelper;
import com.cmpt276.calciumparentapp.model.timer.TimerLogic;
import com.cmpt276.calciumparentapp.model.timer.TimerService;

import java.util.Objects;

public class Timer extends AppCompatActivity {
    private TextView countdownText;
    public static final String CHANNEL_ID = "timerServiceChannel";
    private final TimerLogic timerLogic = TimerLogic.getInstance();
    private NotificationHelper notificationHelper = new NotificationHelper(this);
    private long timeRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timer);
        notificationHelper.createNotificationChannel();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        countdownText = findViewById(R.id.countdown_text);
        setupBroadcastReceiver();
        if(!timerLogic.isMyServiceRunning(this)){
            startTimerService();
        }

        setupButtons();
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

    private void setupButtons(){
        Button stopButton = findViewById(R.id.btnStop);
        stopButton.setOnClickListener(v -> {
            Log.e("Button", "Button clicked!!");
            timerLogic.setTimerLength(timeRemaining);
            stopTimerService();
        });
    }

    // TIMER SERVICE

    private void startTimerService(){
        Intent serviceIntent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(getString(R.string.timer_length_extra), timerLogic.getTimerLength());
        startService(serviceIntent);
    }

    private void stopTimerService(){
        Intent serviceIntent = new Intent(this, TimerService.class);
        stopService(serviceIntent);
    }

    private void setupBroadcastReceiver(){
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                timeRemaining = intent.getLongExtra(TimerService.TIME_REMAINING_KEY, -1);
                countdownText.setText(timerLogic.getTimerText(timeRemaining));
            }
        };

        IntentFilter filter = new IntentFilter(TimerService.BROADCAST_FILTER);
        this.registerReceiver(br, filter);
    }
}