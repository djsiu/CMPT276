package com.cmpt276.calciumparentapp.model.timer;

import static com.cmpt276.calciumparentapp.ui.timer.Timer.CHANNEL_ID;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.ui.timer.Timer;

//TODO Find where notification ends with timer
// setup alarm in that spot
public class TimerService extends Service {
    public static final int NOTIFICATION_ID = 1;
    public static final String BROADCAST_FILTER = "TIMER_FILTER";
    public static final String TIME_REMAINING_KEY = "TIME_REMAINING";

    private boolean enableAlarm = true;

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
        startForeground(NOTIFICATION_ID, getTimerNotification(length));
        return START_NOT_STICKY;
    }

    private Notification getTimerNotification(long timeRemaining){
        Intent notificationIntent = new Intent(this, Timer.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Timer")
                .setContentText(timerLogic.getTimerText(timeRemaining))
                .setSmallIcon(R.drawable.timer_service_icon)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
    }

    private void updateNotification(long newTime){
        Notification notification = getTimerNotification(newTime);

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
                startAlarm();
            }

            @Override
            public void onFinish() {
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
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

//TODO This here isn't working for some reason
class AlertReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show();

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
    }
}















