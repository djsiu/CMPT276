package com.cmpt276.calciumparentapp.ui.breath;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.breath.BreathsManager;
import com.cmpt276.calciumparentapp.model.breath.TakeBreathSession;
import com.cmpt276.calciumparentapp.model.breath.BreathStateMachine;

/*
This activity is for the user to decide the number of breaths and then begin their breaths
the main button is used when breathing in and to begin the breaths.
 */
public class TakeBreath extends AppCompatActivity {

    TextView numBreathsText;
    Button breathBtn;
    Button addBreathBtn;
    Button minusBreathBtn;
    BreathStateMachine stateMachine;
    BreathsManager createBreaths;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        createBreaths = new BreathsManager();

        numBreathsText = findViewById(R.id.number_of_breaths_text_view);
        numBreathsText.setText("" + createBreaths.getNumOfBreaths());

        addBreathBtn = findViewById(R.id.add_breaths_btn);
        minusBreathBtn = findViewById(R.id.minus_breath_btn);

        setupAddBreathBtn(addBreathBtn);
        setupMinusBreathBtn(minusBreathBtn);

        stateMachine = new BreathStateMachine(this);
        stateMachine.setState(stateMachine.selectionState);

        // setting up the breath button
        breathBtn = findViewById(R.id.button_tester);
        breathBtn.setText("begin"); //TODO: un-hardcode
        breathBtn.setOnTouchListener((view, motionEvent) -> {
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stateMachine.buttonPressed();
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "This is a message displayed in a Toast",
//                            Toast.LENGTH_SHORT);
//
//                    toast.show();
                    return true;
                case MotionEvent.ACTION_UP:
                    stateMachine.buttonReleased();
//                    toast = Toast.makeText(getApplicationContext(),
//                            "released",
//                            Toast.LENGTH_SHORT);
//
//                    toast.show();
                    return true;
            }
            return false;
        });
    }

    /**
     * setting up and managing buttons
     */

    public void setupAddBreathBtn(Button button) {
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            createBreaths.addBreath();
            numBreathsText.setText("" + createBreaths.getNumOfBreaths());
        });
    }

    public void setupMinusBreathBtn(Button button) {
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            createBreaths.minusBreath();
            numBreathsText.setText("" + createBreaths.getNumOfBreaths());
        });
    }

    public void setButtonTextIn() {
        breathBtn.setText("in"); //TODO: un-hardcode
    }

    public void setButtonTextOut() {
        breathBtn.setText("out");
    }

    public void beginBreaths() {
        //createBreaths.getNumOfBreaths();
        addBreathBtn.setVisibility(View.GONE);
        minusBreathBtn.setVisibility(View.GONE);
    }

    /**
     * managing toasts
     */

    public void threeSecOfInhale() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "You can breath out now.",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void tenSecOfInhale() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Take you finger off to breathe out.",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void exhaleHelpMessage() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Breathe out now!",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void breathTaken() {
        //hide header
        TextView titleText = findViewById(R.id.number_of_breaths_label_text);
        titleText.setVisibility(View.GONE);

        if(createBreaths.getNumOfBreaths() > 1) {
            createBreaths.breathTaken();
            numBreathsText.setText(createBreaths.getNumOfBreaths() + " breaths remaining!"); //TODO: un-hardcode
        } else {
            numBreathsText.setText("Good job!");
        }
    }

    /**
     * animation code
     */

    public void growCircle(){
        ImageView imageView = findViewById(R.id.imageView_breathIcon);
        imageView.animate()
                .setDuration(10000)
                .scaleX(1)
                .scaleY(1)
                .setListener(new breathAnimationListener())
                .start();
    }
    public void cancelCircleAnimation(){
        ImageView imageView = findViewById(R.id.imageView_breathIcon);
        imageView.animate().cancel();
    }
    public void resetCircle(){
        ImageView imageView = findViewById(R.id.imageView_breathIcon);
        imageView.setScaleX((float) 0.3);
        imageView.setScaleY((float) 0.3);
    }
    public void shrinkCircle(){
        ImageView imageView = findViewById(R.id.imageView_breathIcon);
        imageView.animate()
                .setDuration(10000)
                .scaleX((float) 0.3)
                .scaleY((float) 0.3)
                .setListener(new breathAnimationListener())
                .start();
    }


    /**
     * The listener used by the coin flip animation
     * Handles setting the image of the coin when the animation is over
     * and preventing the buttons from being clicked while the animation is running
     */
    private class breathAnimationListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) { }

        @Override
        public void onAnimationEnd(Animator animation) { }

        @Override
        public void onAnimationCancel(Animator animation) { }

        @Override
        public void onAnimationRepeat(Animator animation) { }
    }

    /**
     * Adds logic to action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Top left back arrow
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, TakeBreath.class);
    }
}
