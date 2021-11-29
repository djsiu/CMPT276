package com.cmpt276.calciumparentapp.ui.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.timer.TimerLogic;

public class TimerChangeSpeed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_change_speed);
        setupButtons();
    }

    private void setupButtons() {
        Button btnMul25 = findViewById(R.id.speed_mul_btn_0_25);
        Button btnMul50 = findViewById(R.id.speed_mul_btn_0_50);
        Button btnMul75 = findViewById(R.id.speed_mul_btn_0_75);
        Button btnMul100 = findViewById(R.id.speed_mul_btn_1);
        Button btnMul200 = findViewById(R.id.speed_mul_btn_2);
        Button btnMul300 = findViewById(R.id.speed_mul_btn_3);
        Button btnMul400 = findViewById(R.id.speed_mul_btn_4);

        btnMul25.setOnClickListener(new ButtonListener());
        btnMul50.setOnClickListener(new ButtonListener());
        btnMul75.setOnClickListener(new ButtonListener());
        btnMul100.setOnClickListener(new ButtonListener());
        btnMul200.setOnClickListener(new ButtonListener());
        btnMul300.setOnClickListener(new ButtonListener());
        btnMul400.setOnClickListener(new ButtonListener());
    }


    private class ButtonListener implements View.OnClickListener {
        // prevent IDE from complaining about ID
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            TimerLogic timerLogic = TimerLogic.getInstance();
            switch(v.getId()) {
                case R.id.speed_mul_btn_0_25:
                    timerLogic.setSpeedMultiplier(0.25);
                    break;

                case R.id.speed_mul_btn_0_50:
                    timerLogic.setSpeedMultiplier(0.5);
                    break;

                case R.id.speed_mul_btn_0_75:
                    timerLogic.setSpeedMultiplier(0.75);
                    break;

                case R.id.speed_mul_btn_1:
                    timerLogic.setSpeedMultiplier(1);
                    break;

                case R.id.speed_mul_btn_2:
                    timerLogic.setSpeedMultiplier(2);
                    break;

                case R.id.speed_mul_btn_3:
                    timerLogic.setSpeedMultiplier(3);
                    break;

                case R.id.speed_mul_btn_4:
                    timerLogic.setSpeedMultiplier(4);
                    break;
            }

            finish();
        }
    }
}