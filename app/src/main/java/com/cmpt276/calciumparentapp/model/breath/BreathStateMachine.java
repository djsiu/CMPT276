package com.cmpt276.calciumparentapp.model.breath;

import android.os.Handler;

import com.cmpt276.calciumparentapp.ui.breath.TakeBreath;

public class BreathStateMachine {
    public final State inhaleState;
    public final State exhaleState;
    public final State selectionState;
    private State currentState;


    public BreathStateMachine(TakeBreath context) {
        inhaleState = new inhaleState(context);
        exhaleState = new exhaleState(context);
        selectionState = new selectionState(context);
        currentState = new idleState(context);
    }

    public void buttonPressed() {
        currentState.handleButtonPressed();
    }
    public void buttonReleased() {
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

        public exhaleState(TakeBreath context) {
            super(context);
        }

        Handler timerHandler = new Handler();

        // 3 second timer
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                context.setButtonTextIn(); // maybe move to activity?
                setState(inhaleState);
            }
        };

        @Override
        void handleEnter() {
            this.context.setButtonTextOut();
            this.context.shrinkCircle();

            //TODO: start exhale sound

            this.context.exhaleHelpMessage();

            // timerHandler code
            timerHandler.removeCallbacks(timerRunnable);
            timerHandler.postDelayed(timerRunnable, 3000);
        }

        @Override
        void handleExit() {
            this.context.breathTaken();
        }

        @Override
        void handleButtonPressed() {
        }

        @Override
        void handleButtonReleased() {
        }
    }

    private class inhaleState extends State {

        private boolean threeSecPassed = false;
        private boolean buttonPressed = false;

        Handler timerHandler = new Handler();
        // 3 second timer
        Runnable timerRunnable = () -> {
            threeSecPassed = true;
            context.threeSecOfInhale();
            context.setButtonTextOut();
        };

        // 10 second timer
        Runnable timerRunnable2 = () -> {
            context.tenSecOfInhale();
        };

        // 7 second timer - for handling breathe out for remaining 7 seconds
        Runnable timerRunnable3 = () -> {
            if(!buttonPressed) {
                context.cancelCircleAnimation();
                context.resetCircle();
                //TODO: stop exhale sound
            }
        };

        public inhaleState(TakeBreath context) {
            super(context);
        }

        @Override
        void handleEnter() {
            this.context.setButtonTextIn();
            this.context.inhaleHelpMessage();
            timerHandler.postDelayed(timerRunnable3, 7000);
        }

        @Override
        void handleExit() {
            this.context.cancelCircleAnimation();
        }

        @Override
        void handleButtonReleased() {
            timerHandler.removeCallbacks(timerRunnable);
            timerHandler.removeCallbacks(timerRunnable2);

            if(threeSecPassed) {
                setState(exhaleState);
            } else {
                this.context.resetCircle();
                setState(inhaleState);
            }
            // TODO: stop inhale sound
        }

        @Override
        void handleButtonPressed() {
            buttonPressed = true;

            // TODO: stop exhale sound
            // TODO: start inhale sound
            this.context.cancelCircleAnimation();
            this.context.resetCircle();
            this.context.growCircle();

            // timerHandler Code
            timerHandler.removeCallbacks(timerRunnable);
            timerHandler.removeCallbacks(timerRunnable2);
            timerHandler.postDelayed(timerRunnable, 3000);
            timerHandler.postDelayed(timerRunnable2, 10000);
        }
    }

    private class selectionState extends State {

        public selectionState(TakeBreath context) {
            super(context);
        }

        @Override
        void handleEnter() {
        }

        @Override
        void handleExit() {
        }

        @Override
        void handleButtonReleased() {
        }

        @Override
        void handleButtonPressed() {
            this.context.beginBreaths();
            setState(inhaleState);
        }
    }

    // Use "Null Object" pattern: This class, does nothing! It's like a safe null
    private class idleState extends State {
        public idleState(TakeBreath context) {
            super(context);
        }
    }
}
