package com.cmpt276.calciumparentapp.model.coinflip;

import java.util.ArrayList;

import javax.xml.namespace.QName;

public class CoinFlipHistoryManager {

    ArrayList<CoinFlipGame> coinFlipGames = new ArrayList<>();

    public void addCoinFlip(String nameOfPicker, String flipResult) {
        coinFlipGames.add(new CoinFlipGame(nameOfPicker, flipResult));
    }
}
