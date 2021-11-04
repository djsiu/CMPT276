package com.cmpt276.calciumparentapp.model.timer;

import static com.cmpt276.calciumparentapp.ui.timer.Timer.ALARM_CHANNEL_ID;
import static com.cmpt276.calciumparentapp.ui.timer.Timer.TIMER_CHANNEL_ID;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.ui.timer.Timer;

public class NotificationHelper extends ContextWrapper {

    private NotificationManager notificationManager;
    private final TimerLogic timerLogic = new TimerLogic();

    public NotificationHelper(Context base) {
        super(base);

        createTimerChannel();
        createAlarmChannel();
    }

    public void createTimerChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                TIMER_CHANNEL_ID,
                "Timer",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        serviceChannel.enableVibration(false);
        serviceChannel.enableLights(false);
        serviceChannel.setSound(null, null);

        android.app.NotificationManager manager = getSystemService(android.app.NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

    public Notification getTimerNotification(long timeRemaining){
        Intent notificationIntent = new Intent(this, Timer.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, TIMER_CHANNEL_ID)
                .setContentTitle("Timer")
                .setContentText(timerLogic.getTimerText(timeRemaining))
                .setSmallIcon(R.drawable.timer_service_icon)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
    }

    private void createAlarmChannel() {
        NotificationChannel channel = new NotificationChannel(ALARM_CHANNEL_ID, "Alarm", NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationCompat.Builder getAlarmNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), ALARM_CHANNEL_ID)
                .setContentTitle("Alarm")
                .setContentText("Timeout is Over!")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_baseline_timer_24);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }
}
