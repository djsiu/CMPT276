package com.cmpt276.calciumparentapp.model.timer;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

/**
 * Receives then displays alarm notifications
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TimerNotifications timerNotifications = new TimerNotifications(context);
        Notification notification = timerNotifications.getAlarmNotification();
        timerNotifications.getManager().notify(1, notification);
    }
}