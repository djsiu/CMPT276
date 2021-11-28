package com.cmpt276.calciumparentapp.ui.breath;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.breath.CreateBreaths;
import com.cmpt276.calciumparentapp.model.breath.TakeBreathSession;

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
