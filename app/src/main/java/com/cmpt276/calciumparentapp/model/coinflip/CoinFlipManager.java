package com.cmpt276.calciumparentapp.model.coinflip;

import android.content.Context;
import android.content.SharedPreferences;

import com.cmpt276.calciumparentapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The class responsible for handling all aspects of the coin flip game
 * including the history.
 * The class is singleton
 *
 *
 */
public class CoinFlipManager {

    public static final String COIN_FLIP_MANAGER_KEY = "CoinFlipManagerKey";
    private ArrayList<CoinFlipGame> games;
    private transient boolean gameRunning = false;
    private transient CoinFlipGame.CoinFlipGameBuilder gameBuilder;

    // Singleton support
    private static CoinFlipManager instance;
    // This causes a memory leak. If anyone knows a good way to fix this then let me know
    private transient Context context;
    private CoinFlipManager() {
        games = new ArrayList<>();
    }
    
    public static CoinFlipManager getInstance(Context context) {
        if(instance == null) {
            generateInstance(context);
        }
        return instance;
    }

    /**
     * Get an unmodifiable copy of all the games
     * @return An unmodifiable list of all the games
     */
    public List<CoinFlipGame> getGamesList() {
        return Collections.unmodifiableList(games);
    }

    public boolean isGameRunning() {
        return gameRunning;
    }


    /**
     * Starts a game with the two given players
     * @param playerID1 The id of the one of the players
     * @param playerID2 The id of the other player
     */
    public void beginGame(int playerID1, int playerID2) {
        if(gameRunning){
            throw new IllegalStateException("Attempting to start a game while another game is running");
        }

        gameRunning = true;
        assignPickersToBuilder(playerID1, playerID2);

    }

    /**
     * Starts a new game with the same players as the previous game.
     * Must have had at least 1 game played while app was running.
     */
    public void beginAnotherGame() {
        // The players of the last game can be taken from the game builder
        if(gameBuilder == null) {
            throw new IllegalStateException("beginAnotherGame called without previous game");
        }

        int playerID1 = gameBuilder.getPickerID();
        int playerID2 = gameBuilder.getSecondPlayerID();

        beginGame(playerID1, playerID2);
    }

    /**
     * Assigns the provided pick to the currently running game.
     * There must be a current game running which was created by CoinFlipManager.beginGame
     * @param coinFlipPick The pick to be assigned to the currently running game
     */
    public void assignCoinPick(CoinFlipGame.CoinFlipResult coinFlipPick) {
        if(!gameRunning) {
            throw new IllegalStateException("Attempting to assign a coin pick without a running game");
                    
        }
        gameBuilder.coinFlipPick(coinFlipPick);

        // If the coin flip result has been set then all fields have been set and the game can is completed
        if(gameBuilder.getCoinFlipResult() != null){
            completeGame();
        }
    }

    /**
     * Assigns the result of a coin flip to the currently running game.
     * There must be a current game running which was created by CoinFlipManager.beginGame
     * @param coinFlipResult The result of the game's coin flip
     */
    public void assignCoinFlipResult(CoinFlipGame.CoinFlipResult coinFlipResult) {
        if(!gameRunning) {
            throw new IllegalStateException("Attempting to assign a coin flip result without a running game");
        }
        
        gameBuilder.coinFlipResult(coinFlipResult);

        // If the coin flip pick has been set then all fields have been set and the game is completed
        if(gameBuilder.getCoinFlipPick() != null){
            completeGame();
        }
    }

    /**
     * Gets the picker ID.
     * There must be a current game running which was created by CoinFlipManager.beginGame
     * @return The picker's ID
     */
    public int getPickerID() {
        if(!gameRunning) {
            throw new IllegalStateException("Attempting to get picker without a running game");
        }

        return gameBuilder.getPickerID();
    }

    /**
     * Completes the currently running game.
     * Requires there is a currently running game 
     * and that the coin pick and coin flip result are set
     */
    public void completeGame() {
        if(!gameRunning) {
            throw new IllegalStateException("Attempting to complete game without a running game");
        }
        // The checks for assigning the coin pick and flip result are handled in the builder
        games.add(gameBuilder.build());
        gameRunning = false;

        saveToSharedPrefs();
    }

    /**
     * Takes the two provided player IDs and sets assigns the gameBuilder to a new instance
     * with the players set correctly.
     * @param playerID1 The id of the one of the players
     * @param playerID2 The id of the other player
     */
    private void assignPickersToBuilder(int playerID1, int playerID2) {
        // IDs initialized to -1 since compiler complains that variables may be uninitialized otherwise
        int pickerID = -1; 
        int otherPlayerId = -1;
        boolean pickerFound = false;

        // Loop through in reverse (starts with most recent game and moves to oldest)
        // assign second pick to the player who most recently went first
        for(int i = games.size() - 1; i >= 0 && !pickerFound; i--) {
            if(games.get(i).getPickerID() == playerID1){
                pickerID = playerID2;
                otherPlayerId = playerID1;
                pickerFound = true;
            }
            else if(games.get(i).getPickerID() == playerID2) {
                pickerID = playerID1;
                otherPlayerId = playerID2;
                pickerFound = true;
            }
        }
        
        // If the picker isn't found, randomly assign one
        if(!pickerFound) {
            Random random = new Random();
            if(random.nextBoolean()){
                pickerID = playerID1;
                otherPlayerId = playerID2;
            }
            else {
                pickerID = playerID2;
                otherPlayerId = playerID1;
            }
        }
        
        gameBuilder = new CoinFlipGame.CoinFlipGameBuilder(pickerID, otherPlayerId);
    }

    /**
     * Generates and assigns the static instance member of the class.
     * Loads instance from shared preferences if one is saved
     * @param context A context used for shared preferences.
     */
    private static void generateInstance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_shared_preferences_key),
                Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prefs.getString(COIN_FLIP_MANAGER_KEY, "");
        if(!json.equals("")){
            instance = gson.fromJson(json, CoinFlipManager.class);
        }
        else{
            instance = new CoinFlipManager();
        }

        instance.context = context;

    }

    /**
     * Saves the current CoinFlipManager to shared preferences
     * This also saves the list of games since it is part of the manager
     */
    private void saveToSharedPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.app_shared_preferences_key),
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this);
        editor.putString(COIN_FLIP_MANAGER_KEY, json);
        editor.apply();
    }
    
    

}
