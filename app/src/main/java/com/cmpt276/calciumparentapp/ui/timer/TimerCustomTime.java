package com.cmpt276.calciumparentapp.ui.timer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.TimerLogic;

import java.util.Objects;

/**
 * UI for Setting Custom Length of Timer
 */
public class TimerCustomTime extends AppCompatActivity {

    private EditText customTimeEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_custom_time);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        customTimeEditText = findViewById(R.id.customTimeEditText);
        setupButtons();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupButtons() {
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isInputValid()){
                    setTime();
                    startTimerActivity();
                }
                else{
                    displayInvalidTextError();
                }
            }
        });
    }

    private void displayInvalidTextError() {
        Toast errorToast = Toast.makeText(this, R.string.invalid_custom_time_toast, Toast.LENGTH_SHORT);
        errorToast.show();

    }

    // Checks if the value entered into the editText is valid
    private boolean isInputValid() {
        if(customTimeEditText.getText().length() != 0){
            int time = Integer.parseInt(customTimeEditText.getText().toString());
            if(time != 0){
                return true;
            }
        }

        return false;
    }

    private void setTime() {
        long time = Integer.parseInt(customTimeEditText.getText().toString());
        time = time * 1000 * 60; // minutes to milliseconds
        TimerLogic.getInstance().setTimerLength(time);
    }

    private void startTimerActivity() {
        Intent intent = new Intent(this, Timer.class);
        finish();
        startActivity(intent);
    }




}