package com.cmpt276.calciumparentapp.model.breath;

/*
 for the user to decide on the number of breaths before starting
 also limiting the number of breaths to >= 1 and <= 10
*/

//TODO: implement shared preferences to save the num of breaths chosen last

public class CreateBreaths {

    public final static int MAX_NUM_BREATHS = 10;
    public final static int MIN_NUM_BREATHS = 1;
    private int numOfBreaths;

    public CreateBreaths() {
        numOfBreaths = 1;
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
}
