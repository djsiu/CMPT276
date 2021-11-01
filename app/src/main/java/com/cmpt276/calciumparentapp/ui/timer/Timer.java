package com.cmpt276.calciumparentapp.ui.timer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.widget.TextView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.TimerLogic;

public class Timer extends AppCompatActivity {
    private TextView countdownText;

    private CountDownTimer countDownTimer;
    private TimerLogic timerLogic;
    private long timeInMS = 50000; // 5 seconds
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

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeInMS, 1000) {
            @Override
            public void onTick(long l) {
                timeInMS = l;

                countdownText.setText(timerLogic.getTimerText(timeInMS));
            }

            @Override
            public void onFinish() {

            }
        }.start();

        timerRunning = true;
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, Timer.class);
    }
}