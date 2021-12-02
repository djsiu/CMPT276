package com.cmpt276.calciumparentapp.model.timer;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Singleton class that handles the timer logic
 */
public class TimerLogic {
    private static TimerLogic instance;
    private long ms = 60000;
    private double speedMultiplier;
    private static final double DEFAULT_SPEED_MUL = 1;

    // Singleton support
    public static TimerLogic getInstance(){
        if(instance == null){
            instance = new TimerLogic();
        }

        return instance;
    }

    // Private constructor for singleton
    private TimerLogic(){
        speedMultiplier = DEFAULT_SPEED_MUL;
    }

    // Sets the length of the timer in milliseconds
    public void setTimerLength(long ms){
        this.ms = ms;
    }

    public void setSpeedMultiplier(double mul) {
        speedMultiplier = mul;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public long getTimerLength(){
        return ms;
    }

    /**
     * Resets the speed multiplier to its default value
     */
    public void resetSpeedMultiplier() {
        speedMultiplier = DEFAULT_SPEED_MUL;
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

    // Function from:
    // https://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android
    //
    // Google deprecated 'getRunningServices' because of security issues
    // There isn't a replacement for this implementation so I'm just suppressing the warning
    @SuppressWarnings("deprecation")
    public boolean isTimerServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (TimerService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
