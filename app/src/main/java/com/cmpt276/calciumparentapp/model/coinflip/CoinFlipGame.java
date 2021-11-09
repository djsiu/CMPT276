package com.cmpt276.calciumparentapp.model.coinflip;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
Creating individual coin flip games.
 */

public class CoinFlipGame {

    // This doesn't have a context so you can't get a string resource easily
    // If anyone knows how to get one then change this
    private final String HEADS = "Heads";
    private final String TAILS = "Tails";

    private String date;
    private int pickerID;
    private int secondPlayerID;
    private CoinFlipResult coinFlipPick;
    private CoinFlipResult coinFlipResult;

    public enum CoinFlipResult {
        HEADS,
        TAILS
    }

    private CoinFlipGame(CoinFlipGameBuilder builder) {
        this.pickerID = builder.pickerID;
        this.secondPlayerID = builder.secondPlayerID;
        this.coinFlipPick = builder.coinFlipPick;
        this.coinFlipResult = builder.coinFlipResult;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss");
        date = LocalDateTime.now().format(formatter);
    }

    public int getPickerID() {
        return pickerID;
    }

    public int getSecondPlayerID() {
        return secondPlayerID;
    }
    
    public boolean isGameWonByPicker() {
        return coinFlipPick == coinFlipResult;
    }

    /**
     * Gets the text for the game. In the format:
     * picker name \nflip result \ndate
     * @return A string representing the game
     */
    public String getGameText() {
        FamilyMembersManager familyMembersManager = FamilyMembersManager.getInstance();
        String pickerName = familyMembersManager.getFamilyMemberNameFromID(pickerID);
        String flipResultStr;
        if(coinFlipResult == CoinFlipResult.HEADS){
            flipResultStr = HEADS;
        }
        else{
            flipResultStr = TAILS;
        }

        return pickerName + '\n' + flipResultStr + '\n' + date;
    }


    /**
     * The builder used to create a CoinFlipGame
     */
    public static class CoinFlipGameBuilder {
        private final int pickerID;
        private final int secondPlayerID;
        private CoinFlipResult coinFlipPick;
        private CoinFlipResult coinFlipResult;

        /**
         * Create a new builder instance
         * @param pickerID The ID of player who will be picking
         * @param secondPlayerID The ID of the other player
         */
        public CoinFlipGameBuilder(int pickerID, int secondPlayerID) {
            this.pickerID = pickerID;
            this.secondPlayerID = secondPlayerID;
        }

        public CoinFlipGameBuilder coinFlipResult(CoinFlipResult coinFlipResult) {
            this.coinFlipResult = coinFlipResult;
            return this;
        }

        public CoinFlipGameBuilder coinFlipPick(CoinFlipResult pick) {
            coinFlipPick = pick;
            return this;
        }

        public CoinFlipGame build() {
            CoinFlipGame game = new CoinFlipGame(this);
            validateCoinFlipGame(game);
            return game;
        }

        private void validateCoinFlipGame(CoinFlipGame game) {
            // TODO: implement validation
        }

        public int getPickerID() {
            return pickerID;
        }

        public int getSecondPlayerID() {
            return secondPlayerID;
        }

        public CoinFlipResult getCoinFlipPick() {
            return coinFlipPick;
        }

        public CoinFlipResult getCoinFlipResult() {
            return coinFlipResult;
        }
    }

}
