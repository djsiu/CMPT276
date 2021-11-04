package com.cmpt276.calciumparentapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;

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





}