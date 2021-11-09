package com.cmpt276.calciumparentapp.model.coinflip;

import com.cmpt276.calciumparentapp.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoinFlipGame {

    LocalDateTime timeStamp;
    String date, nameOfPicker, flipResult;
    String pickerChoice;


    public CoinFlipGame(String nameOfPicker, String flipResult, String pickerChoice) {
        this.date = createDate();
        this.flipResult = flipResult;
        this.nameOfPicker = nameOfPicker;
        this.pickerChoice = pickerChoice;
    }

    private String createDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss");
        timeStamp = LocalDateTime.now();
        return timeStamp.format(formatter);
    }

    public String getGameData() {
        return nameOfPicker + "\n" + flipResult + "\n" + date;
    }


    public int getIconID() {
        int id;
        if(pickerChoice.equals(flipResult)) {
            id = R.drawable.win_flip_history;
        } else {
            id = R.drawable.lose_flip_history;
        }
        return id;
    }
}
