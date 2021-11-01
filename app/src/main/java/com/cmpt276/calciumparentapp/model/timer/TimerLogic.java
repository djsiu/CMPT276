package com.cmpt276.calciumparentapp.model.timer;


public class TimerLogic {
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
