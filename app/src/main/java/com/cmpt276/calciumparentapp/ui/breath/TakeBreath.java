package com.cmpt276.calciumparentapp.ui.breath;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.breath.BreathsManager;
import com.cmpt276.calciumparentapp.model.breath.BreathStateMachine;

/*
This activity is for the user to decide the number of breaths and then begin their breaths
the main button is used when breathing in and to begin the breaths.
 */
public class TakeBreath extends AppCompatActivity {

    TextView numBreathsText;
    Button breatheBtn;
    Button addBreathBtn;
    Button minusBreathBtn;
    //play sound
    MediaPlayer mediaPlayer;

    TextView messageTextView;

    BreathStateMachine stateMachine;
    BreathsManager breathsManager;
    ImageView breathImageView;

    String numOfBreathsTxt;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);


        messageTextView = findViewById(R.id.number_of_breaths_label_text);
        setupBreathCount();

        //set up mediaPlayer
        breathImageView = findViewById(R.id.imageView_breathIcon);
        mediaPlayer = MediaPlayer.create(this, R.raw.coin_flip_sound);
        // setting up the breath button

        breatheBtn.setOnTouchListener((view, motionEvent) -> {
            switch(motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    stateMachine.buttonPressed();
                    return true;

                case MotionEvent.ACTION_UP:
                    stateMachine.buttonReleased();
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
            breathsManager.addBreath();

            numOfBreathsTxt = "" + breathsManager.getNumOfBreaths();
            numBreathsText.setText(numOfBreathsTxt);
            breathsManager.saveBreathsToSharedPrefs();
        });
    }

    public void setupBreathCount(){
        breathsManager = new BreathsManager(this);
        stateMachine = new BreathStateMachine(this);
        stateMachine.setState(stateMachine.selectionState);

        numBreathsText = findViewById(R.id.number_of_breaths_text_view);

        //set up breath text
        numOfBreathsTxt = "" + breathsManager.getBreathsFromSharedPrefs();
        numBreathsText.setText(numOfBreathsTxt);
        // set up buttons
        addBreathBtn = findViewById(R.id.add_breaths_btn);
        minusBreathBtn = findViewById(R.id.minus_breath_btn);
        breatheBtn = findViewById(R.id.breathe_button);

        setupAddBreathBtn(addBreathBtn);
        setupMinusBreathBtn(minusBreathBtn);

        breatheBtn.setText(R.string.begin_btn_text);
        breatheBtn.setClickable(true);
        TextView textView = findViewById(R.id.number_of_breaths_label_text);
        textView.setText(R.string.num_breaths_text);
    }


    public void setupMinusBreathBtn(Button button) {
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            breathsManager.minusBreath();

            numOfBreathsTxt = "" + breathsManager.getNumOfBreaths();
            numBreathsText.setText(numOfBreathsTxt);
            breathsManager.saveBreathsToSharedPrefs();
        });
    }

    public void disableBreathBtn() {
        breatheBtn.setEnabled(false);
    }

    public void enableBreathBtn() {
        breatheBtn.setEnabled(true);
    }

    public void setButtonTextIn() {
        breatheBtn.setText(R.string.in_btn_text);
    }

    public void setButtonTextOut() {
        breatheBtn.setText(R.string.out_btn_text);
    }

    public void removePlusMinusBtns() {
        addBreathBtn.setVisibility(View.GONE);
        minusBreathBtn.setVisibility(View.GONE);
    }




    /**
     * managing help messages
     */

    public void threeSecOfInhale() {

        messageTextView.setText(R.string.allow_exhale_toast);
    }

    public void tenSecOfInhale() {
        messageTextView.setText(R.string.stop_inhale_toast);
    }

    public void exhaleHelpMessage() {
        if(breathsManager.getNumOfBreaths()>1){
            messageTextView.setText(R.string.exhale_toast);
        }
    }

    public void inhaleHelpMessage() {
        messageTextView.setText(R.string.inhale_help_toast);
    }

    /**
     * manage textviews
     */

    public void breathTaken() {

        if(breathsManager.getNumOfBreaths() > 1) {
            breathsManager.breathTaken();
            String remainBreathsString = breathsManager.getNumOfBreaths() + getString(R.string.breath_remaining_message);
            numBreathsText.setText(remainBreathsString);
            stateMachine.setState(stateMachine.inhaleState);
        } else {
            messageTextView.setText("");
            numBreathsText.setText("");
            breatheBtn.setText(R.string.good_job_message);

        }
    }
    public boolean isMoreBreaths(){
        return (breathsManager.getNumOfBreaths() >= 1);
    }


    /**
     * sound implementation
     */
    public void startInhaleSound(){
        //play sound
        mediaPlayer = MediaPlayer.create(this, R.raw.inhale_fade);
        mediaPlayer.start();
    }
    public void cancelInhaleSound(){
        mediaPlayer.stop();
    }
    public void startExhaleSound(){
        //play sound
        mediaPlayer = MediaPlayer.create(this, R.raw.exhale_fade);
        mediaPlayer.start();
    }
    public void cancelExhaleSound(){
        mediaPlayer.stop();
    }

    /**
     * animation implementation
     */

    public void growCircle(){
        setImageInhale();
        breathImageView.animate()
                .setDuration(10000)
                .scaleX(1)
                .scaleY(1)
                .setListener(new breathAnimationListener())
                .start();
    }
    public void cancelCircleAnimation(){
        breathImageView.animate().cancel();
    }
    public void resetCircle(){
        ImageView imageView = findViewById(R.id.imageView_breathIcon);
        imageView.setScaleX((float) 0.3);
        imageView.setScaleY((float) 0.3);
    }
    public void shrinkCircle(){
        setImageExhale();
        breathImageView.animate()
                .setDuration(10000)
                .scaleX((float) 0.3)
                .scaleY((float) 0.3)
                .setListener(new breathAnimationListener())
                .start();
    }

    public void setImageInhale(){
        breathImageView.setImageResource(R.drawable.breath_circle);

    }

    public void setImageExhale(){
        breathImageView.setImageResource(R.drawable.exhale_circle);
    }

    /**
     * The listener used by the coin flip animation
     * Handles setting the image of the coin when the animation is over
     * and preventing the buttons from being clicked while the animation is running
     */
    private static class breathAnimationListener implements Animator.AnimatorListener {

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
