package com.cmpt276.calciumparentapp.model.breath;

public class TakeBreathLogic {
    private int totalBreaths, remainingBreaths;

    public TakeBreathLogic(int totalBreaths) {
        this.totalBreaths = totalBreaths;
        this.remainingBreaths = this.totalBreaths;
    }

    public void breathIn() {
        remainingBreaths--;
    }

    public int getRemainingBreaths() {
        return remainingBreaths;
    }
}
