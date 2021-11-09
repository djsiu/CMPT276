package com.cmpt276.calciumparentapp.ui.coinflip;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFlipHistoryManager;
import com.cmpt276.calciumparentapp.model.coinflip.TurnPicker;
import com.cmpt276.calciumparentapp.model.manage.FamilyMemberSharedPreferences;
import com.google.gson.Gson;

public class CoinFlip extends AppCompatActivity {

    private CoinFlipHistoryManager flipHistoryManager;

    private enum Face{
        HEADS,
        TAILS
    }

    private Face currentFace;
    Bundle extras;
    private String buttonClicked = "";
    private String picker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FamilyMemberSharedPreferences.saveFamilyManagerToSharedPrefs(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        //get players from the selection activity
        extras = getIntent().getExtras();

        currentFace = Face.TAILS;
        //set buttons
        Button buttonHeads = findViewById(R.id.coin_button_heads);
        Button buttonTails = findViewById(R.id.coin_button_tails);
        Button buttonFlipAgain = findViewById(R.id.coin_button_flipAgain);


        buttonHeads.setOnClickListener(view -> {
            flipCoin();
            buttonHeads.setVisibility(View.GONE);

            buttonTails.setVisibility(View.GONE);

            buttonFlipAgain.setVisibility(View.VISIBLE);

            buttonClicked = "heads";
        });

        buttonTails.setOnClickListener(view -> {
            flipCoin();
            buttonHeads.setVisibility(View.GONE);

            buttonTails.setVisibility(View.GONE);

            buttonFlipAgain.setVisibility(View.VISIBLE);

            buttonClicked = "tails";
        });

        buttonFlipAgain.setOnClickListener(view -> {
            getPicker();
            buttonHeads.setVisibility(View.VISIBLE);

            buttonTails.setVisibility(View.VISIBLE);

            buttonFlipAgain.setVisibility(View.GONE);

        });

        flipHistoryManager = CoinFlipHistoryManager.getFlipHistoryManagerFromSharedPrefs(this);

        //set picker
        getPicker();
    }

    private void getPicker() {
        TextView textView = findViewById(R.id.coin_textView_message);
        if (extras != null){
            //get player Indexes
            int player1Index = (int) extras.get("player1");
            int player2Index = (int) extras.get("player2");

            picker = TurnPicker.choosePicker(this, player1Index, player2Index);
            //set name
            textView.setText(getString(R.string.coin_textView_picker, picker));
        }else{
            textView.setText(getString(R.string.coin_textView_pickerGeneric));
            picker = getString(R.string.no_picker);
        }
    }


    private void updateWinner(){
        TextView textView = findViewById(R.id.coin_textView_message);
        if (currentFace == Face.HEADS) {
            textView.setText(R.string.coin_message_headsWin);
            flipHistoryManager.addCoinFlip(picker, getString(R.string.heads), buttonClicked);
            System.out.println("adding a coin flip HEADSSSSSS");
        }else{
            textView.setText(R.string.coin_message_tailsWin);
            flipHistoryManager.addCoinFlip(picker, getString(R.string.tails), buttonClicked);
            System.out.println("adding a coin flip TAILSSSS");
        }
        saveFlipHistoryManagerToSharedPrefs();
    }

    private void flipCoin(){

        //get random face
        if((((int)(Math.random()*10))%2) == 0) {//Heads represented by 0
            //perform animation
            currentFace = Face.HEADS;
        }else{
            //perform animation
            currentFace = Face.TAILS;
        }
        animateCoin();

        //TODO:save roll data
        //return winner
    }

    private void animateCoin(){

        int numberOfRotations =  8;

        //play sound
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.coin_flip_sound);
        mediaPlayer.start();

        //show flip
        ImageView imageView = findViewById(R.id.imageView_coin);
        imageView.animate().setDuration(250*numberOfRotations).rotationYBy(numberOfRotations*180f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //render buttons unclickable while moving
                Button buttonHeads = findViewById(R.id.coin_button_heads);
                buttonHeads.setClickable(false);
                Button buttonTails = findViewById(R.id.coin_button_tails);
                buttonTails.setClickable(false);

                imageView.setImageResource(R.drawable.coin_faceless);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ImageView imageView = findViewById(R.id.imageView_coin);
                if(Face.HEADS == currentFace){
                    imageView.setImageResource(R.drawable.coin_heads);

                }else{
                    imageView.setImageResource(R.drawable.coin_tails);

                }
                updateWinner();
                Button buttonHeads = findViewById(R.id.coin_button_heads);
                buttonHeads.setClickable(true);
                Button buttonTails = findViewById(R.id.coin_button_tails);
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

    //credit to eamonnmcmanus on github
    private void saveFlipHistoryManagerToSharedPrefs() {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(flipHistoryManager);
        editor.putString("FlipHistoryManager", json);
        editor.apply();
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
