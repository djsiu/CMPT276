package com.cmpt276.calciumparentapp.model.coinflip;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.xml.namespace.QName;


/*
managing the history of coin flips: adding, retrieving and storing the game history in shared preferences
 */
public class CoinFlipHistoryManager {


    ArrayList<CoinFlipGame> coinFlipGames = new ArrayList<>();

    private static CoinFlipHistoryManager instance;

    private static final String SHARED_PREFS_KEY = "AppPrefs";
    private static final String SHARED_PREFS_FLIP_HISTORY_MANAGER_KEY = "FlipHistoryManager";

    public void addCoinFlip(String nameOfPicker, String flipResult, String pickerChoice) {
        coinFlipGames.add(new CoinFlipGame(nameOfPicker, flipResult, pickerChoice));
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

    //credit to eamonnmcmanus on github
    public static CoinFlipHistoryManager getFlipHistoryManagerFromSharedPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(SHARED_PREFS_FLIP_HISTORY_MANAGER_KEY, "");

        instance = gson.fromJson(json, CoinFlipHistoryManager.class);
        if(instance == null) {
            instance = new CoinFlipHistoryManager();
        }
        
        return instance;
    }
}
