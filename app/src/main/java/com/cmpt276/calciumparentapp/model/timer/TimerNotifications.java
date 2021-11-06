package com.cmpt276.calciumparentapp.model.timer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.ui.timer.Timer;

/**
 * Logic for creating timer related notifications
 */
public class TimerNotifications extends ContextWrapper {
    public static final int NOTIFICATION_ID = 1;
    public static final String TIMER_CHANNEL_ID = "timerNotificationChannel";
    public static final String ALARM_CHANNEL_ID = "alarmNotificationChannel";
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); // Gets system alarm sound

    private NotificationManager notificationManager;
    private final TimerLogic timerLogic = TimerLogic.getInstance();

    public TimerNotifications(Context base) {
        super(base);

        createTimerChannel();
        createAlarmChannel();
    }

    public void createTimerChannel() {
        NotificationChannel channel = new NotificationChannel(
                TIMER_CHANNEL_ID,
                "Timer",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.enableVibration(false);
        channel.enableLights(false);
        channel.setSound(null, null);

        getManager().createNotificationChannel(channel);
    }

    private void createAlarmChannel() {
        long[] vibratePattern = new long[]{200,400,800,600,800,800,800,1000,1000,1000};

        NotificationChannel channel = new NotificationChannel(
                ALARM_CHANNEL_ID,
                "Alarm",
                NotificationManager.IMPORTANCE_HIGH
        );

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();

        channel.enableVibration(true);
        channel.enableLights(true);
        channel.canBypassDnd();
        channel.setVibrationPattern(vibratePattern);
        channel.setSound(alarmSound, audioAttributes);

        getManager().createNotificationChannel(channel);
    }

    public Notification getTimerNotification(long timeRemaining){
        PendingIntent pendingIntent = getNotificationPendingIntent();

        return new NotificationCompat.Builder(this, TIMER_CHANNEL_ID)
                .setContentTitle(getString(R.string.timer_notification_title))
                .setContentText(timerLogic.getTimerText(timeRemaining))
                .setSmallIcon(R.drawable.timer_service_icon)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
    }

    public void generatePauseNotification() {
        PendingIntent pendingIntent = getNotificationPendingIntent();

        Notification notification = new NotificationCompat.Builder(this, TIMER_CHANNEL_ID)
                .setContentTitle(getString(R.string.timer_notification_title))
                .setContentText("Timer Paused")
                .setSmallIcon(R.drawable.timer_service_icon)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public Notification getAlarmNotification() {
        // Forces Heads-up notification to stay open with this dummy intent
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), ALARM_CHANNEL_ID)
                .setContentTitle("Alarm")
                .setContentText("Timeout is Over!")
                .setAutoCancel(false)
                .setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_ALARM)
                .setFullScreenIntent(pendingIntent, true)
                .setSmallIcon(R.drawable.ic_baseline_timer_24);

        Notification notification = notificationBuilder.build();
        notification.flags = Notification.FLAG_INSISTENT; // Loops notification sound

        return notification;
    }

    private PendingIntent getNotificationPendingIntent() {
        Intent notificationIntent = new Intent(this, Timer.class);

        return PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }
}
