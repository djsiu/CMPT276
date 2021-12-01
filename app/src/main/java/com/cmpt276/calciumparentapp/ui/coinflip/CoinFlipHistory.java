package com.cmpt276.calciumparentapp.ui.coinflip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFlipGame;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFlipManager;

import java.util.List;

/*
    NEEDS TO DISPLAY:
    - date + time of flip
    - name of picker
    - what flip came up
    - won or lost icon
 */

public class CoinFlipHistory extends AppCompatActivity {

    private CoinFlipManager coinFlipManager;
    private CoinFlipHistoryRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_history);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        coinFlipManager = CoinFlipManager.getInstance(this);

        populateListView();
        setupRecyclerView();
    }

    private void populateListView() {
        ArrayAdapter<CoinFlipGame> adapter = new MyListAdapter();

        ListView familyMembersList = findViewById(R.id.coinFlipHistoryListView);
        familyMembersList.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.coinFlipHistoryRecyclerView);
        adapter = new CoinFlipHistoryRecyclerViewAdapter(this, coinFlipManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private class MyListAdapter extends ArrayAdapter<CoinFlipGame>{
        public MyListAdapter() {
            super(CoinFlipHistory.this, R.layout.list_item_coin_flip_history, coinFlipManager.getGamesList());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            List<CoinFlipGame> games = coinFlipManager.getGamesList();
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_item_coin_flip_history, parent, false);
            }

            // Find the car to work with.
            CoinFlipGame currentGame = games.get(position);

            // Fill the view
            ImageView imageView = itemView.findViewById(R.id.coin_flip_history_win_loss_icon);
            imageView.setImageResource(getGameIconID(currentGame));
            //player image
            ImageView pickerImageView = itemView.findViewById(R.id.coin_flip_history_child_image);

            if (currentGame.getPickerID() == -1) {
                pickerImageView.setVisibility(View.GONE);
            } else {
                //put image here
                Bitmap pickerImageId = currentGame.getPickerPhotoId(getApplicationContext());
                pickerImageView.setImageBitmap(pickerImageId);
            }

            // Game data text
            TextView makeText = itemView.findViewById(R.id.coin_flip_history_text);
            makeText.setText(currentGame.getGameText(getApplicationContext()));

            return itemView;
        }
    }

    /**
     * Get the ID for the appropriate game icon
     * @param game The game which the icon of will be determined
     * @return The ID of the appropriate game icon
     */
    private int getGameIconID(CoinFlipGame game) {
        if(game.isGameWonByPicker()){
            return R.drawable.win_flip_history;
        }

        return R.drawable.lose_flip_history;
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