package com.cmpt276.calciumparentapp.ui.coinflip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFlipGame;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFlipHistoryManager;

import java.util.ArrayList;

/*
    NEEDS TO DISPLAY:
    - date + time of flip
    - name of picker
    - what flip came up
    - won or lost icon
 */

public class CoinFlipHistory extends AppCompatActivity {

    private CoinFlipHistoryManager flipHistoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_history);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        flipHistoryManager = CoinFlipHistoryManager.getFlipHistoryManagerFromSharedPrefs(this);

        populateListView();
    }

    private void populateListView() {

        ArrayList<CoinFlipGame> storedGames;

        ArrayAdapter<CoinFlipGame> adapter = new MyListAdapter();

        ListView familyMembersList = findViewById(R.id.coinFlipHistoryListView);
        familyMembersList.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<CoinFlipGame>{

        public MyListAdapter() {
            super(CoinFlipHistory.this, R.layout.history_item_view, flipHistoryManager.getCoinFlipGames());
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ArrayList<CoinFlipGame> games = flipHistoryManager.getCoinFlipGames();
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.history_item_view, parent, false);
            }
            // Find the car to work with.
            CoinFlipGame currentGame = games.get(position);

            // Fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.win_lose_history_icon);
            imageView.setImageResource(currentGame.getIconID());

            // Game data text
            TextView makeText = (TextView) itemView.findViewById(R.id.history_text);
            makeText.setText(currentGame.getGameData());

            return itemView;
        }
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