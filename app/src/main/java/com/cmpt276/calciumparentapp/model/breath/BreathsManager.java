package com.cmpt276.calciumparentapp.model.breath;

/*
 for the user to decide on the number of breaths before starting
 also limiting the number of breaths to >= 1 and <= 10

 also for keep track of number of breaths remaining?
*/

//TODO: implement shared preferences to save the num of breaths chosen last

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class BreathsManager {

    public final static int MAX_NUM_BREATHS = 10;
    public final static int MIN_NUM_BREATHS = 1;
    private int numOfBreaths;

    private Context context;

    public BreathsManager(Context context) {
        this.context = context;
        numOfBreaths = getBreathsFromSharedPrefs();
    }

    public int getNumOfBreaths() {
        return numOfBreaths;
    }

    // if breaths are <= max breaths, allow a breath to be added
    public boolean addBreath() {
        if(numOfBreaths < MAX_NUM_BREATHS) {
            numOfBreaths++;
            return true;
        }
        return false;
    }

    // if breaths are >= min breaths, allow a breath to be minused
    public boolean minusBreath() {
        if(numOfBreaths > MIN_NUM_BREATHS) {
            numOfBreaths--;
            return true;
        }
        return false;
    }

    public void breathTaken() {
        numOfBreaths--;
    }

    public void saveBreathsToSharedPrefs() {

        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Number of breaths", getNumOfBreaths());
        editor.apply();
    }

    public int getBreathsFromSharedPrefs() {

        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getInt("Number of breaths", 1);
    }
}
