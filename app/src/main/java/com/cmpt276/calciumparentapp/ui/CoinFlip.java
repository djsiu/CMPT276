package com.cmpt276.calciumparentapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;

public class CoinFlip extends AppCompatActivity {

    String TAG = "FlipCoin";


    //private Face currentFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Log.i(TAG, "onCreate: Entered create\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n/////////////////////////////////////////////////////////////////////////////////////////////");
/*
        currentFace = Face.HEADS;

        //set buttons
        Button button = findViewById(R.id.coin_button_heads);
        button.setOnClickListener(view -> calculateWinner());

        button = findViewById(R.id.coin_button_tails);
        button.setOnClickListener(view -> calculateWinner());
*/


    }
/*
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
        Log.i(TAG, "flipCoin: This is a random number" + (((int)(Math.random()*10))%2));
        if(( ((int)(Math.random()*10))%2) == 0) {//H                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            eads represented by 0
            //perform animation
            //animateCoin(Face.HEADS);

            ImageView imageView = findViewById(R.id.imageView_coin);
            imageView.setImageResource(R.drawable.coin_heads);
            //set face
            currentFace = Face.HEADS;

        }else{
            //perform animation
            //animateCoin(Face.TAILS);

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

    private void animateCoin(Face resultFace){

        int numberOfRotations =  2 * (((int)(Math.random()*10))%3 + 1) ; //will flip 2/7 times

        if (resultFace == currentFace){
            numberOfRotations += 1;
        }


        ImageView imageView = (ImageView) findViewById(R.id.imageView_coin);
        for (int rotations = 0; rotations < numberOfRotations; rotations++){
            imageView.animate().setDuration(50).rotationYBy(90f).withEndAction(new Runnable() {
                @Override
                public void run() {
                    if (currentFace == Face.HEADS){
                        imageView.setImageResource(R.drawable.coin_tails);
                        currentFace = Face.TAILS;
                    }else{
                        imageView.setImageResource(R.drawable.coin_heads);
                        currentFace = Face.HEADS;
                    }
                }
            }).start();
            imageView.animate().setDuration(50).rotationYBy(90f).start();
        }
    }



*/

}