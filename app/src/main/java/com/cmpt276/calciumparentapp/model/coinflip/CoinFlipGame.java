package com.cmpt276.calciumparentapp.model.coinflip;

import android.content.Context;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

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

    private final String date;
    private final int pickerID;
    private final int secondPlayerID;
    private final CoinFace coinFlipPick;
    private final CoinFace coinFlipResult;


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
     *
     * @param context A context needed to get a FamilyMembersManager for the name
     * @return A string representing the game
     */
    // TODO: History game text generated here
    public SpannableString getGameText(Context context) {
        FamilyMembersManager familyMembersManager = FamilyMembersManager.getInstance(context);
        String pickerName = familyMembersManager.getFamilyMemberNameFromID(pickerID);
        String flipResultStr;
        if (coinFlipResult == CoinFace.HEADS) {
            flipResultStr = HEADS;
        } else {
            flipResultStr = TAILS;
        }

        String historyString = pickerName + "\n\n\n" + flipResultStr + '\n' + date;
        SpannableString formattedHistoryString = new SpannableString(historyString);

        // Makes name bold
        formattedHistoryString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0,
                pickerName.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return formattedHistoryString;
    }

    /**
     * The builder used to create a CoinFlipGame
     */
    public static class CoinFlipGameBuilder {
        private final int pickerID;
        private final int secondPlayerID;
        private CoinFace coinFlipPick;
        private CoinFace coinFlipResult;

        /**
         * Create a new builder instance
         *
         * @param pickerID       The ID of player who will be picking
         * @param secondPlayerID The ID of the other player
         */
        public CoinFlipGameBuilder(int pickerID, int secondPlayerID) {
            this.pickerID = pickerID;
            this.secondPlayerID = secondPlayerID;
        }

        public CoinFlipGameBuilder coinFlipResult(CoinFace coinFlipResult) {
            this.coinFlipResult = coinFlipResult;
            return this;
        }

        public CoinFlipGameBuilder coinFlipPick(CoinFace pick) {
            coinFlipPick = pick;
            return this;
        }

        public CoinFlipGame build() {
            return new CoinFlipGame(this);
        }


        public int getPickerID() {
            return pickerID;
        }

        public int getSecondPlayerID() {
            return secondPlayerID;
        }

        public CoinFace getCoinFlipPick() {
            return coinFlipPick;
        }

        public CoinFace getCoinFlipResult() {
            return coinFlipResult;
        }
    }

}
