package com.cmpt276.calciumparentapp.model.timer;

import static com.cmpt276.calciumparentapp.ui.timer.Timer.CHANNEL_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.core.app.NotificationCompat;

import com.cmpt276.calciumparentapp.R;

public class NotificationHelper extends ContextWrapper {

    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
    }

    // Creates the notification channel for the required versions of android
    // May need to be called only once for the whole application in which case this needs to be
    // moved to the main activity
    public void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Timer Service Channel",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
        );
        serviceChannel.enableVibration(false);
        serviceChannel.enableLights(false);
        serviceChannel.setSound(null, null);

        android.app.NotificationManager manager = getSystemService(android.app.NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

    public NotificationCompat.Builder getChannelNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("Alarm")
                .setContentText("Timeout is Over!")
                .setSmallIcon(R.drawable.ic_baseline_timer_24);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }
}
