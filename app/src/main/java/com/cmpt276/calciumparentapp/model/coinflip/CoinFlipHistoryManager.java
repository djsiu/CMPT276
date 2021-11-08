package com.cmpt276.calciumparentapp.model.coinflip;

import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;

import java.util.ArrayList;

import javax.xml.namespace.QName;

public class CoinFlipHistoryManager {

    ArrayList<CoinFlipGame> coinFlipGames = new ArrayList<>();

    //singleton support
    private static CoinFlipHistoryManager instance;

    public static CoinFlipHistoryManager getInstance() {
        if(instance == null) {
            instance = new CoinFlipHistoryManager();
        }
        return instance;
    }

    public void addCoinFlip(String nameOfPicker, String flipResult, boolean pickerWon) {

        coinFlipGames.add(new CoinFlipGame(nameOfPicker, flipResult, pickerWon));
    }

    public ArrayList<String> getCoinFlipGameStrings() {
        ArrayList<String> gameStrings = new ArrayList<>();
        if(coinFlipGames != null) {
            for(int i = 0; i < coinFlipGames.size(); i++) {
                gameStrings.add(coinFlipGames.get(i).getGameData());
            }
        }
        return gameStrings;
    }

    public ArrayList<CoinFlipGame> getCoinFlipGames() {
        return coinFlipGames;
    }
}
