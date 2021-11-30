package com.cmpt276.calciumparentapp.model.breath;

import android.os.Handler;

import com.cmpt276.calciumparentapp.ui.breath.TakeBreath;

public class breathStateMachine {
    public final State inhaleState;
    public final State exhaleState;
    private State currentState;


    public breathStateMachine(TakeBreath context) {
        inhaleState = new inhaleState(context);
        exhaleState = new exhaleState(context);
        currentState = new idleState(context);
    }

    public void buttonPressed(){
        currentState.handleButtonPressed();
    }
    public void buttonReleased(){
        currentState.handleButtonReleased();
    }


    // State Pattern's base states
    private abstract class State {

        public TakeBreath context;
        public State(TakeBreath context) {
            this.context = context;
        }


        // Empty implementations, so derived class don't need to
        // override methods they don't care about.
        void handleEnter() {}
        void handleExit() {}
        void handleButtonPressed() {}
        void handleButtonReleased() {}
    }



    public void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    // ************************************************************
    // State Pattern states
    // ************************************************************
    private class exhaleState extends State {

        private int count = 0;

        public exhaleState(TakeBreath context) {
            super(context);
        }

        @Override
        void handleEnter() {

        }

        @Override
        void handleExit() {
        }

        @Override
        void handleButtonPressed() {
            this.context.shrinkCircle();
        }

        @Override
        void handleButtonReleased() {
            this.context.cancelCircleAnimation();
            this.context.resetCircle();
            setState(inhaleState);
        }
    }


    private class inhaleState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                //setState(offState);
            }
        };

        public inhaleState(TakeBreath context) {
            super(context);
        }

        @Override
        void handleEnter() {
            timerHandler.postDelayed(timerRunnable, 3000);
            timerHandler.postDelayed(timerRunnable, 10000);

        }

        @Override
        void handleExit() {
            timerHandler.removeCallbacks(timerRunnable);
        }

        @Override
        void handleButtonReleased() {
            this.context.cancelCircleAnimation();
            setState(exhaleState);
        }

        @Override
        void handleButtonPressed() {
            // Reset the timer
            this.context.growCircle();

            //Additional timerHandler Code
            timerHandler.removeCallbacks(timerRunnable);
            timerHandler.postDelayed(timerRunnable, 2000);
        }
    }




    // Use "Null Object" pattern: This class, does nothing! It's like a safe null
    private class idleState extends State {
        public idleState(TakeBreath context) {
            super(context);
        }
    }


}
