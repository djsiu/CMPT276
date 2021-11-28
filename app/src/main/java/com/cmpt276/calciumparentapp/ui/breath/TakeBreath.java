package com.cmpt276.calciumparentapp.ui.breath;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    }

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
            stateMachine.buttonPressed();
        });
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stateMachine.buttonPressed();
                        return true;
                    case MotionEvent.ACTION_UP:
                        stateMachine.buttonReleased();
                        return true;
                }
                return false;
            }
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
