package com.cmpt276.calciumparentapp.model.breath;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *  Decides the number of breaths before starting,
 *  limits the number of breaths to >= 1 and <= 10,
 *  and keeps track of the number of breaths remaining
 */
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
    public void addBreath() {
        if(numOfBreaths < MAX_NUM_BREATHS) {
            numOfBreaths++;
        }
    }

    // if breaths are >= min breaths, allow a breath to be minused
    public void minusBreath() {
        if(numOfBreaths > MIN_NUM_BREATHS) {
            numOfBreaths--;
        }
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
