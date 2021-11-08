package com.cmpt276.calciumparentapp.model.coinFlip;

import android.content.Context;
import android.util.Log;

import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.cmpt276.calciumparentapp.ui.manage.ManageFamilyMembers;

public class TurnPicker {

    private static String TAG = "TurnPicker";

    public static String choosePicker(Context context, int player1, int player2){
        Log.i(TAG, "choosePicker: entered turn picker");
        FamilyMembersManager familyManager = FamilyMembersManager.getInstance();
        //find turn order of each member
        Log.i(TAG, "choosePicker: player 1: " + player1 + " and player 2: " + player2 + " size is "+ familyManager.getSize() );
        int player1Priority = familyManager.getCoinFlipPriority(player1);
        int player2Priority = familyManager.getCoinFlipPriority(player2);

        //compare turn order of each child
        String pickerName = null;
        if (player1Priority < player2Priority){
           pickerName = familyManager.choosePicker(player1);
        }
        Log.i(TAG, "choosePicker: saving to prefs");
        ManageFamilyMembers.saveFamilyManagerToSharedPrefs(context);
        Log.i(TAG, "choosePicker: saved to prefs\n\n\n\n\n\n\n\n\n\n");
        //return the name of the picker
        return pickerName;

    }

}
