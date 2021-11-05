package com.cmpt276.calciumparentapp.model.coinflip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoinFlipGame {

    LocalDateTime timeStamp;
    String date, nameOfPicker, flipResult;
    boolean pickerChoice, pickerWon;


    public CoinFlipGame(String nameOfPicker, String flipResult) {
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

    private String getGameData() {
        return nameOfPicker + "\n" + flipResult + "\n" + timeStamp;
    }
}
