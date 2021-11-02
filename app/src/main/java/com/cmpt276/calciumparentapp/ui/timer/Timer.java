package com.cmpt276.calciumparentapp.ui.timer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.TimerLogic;

public class Timer extends AppCompatActivity {
    private TextView countdownText;
    public static final String CHANNEL_ID = "timerServiceChannel";
    private final TimerLogic timerLogic = TimerLogic.getInstance();
    private long timeRemaining;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timer);
        createNotificationChannel();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        countdownText = findViewById(R.id.countdown_text);
        setupBroadcastReceiver();
        if(!isMyServiceRunning(TimerService.class)){
            startTimerService();
        }

        setupButtons();
    }

    private void setupButtons(){
        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Button", "Button clicked!!");
                timerLogic.setTimerLength(timeRemaining);
                stopTimerService();
            }
        });
    }


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

    // Creates the notification channel for the required versions of android
    // May need to be called only once for the whole application in which case this needs to be
    // moved to the main activity
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Timer Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.enableVibration(false);
            serviceChannel.enableLights(false);
            serviceChannel.setSound(null, null);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

        }
    }

    private void startTimerService(){
        Intent serviceIntent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(getString(R.string.timer_length_extra), timerLogic.getTimerLength());
        startService(serviceIntent);
    }

    private void stopTimerService(){
        Intent serviceIntent = new Intent(this, TimerService.class);
        stopService(serviceIntent);
    }

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