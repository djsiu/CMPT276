package com.cmpt276.calciumparentapp.ui.timer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.widget.TextView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.TimerLogic;

//TODO Move more of this logic into model
// Would be a good idea so app can resume timer when pressing
// button from main menu.
public class Timer extends AppCompatActivity {
    private TextView countdownText;

    private final TimerLogic timerLogic = new TimerLogic();
    private long timeLeftInMS = 60000; // 1 min
    private long endTime;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        countdownText = findViewById(R.id.countdown_text);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        timerRunning = true;
        startTimer();
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

    /**
     * Initiates the timer's text counting down
     */
    public void startTimer(){
        // Allows timer to stay accurate even when changing states
        endTime = System.currentTimeMillis() + timeLeftInMS;

        CountDownTimer countDownTimer = new CountDownTimer(timeLeftInMS, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMS = l;

                countdownText.setText(timerLogic.getTimerText(timeLeftInMS));
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO Change name to a constant here
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        timeLeftInMS = prefs.getLong("MSLeft", 60000); //TODO Setup this default more nicely
        timerRunning = prefs.getBoolean("timerRunning", false);
        endTime = prefs.getLong("EndTime", endTime);

        if (timerRunning){
            endTime = prefs.getLong("endTime", 0);
            timeLeftInMS = endTime - System.currentTimeMillis();

            if (timeLeftInMS < 0) {
                timeLeftInMS = 0;
                timerRunning = false;
            } else {
                startTimer();
            }
        }
    }

    protected void onStop(){
        super.onStop();

        //TODO Change name to a constant here
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("MSLeft", timeLeftInMS);
        editor.putBoolean("TimerRunning", timerRunning);
        editor.putLong("EndTime", endTime);

        editor.apply();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, Timer.class);
    }
}