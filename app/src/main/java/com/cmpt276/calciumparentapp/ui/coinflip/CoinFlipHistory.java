package com.cmpt276.calciumparentapp.ui.coinflip;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.ui.manage.ManageFamilyEdit;

/*
    NEEDS TO DISPLAY:
    - date + time of flip
    - name of picker
    - what flip came up
    - won or lost icon
 */

public class CoinFlipHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_history);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        populateListView();
    }

    private void populateListView() {

        String[] sample = {"hellp", "hoo", "yes"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.family_member_list_view,
                sample);

        ListView familyMembersList = findViewById(R.id.coinFlipHistoryListView);
        familyMembersList.setAdapter(adapter);
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
        return new Intent(context, CoinFlipHistory.class);
    }
}