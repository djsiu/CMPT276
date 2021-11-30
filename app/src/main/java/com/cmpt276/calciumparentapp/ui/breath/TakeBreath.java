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
import com.cmpt276.calciumparentapp.model.breath.CreateBreaths;
import com.cmpt276.calciumparentapp.model.breath.TakeBreathSession;
import com.cmpt276.calciumparentapp.model.breath.breathStateMachine;

/*
This activity is for the user to decide the number of breaths and then begin their breaths
the main button is used when breathing in and to begin the breaths.
 */
public class TakeBreath extends AppCompatActivity {

    TakeBreathSession breathSession;
    CreateBreaths createBreaths;

    TextView numBreathsText;
    Button mainBtn;
    Button addBreathBtn;
    Button minusBreathBtn;
    breathStateMachine stateMachine;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        breathSession = new TakeBreathSession(); ////////////////////
        createBreaths = new CreateBreaths();

        numBreathsText = findViewById(R.id.number_of_breaths_text_view);
        numBreathsText.setText("" + createBreaths.getNumOfBreaths());

        mainBtn = findViewById(R.id.take_breath_begin_btn);
        addBreathBtn = findViewById(R.id.add_breaths_btn);
        minusBreathBtn = findViewById(R.id.minus_breath_btn);

        setupMainBtn(mainBtn);
        setupAddBreathBtn(addBreathBtn);
        setupMinusBreathBtn(minusBreathBtn);
        stateMachine = new breathStateMachine(this);
        stateMachine.setState(stateMachine.inhaleState);

        Button button = findViewById(R.id.button_tester);
        button.setOnTouchListener((view, motionEvent) -> {
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stateMachine.buttonPressed();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "This is a message displayed in a Toast",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    return true;
                case MotionEvent.ACTION_UP:
                    stateMachine.buttonReleased();
                    toast = Toast.makeText(getApplicationContext(),
                            "released",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    return true;
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupMainBtn(Button button) {

        // change button depending on state
        // and perform actions depending on state
        button.setOnClickListener(v -> {
            if (breathSession.getState() == TakeBreathSession.BREATH_MENU) {

                breathSession.setBreathsAndBegin(createBreaths.getNumOfBreaths());
                addBreathBtn.setVisibility(View.GONE);
                minusBreathBtn.setVisibility(View.GONE);

            } else if (breathSession.getState() == TakeBreathSession.BREATHING_IN) {
                breathSession.breathIn();
            } else if (breathSession.getState() == TakeBreathSession.BREATHING_OUT) {
                breathSession.breathOut();
            }

            if (breathSession.getState() == TakeBreathSession.DONE) {
                mainBtn.setVisibility(View.GONE);
            }
            updateBtnText();
            updateBreathsText();
        });

    }

    // TODO: un-hardcode the strings.
    private void updateBtnText() {
        if(breathSession.getState() == TakeBreathSession.BREATH_MENU) {
            mainBtn.setText("begin");
        } else if (breathSession.getState() == TakeBreathSession.BREATHING_IN) {
            mainBtn.setText("in");
        } else if (breathSession.getState() == TakeBreathSession.BREATHING_OUT) {
            mainBtn.setText("out");
        }
    }

    private void updateBreathsText() {
        numBreathsText.setText(breathSession.getNumOfBreaths() + " breaths remaining!");
    }

    public void setupAddBreathBtn(Button button) {
        button.setOnClickListener(v -> {
            createBreaths.addBreath();
            numBreathsText.setText("" + createBreaths.getNumOfBreaths());
        });
    }

    public void setupMinusBreathBtn(Button button) {
        button.setOnClickListener(v -> {
            createBreaths.minusBreath();
            numBreathsText.setText("" + createBreaths.getNumOfBreaths());
        });
    }

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
