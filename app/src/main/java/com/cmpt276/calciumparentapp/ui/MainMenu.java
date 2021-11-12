package com.cmpt276.calciumparentapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.ui.coinflip.CoinFlipSelection;
import com.cmpt276.calciumparentapp.ui.manage.ManageFamilyMembers;
import com.cmpt276.calciumparentapp.ui.timer.TimerSelectTime;

/**
 * Main menu activity connects
 */
public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button btnCoinFlip = findViewById(R.id.main_menu_left_button);
        Button btnTimer = findViewById(R.id.main_menu_right_button);
        Button btnFamManage = findViewById(R.id.main_menu_top_button);
        Button btnHelp = findViewById(R.id.main_menu_help_button);

        setupCoinFlipButton(btnCoinFlip);
        setupTimerButton(btnTimer);
        setupFamilyManageButton(btnFamManage);
        setupHelpButton(btnHelp);

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
    public void setupHelpButton(Button button) {
        button.setOnClickListener(v -> {
            // Opens the ManageFamilyMembers activity
            Intent i = HelpScreen.makeIntent(MainMenu.this);
            startActivity(i);
        });
    }
}