package com.cmpt276.calciumparentapp.model.timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import java.io.IOException;

/**
 * Singleton that handles alarm sound logic
 */
public class AlarmSoundManager {
    // By using getApplicationContext in the singleton the memory leak is fixed
    @SuppressLint("StaticFieldLeak")
    private static AlarmSoundManager instance;
    private final MediaPlayer mediaPlayer = new MediaPlayer();

    private AlarmSoundManager(Context context){

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); // Gets system alarm sound
        mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build());

        try {
            mediaPlayer.setDataSource(context, alarmSound);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Singleton support
    public static AlarmSoundManager getInstance(Context context){
        if(instance == null){
            instance = new AlarmSoundManager(context.getApplicationContext());
        }
        return instance;
    }

    public void startAlarmSound() {

        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }

    public void stopAlarmSound() {
        mediaPlayer.stop();

        // After the mediaPlayer is stopped, prepare needs to be called again before
        // its in a state where it can be played again.
        // See the state diagram: https://developer.android.com/reference/android/media/MediaPlayer#StateDiagram
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
