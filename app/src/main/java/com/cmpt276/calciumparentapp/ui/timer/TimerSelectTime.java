package com.cmpt276.calciumparentapp.ui.timer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.ui.MainMenu;

public class TimerSelectTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_select_time);

        Button btnTimerOneMin = findViewById(R.id.select_time_1_minute_button);

        setupOneMinuteButton(btnTimerOneMin);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
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

    private void setupOneMinuteButton(Button button){
        button.setOnClickListener(v -> {
            // Opens the Timer activity
            Intent i = Timer.makeIntent(TimerSelectTime.this);
            startActivity(i);
        });
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, TimerSelectTime.class);
    }
}