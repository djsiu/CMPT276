package com.cmpt276.calciumparentapp.model.timer.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cmpt276.calciumparentapp.model.timer.AlarmSoundManager;

public class AlarmDismiss extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmSoundManager.getInstance(context).stopAlarmSound();
    }
}
