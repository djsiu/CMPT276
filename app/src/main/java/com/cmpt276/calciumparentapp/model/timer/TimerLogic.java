package com.cmpt276.calciumparentapp.model.timer;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Singleton class that handles the timer logic
 */
public class TimerLogic {

    private static TimerLogic instance;
    private long ms = 60000;

    // Singleton support
    public static TimerLogic getInstance(){
        if(instance == null){
            instance = new TimerLogic();
        }

        return instance;
    }

    // Private constructor for singleton
    private TimerLogic(){

    }

    // Sets the length of the timer in milliseconds
    public void setTimerLength(long ms){
        this.ms = ms;
    }

    public long getTimerLength(){
        return ms;
    }

    public String getTimerText(long timeLeftInMS){
        int minutes = (int) timeLeftInMS / 60000;
        int seconds = (int) timeLeftInMS % 60000 / 1000;

        String timeLeftText;
        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        return timeLeftText;
    }
}
