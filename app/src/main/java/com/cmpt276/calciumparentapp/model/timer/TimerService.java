package com.cmpt276.calciumparentapp.model.timer;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.ui.timer.Timer;

//TODO Find where notification ends with timer
// setup alarm in that spot
public class TimerService extends Service {
    public static final int NOTIFICATION_ID = 1;
    public static final String BROADCAST_FILTER = "TIMER_FILTER";
    public static final String TIME_REMAINING_KEY = "TIME_REMAINING";

    private boolean enableAlarm = true;

    private final NotificationHelper notificationHelper = new NotificationHelper(this);
    private TimerLogic timerLogic;
    private CountDownTimer timer;

    @Override
    public void onCreate() {
        timerLogic = TimerLogic.getInstance();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("Service", "service onDestroy called!");
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long length = intent.getLongExtra(getString(R.string.timer_length_extra), -1);
        if(length == -1) {
            throw new IllegalStateException("Timer service started without providing a length");
        }

        setupTimer(length);
        startForeground(NOTIFICATION_ID, notificationHelper.getTimerNotification(length));
        return START_NOT_STICKY;
    }

    private void updateNotification(long newTime){
        Notification notification = notificationHelper.getTimerNotification(newTime);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }

    //TODO I think I need to setup the alarm here
    private void setupTimer(long length){
        // update every second
        timer = new CountDownTimer(length, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateNotification(millisUntilFinished);
                talk(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                startAlarm();
                stopSelf();
            }
        }.start();
    }

    public void talk(long ms) {
        Intent i = new Intent();
        i.putExtra(TIME_REMAINING_KEY, ms);
        i.setAction(BROADCAST_FILTER);
        sendBroadcast(i);
    }

    public void startAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Timer.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                1, intent,
                PendingIntent.FLAG_IMMUTABLE);

        AlarmManager.AlarmClockInfo timeInfo = new AlarmManager.AlarmClockInfo(0, pendingIntent);

        alarmManager.setAlarmClock(timeInfo, pendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}















