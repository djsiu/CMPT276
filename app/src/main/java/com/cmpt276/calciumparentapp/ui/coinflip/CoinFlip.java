package com.cmpt276.calciumparentapp.ui.coinflip;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.coinFlip.TurnPicker;
import com.cmpt276.calciumparentapp.model.manage.FamilyMemberSharedPreferences;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.cmpt276.calciumparentapp.ui.manage.ManageFamilyMembers;

public class CoinFlip extends AppCompatActivity {

    private enum Face{
        HEADS,
        TAILS
    }

    private Face currentFace;
    Bundle extras;

    private FamilyMembersManager familyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        familyManager = FamilyMembersManager.getInstance();
        FamilyMemberSharedPreferences.saveFamilyManagerToSharedPrefs(this);
        Log.i("CF", "buttonFunc: size is" + familyManager.getSize());
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
        Button button = findViewById(R.id.coin_button_heads);
        button.setOnClickListener(view -> flipCoin());

        button = findViewById(R.id.coin_button_tails);
        button.setOnClickListener(view -> flipCoin());


        //TODO: remove following section
        familyManager = FamilyMembersManager.getInstance();

        Log.i("CF", " size is "+ familyManager.getSize() );


        //set picker
        getPicker();
    }

    private void getPicker() {
        familyManager = FamilyMembersManager.getInstance();
        Log.i("CF", "getPicker: Size is "+familyManager.getSize());

        TextView textView = findViewById(R.id.coin_textView_message);
        if (extras != null){
            //get player Indexes
            int player1Index = (int) extras.get("player1");
            int player2Index = (int) extras.get("player2");

            //set name
            textView.setText(getString(R.string.coin_textView_picker, TurnPicker.choosePicker(this,player1Index, player2Index)));
        }else{
            textView.setText(getString(R.string.coin_textView_pickerGeneric));
        }
    }


    private void updateWinner(){
        TextView textView = findViewById(R.id.coin_textView_message);
        if (currentFace == Face.HEADS) {
            textView.setText(R.string.coin_message_headsWin);
        }else{
            textView.setText(R.string.coin_message_tailsWin);
        }
    }

    private void flipCoin(){

        //get random face
        if((((int)(Math.random()*10))%2) == 0) {//Heads represented by 0
            //perform animation
            currentFace = Face.HEADS;
            animateCoin();
        }else{
            //perform animation
            currentFace = Face.TAILS;
            animateCoin();
        }

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
