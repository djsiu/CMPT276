package com.cmpt276.calciumparentapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.ui.coinflip.CoinFlip;
import com.cmpt276.calciumparentapp.ui.coinflip.CoinFlipSelection;
import com.cmpt276.calciumparentapp.ui.manage.ManageFamilyMembers;
import com.cmpt276.calciumparentapp.ui.timer.Timer;
import com.cmpt276.calciumparentapp.ui.timer.TimerSelectTime;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button btnCoinFlip = findViewById(R.id.main_menu_left_button);
        Button btnTimer = findViewById(R.id.main_menu_right_button);
        Button btnFamManage = findViewById(R.id.main_menu_top_button);

        setupCoinFlipButton(btnCoinFlip);
        setupTimerButton(btnTimer);
        setupFamilyManageButton(btnFamManage);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disables Dark Mode
    }

    public void setupCoinFlipButton(Button button) {
        button.setOnClickListener(v -> {
            // Opens the CoinFlip activity
            Intent i = CoinFlipSelection.makeIntent(MainMenu.this);
            startActivity(i);
        });
    }

    public void setupTimerButton(Button button) {
        button.setOnClickListener(v -> {
            // Opens the Timer activity
            Intent i = TimerSelectTime.makeIntent(MainMenu.this);
            startActivity(i);
        });
    }

    public void setupFamilyManageButton(Button button) {
        button.setOnClickListener(v -> {
            // Opens the ManageFamilyMembers activity
            Intent i = ManageFamilyMembers.makeIntent(MainMenu.this);
            startActivity(i);
        });
    }
}