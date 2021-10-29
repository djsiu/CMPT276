package com.cmpt276.calciumparentapp.ui.coinflip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cmpt276.calciumparentapp.R;

public class CoinFlip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, CoinFlip.class);
    }

}