package com.cmpt276.calciumparentapp.ui.manage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.ui.MainMenu;
import com.cmpt276.calciumparentapp.ui.coinflip.CoinFlip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManageFamilyMembers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_members);

        FloatingActionButton btnAddMember = findViewById(R.id.manage_family_add_button);

        setupManageFamilyAddButton(btnAddMember);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupManageFamilyAddButton(FloatingActionButton button) {
        button.setOnClickListener(v -> {
            // Opens the ManageFamilyAdd activity
            Intent i = ManageFamilyAdd.makeIntent(ManageFamilyMembers.this);
            startActivity(i);
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
        return new Intent(context, ManageFamilyMembers.class);
    }

}