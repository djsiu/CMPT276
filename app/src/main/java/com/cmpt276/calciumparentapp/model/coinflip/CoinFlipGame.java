package com.cmpt276.calciumparentapp.model.coinflip;

import com.cmpt276.calciumparentapp.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoinFlipGame {

    LocalDateTime timeStamp;
    String date, nameOfPicker, flipResult;
    boolean pickerWon;


    public CoinFlipGame(String nameOfPicker, String flipResult, boolean pickerWon) {
        this.date = createDate();
        this.flipResult = flipResult;
        this.nameOfPicker = nameOfPicker;
    }

    private String createDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        timeStamp = LocalDateTime.now();
        String nowFormatted = timeStamp.format(formatter);

        return nowFormatted;
    }

    public String getGameData() {
        return nameOfPicker + "\n" + flipResult + "\n" + timeStamp;
    }


    public int getIconID() {
        int id;
        if(pickerWon) {
            id = R.drawable.win_flip_history;
        } else {
            id = R.drawable.lose_flip_history;
        }
        return id;
    }
}
