package com.cmpt276.calciumparentapp.ui.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.TimerLogic;

public class TimerSelectTime extends AppCompatActivity {

    TimerLogic timerLogic = TimerLogic.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_select_time);

        if(isMyServiceRunning(TimerService.class)){
            startTimerActivity();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupButtons();
    }
    // This function is repeated in Timer.java. May want to clean up code
    // Function from:
    // https://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void setupButtons(){

        Button min1 = (Button) findViewById(R.id.button);
        Button min2 = (Button) findViewById(R.id.button2);
        Button min3 = (Button) findViewById(R.id.button3);
        Button min5 = (Button) findViewById(R.id.button4);
        Button min10 = (Button) findViewById(R.id.button5);
        Button custom = (Button) findViewById(R.id.button6);

        min1.setOnClickListener(new ButtonListener());
        min2.setOnClickListener(new ButtonListener());
        min3.setOnClickListener(new ButtonListener());
        min5.setOnClickListener(new ButtonListener());
        min10.setOnClickListener(new ButtonListener());
        custom.setOnClickListener(new ButtonListener());

    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()){

                case R.id.button:
                    timerLogic.setTimerLength(60000);
                    break;
                case R.id.button2:
                    timerLogic.setTimerLength(60000 * 2);
                    break;
                case R.id.button3:
                    timerLogic.setTimerLength(60000 * 3);
                    break;
                case R.id.button4:
                    timerLogic.setTimerLength(60000 * 5);
                    break;
                case R.id.button5:
                    timerLogic.setTimerLength(60000 * 10);
                    break;
                case R.id.button6:
                    // TODO: Implement custom time screen
                    break;

            }

            startTimerActivity();
        }
    }

    private void startTimerActivity(){
        Intent intent = new Intent(this, Timer.class);
        finish();
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Top left back arrow
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, TimerSelectTime.class);
    }
}