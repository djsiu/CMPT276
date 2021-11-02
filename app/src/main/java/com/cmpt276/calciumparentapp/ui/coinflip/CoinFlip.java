package com.cmpt276.calciumparentapp.ui.coinflip;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    String TAG = "FlipCoin";

    private Face currentFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        currentFace = Face.TAILS;
        //set buttons
        Button button = findViewById(R.id.coin_button_heads);
        button.setOnClickListener(view -> flipCoin());

        button = findViewById(R.id.coin_button_tails);
        button.setOnClickListener(view -> flipCoin());
    }



    private void updateWinner(){
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

        Log.i(TAG, "flipCoin: This is a random number" + (((int)(Math.random()*10))%2));
        if((((int)(Math.random()*10))%2) == 0) {//Heads represented by 0
            //perform animation
            animateCoin(Face.HEADS);

            //set face
            currentFace = Face.HEADS;


        }else{
            //perform animation
            animateCoin(Face.TAILS);
            //set face
            currentFace = Face.TAILS;

        }

        //save roll data
        //free screen
        //return winner
        return currentFace;
    }



    private void animateCoin(Face resultFace){


        int numberOfRotations =  8;

        ImageView imageView = (ImageView) findViewById(R.id.imageView_coin);
        imageView.animate().setDuration(300*numberOfRotations).rotationYBy(numberOfRotations*180f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //render buttons unclickable while the
                Button buttonHeads = (Button) findViewById(R.id.coin_button_heads);
                buttonHeads.setClickable(false);
                Button buttonTails = (Button) findViewById(R.id.coin_button_tails);
                buttonTails.setClickable(false);

                imageView.setImageResource(R.drawable.coin_faceless);
                //start sound
            }

            @Override
            public void onAnimationEnd(Animator animation) {
             //play catch sound
                ImageView imageView = (ImageView) findViewById(R.id.imageView_coin);
                Log.i(TAG, "run: enter end action");
                Log.i(TAG, "run: is Heads");
                if(Face.HEADS == resultFace){
                    imageView.setImageResource(R.drawable.coin_heads);

                }else{
                    imageView.setImageResource(R.drawable.coin_tails);

                }

                Button buttonHeads = (Button) findViewById(R.id.coin_button_heads);
                buttonHeads.setClickable(true);
                Button buttonTails = (Button) findViewById(R.id.coin_button_tails);
                buttonTails.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

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