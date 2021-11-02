package com.cmpt276.calciumparentapp.ui.coinflip;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.ui.MainMenu;

public class CoinFlip extends AppCompatActivity {

    private enum Face{
        HEADS,
        TAILS
    }

    private Face currentFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        currentFace = Face.HEADS;

        //set buttons
        Button button = findViewById(R.id.coin_button_heads);
        button.setOnClickListener(view -> calculateWinner());

        button = findViewById(R.id.coin_button_tails);
        button.setOnClickListener(view -> calculateWinner());
    }





    private void calculateWinner(){
        if (flipCoin() == Face.HEADS) {
            TextView textView = findViewById(R.id.coin_textView_message);
            textView.setText(R.string.coin_message_headsWin);
        }else{
            TextView textView = findViewById(R.id.coin_textView_message);
            textView.setText(R.string.coin_message_tailsWin);

        }
    }

    private Face flipCoin(){

        //lock screen
        //get random face
        if((Math.random()*10)%2 == 0) {//Heads represented by 0
            //perform animation
            animateCoin();

            ImageView imageView = findViewById(R.id.imageView_coin);
            imageView.setImageResource(R.drawable.coin_heads);
            //set face
            currentFace = Face.HEADS;

        }else{
            //perform animation
            animateCoin();

            ImageView imageView = findViewById(R.id.imageView_coin);
            imageView.setImageResource(R.drawable.coin_tails);
            //set face
            currentFace = Face.TAILS;

        }

        //save roll data
        //free screen
        //return winner
        return currentFace;
    }

    private void animateCoin(){

    }

    /**
     * Displays actionbar buttons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_coin_flip, menu);
        return true;
    }

    /**
     * Adds logic to action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_history) {      // History Button
            openCoinFlipHistory();
            return true;
        } else if (item.getItemId() == android.R.id.home){  // Top left back arrow
            finish();
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    private void openCoinFlipHistory() {
        Intent i = CoinFlipHistory.makeIntent(CoinFlip.this);
        startActivity(i);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, CoinFlip.class);
    }
}