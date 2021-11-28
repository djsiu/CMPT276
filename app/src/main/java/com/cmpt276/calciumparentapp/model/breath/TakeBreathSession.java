package com.cmpt276.calciumparentapp.model.breath;

/*
holding the states and counting remaining breaths
 */

public class TakeBreathSession {

    private int numOfBreaths;

    //states
    public static final int BREATH_MENU = 0;
    public static final int BREATHING_IN = 1;
    public static final int BREATHING_OUT = 2;
    public static final int DONE = 3;
    private int state;
    //Beginning state is breath_menu
    public TakeBreathSession() {
        state = BREATH_MENU;
    }

    public void setBreathsAndBegin(int numBreaths) {
        state = BREATHING_IN;
        numOfBreaths = numBreaths;
    }

    public void breathIn() {
            numOfBreaths--;
            state = BREATHING_OUT;
    }

    public void breathOut() {
        if(numOfBreaths >= CreateBreaths.MIN_NUM_BREATHS) {
            state = BREATHING_IN;    // need to change to check if there are any breaths remaining
        } else {
            state = DONE;
        }
    }

    public int getNumOfBreaths() {
        return numOfBreaths;
    }

    public int getState() {
        return state;
    }
}
