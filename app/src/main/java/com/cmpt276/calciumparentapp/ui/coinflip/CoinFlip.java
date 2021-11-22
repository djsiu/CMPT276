package com.cmpt276.calciumparentapp.ui.coinflip;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFace;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFlipManager;
import com.cmpt276.calciumparentapp.model.manage.FamilyMember;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;

/*
ui for the coin flip activity
 */
public class CoinFlip extends AppCompatActivity {

    // Indicates if the game was started by selecting two children or if it was done
    // automatically since there were not enough children registered in the app
    boolean gameWithNamedPlayers;
    private FamilyMembersManager familyMembersManager;
    private CoinFlipManager coinFlipManager;
    private Button buttonHeads;
    private Button buttonTails;
    private Button buttonFlipAgain;
    private ImageButton buttonSwap;
    private CoinFace coinFlipResult;
    private TextView nameTextView;
    private ImageView coinImageView;
    private MenuItem historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        coinFlipManager = CoinFlipManager.getInstance(this);
        familyMembersManager = FamilyMembersManager.getInstance(this);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        setupGame();

    }

    @Override
    protected void onDestroy() {
        coinFlipManager.cancelGame();
        super.onDestroy();
    }

    private void setupGame() {
        nameTextView = findViewById(R.id.coin_textView_name);
        coinImageView = findViewById(R.id.imageView_coin);
        //sets game with named players if there is a game running with named players and false otherwise
        if(coinFlipManager.isGameRunning()){
            gameWithNamedPlayers = coinFlipManager.getPickerID() != -1;
        }

        setupGameButtons();
        setPickerInfo();
    }

    /**
     * Sets up the button variables and the on click listeners for the game buttons
     */
    private void setupGameButtons() {

        buttonHeads = findViewById(R.id.coin_button_heads);
        buttonTails = findViewById(R.id.coin_button_tails);
        buttonFlipAgain = findViewById(R.id.coin_button_flipAgain);
        buttonSwap = findViewById(R.id.coin_button_swapPicker);



        // the this:: code just calls that method and passes the view.
        buttonHeads.setOnClickListener(this::onCoinFlipButtonClick);
        buttonTails.setOnClickListener(this::onCoinFlipButtonClick);
        buttonFlipAgain.setOnClickListener(v -> onFlipAgainButtonClick());
        buttonSwap.setOnClickListener(v -> onSwapButtonClick());

    }

    /**
     * Called when one of the coin flip buttons is clicked
     * Registers the user's choice in the game manager,
     * calls the animation function, and registers the result of the coin flip.
     * @param clickedBtnView The view for the button that is clicked
     */
    private void onCoinFlipButtonClick(View clickedBtnView) {
        Button clickedBtn = (Button) clickedBtnView;

        // Hide the coin flip buttons and make play again visible
        buttonHeads.setVisibility(View.GONE);
        buttonTails.setVisibility(View.GONE);
        buttonFlipAgain.setVisibility(View.VISIBLE);
        buttonSwap.setVisibility(View.GONE);

        // flip the coin and start the animation
        coinFlipResult = coinFlipManager.flipCoin();
        animateCoin();

        CoinFace flipPick;

        if(clickedBtn.equals(buttonHeads)) {
            flipPick = CoinFace.HEADS;
        }
        else {
            flipPick = CoinFace.TAILS;
        }
        coinFlipManager.assignCoinPick(flipPick);

    }

    private void onFlipAgainButtonClick() {

        coinFlipManager.beginAnotherGame();
        buttonSwap.setVisibility(View.VISIBLE);
        setPickerInfo();
        buttonHeads.setVisibility(View.VISIBLE);
        buttonTails.setVisibility(View.VISIBLE);
        buttonFlipAgain.setVisibility(View.GONE);

    }

    private void onSwapButtonClick() {
        coinFlipManager.swapFlipper();
        setPickerInfo();
    }


    /**
     * Sets the messageTextView's and the picker image according to the picker or to a generic message if
     * there are no named players
     */
    private void setPickerInfo() {

        ImageView pickerImageView = findViewById(R.id.coin_imageView_picker);
        if(gameWithNamedPlayers){
            String pickerName = familyMembersManager.getFamilyMemberNameFromID(coinFlipManager.getPickerID());
            nameTextView.setText(pickerName);

            //set the image here
            FamilyMember picker = familyMembersManager.getFamilyMemberFromID(coinFlipManager.getPickerID());
            pickerImageView.setImageBitmap(picker.getProfileBitmap());
        }

        //if memberless game
        else {
            buttonSwap.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            nameTextView.setText(getString(R.string.coin_textView_pickerGeneric));
            pickerImageView.setVisibility(View.GONE);
        }
    }

    private void updateWinner() {

        if(coinFlipResult == CoinFace.HEADS) {
            nameTextView.setText(R.string.coin_message_headsWin);
        }
        else{
            nameTextView.setText(R.string.coin_message_tailsWin);
        }
    }

    /**
     * Sets whether all the buttons are enabled
     * Used to control input during the coin flip animation
     * @param enabled Indicates if the buttons should be enabled
     */
    private void setButtonsEnabled(boolean enabled) {
        buttonHeads.setEnabled(enabled);
        buttonTails.setEnabled(enabled);
        buttonFlipAgain.setEnabled(enabled);
        historyButton.setEnabled(enabled);
    }

    /**
     * Plays the animation and sound for the coin flip and
     * registers the callbacks for when its completed.
     */
    private void animateCoin(){

        int numberOfRotations =  8;

        //play sound
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.coin_flip_sound);
        mediaPlayer.start();

        //show flip
        ImageView imageView = findViewById(R.id.imageView_coin);
        imageView.animate()
                .setDuration(250*numberOfRotations)
                .rotationYBy(numberOfRotations*180f)
                .setListener(new CoinFlipAnimationListener())
                .start();

    }

    /**
     * The listener used by the coin flip animation
     * Handles setting the image of the coin when the animation is over
     * and preventing the buttons from being clicked while the animation is running
     */
    private class CoinFlipAnimationListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            // prevent buttons from being clicked while the animation is running
            setButtonsEnabled(false);
            coinImageView.setImageResource(R.drawable.coin_faceless);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if(coinFlipResult == CoinFace.HEADS) {
                coinImageView.setImageResource(R.drawable.coin_heads);
            }
            else{
                coinImageView.setImageResource(R.drawable.coin_tails);
            }

            // Make the buttons clickable again
            setButtonsEnabled(true);

            updateWinner();
        }

        @Override
        public void onAnimationCancel(Animator animation) { }

        @Override
        public void onAnimationRepeat(Animator animation) { }
    }



    /**
     * Displays actionbar buttons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_coin_flip, menu);
        historyButton = menu.findItem(R.id.action_history);
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
        }
        else if (item.getItemId() == android.R.id.home){  // Top left back arrow
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
