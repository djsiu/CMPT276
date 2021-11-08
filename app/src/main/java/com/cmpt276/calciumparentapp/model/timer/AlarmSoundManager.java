package com.cmpt276.calciumparentapp.model.timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import java.io.IOException;

public class AlarmSoundManager {
    // By using getApplicationContext in the singleton the memory leak is fixed
    @SuppressLint("StaticFieldLeak")
    private static AlarmSoundManager instance;
    private final Context context;
    private final MediaPlayer mp = new MediaPlayer();

    private AlarmSoundManager(Context context){
        this.context = context;
    }

    // Singleton support
    public static AlarmSoundManager getInstance(Context context){
        if(instance == null){
            instance = new AlarmSoundManager(context.getApplicationContext());
        }

        return instance;
    }

    public void startAlarmSound() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); // Gets system alarm sound

        mp.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build());

        try {
            mp.setDataSource(context, alarmSound);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!mp.isPlaying()) {
            mp.start();
            mp.setLooping(true);
        }
    }

    public void stopAlarmSound() {
        mp.stop();
    }
}
