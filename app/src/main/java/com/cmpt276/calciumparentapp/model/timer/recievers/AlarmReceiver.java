package com.cmpt276.calciumparentapp.model.timer.recievers;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cmpt276.calciumparentapp.model.timer.AlarmSoundManager;
import com.cmpt276.calciumparentapp.model.timer.TimerNotifications;

/**
 * Receives then displays alarm notifications
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmSoundManager.getInstance(context).startAlarmSound();

        TimerNotifications timerNotifications = new TimerNotifications(context);
        Notification alarmNotification = timerNotifications.getAlarmNotification();
        timerNotifications.getManager().notify(1, alarmNotification);
    }
}