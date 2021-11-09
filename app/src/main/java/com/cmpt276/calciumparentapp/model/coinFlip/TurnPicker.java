package com.cmpt276.calciumparentapp.model.coinFlip;

import android.content.Context;

import com.cmpt276.calciumparentapp.model.manage.FamilyMemberSharedPreferences;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;

public class TurnPicker {

    public static String choosePicker(Context context, int player1, int player2){
        FamilyMembersManager familyManager = FamilyMembersManager.getInstance();
        FamilyMemberSharedPreferences.getFamilyManagerFromSharedPrefs(context);

        //find turn order of each member
        int player1Priority = familyManager.getCoinFlipPriority(player1);
        int player2Priority = familyManager.getCoinFlipPriority(player2);

        //compare turn order of each child
        String pickerName;
        if (player1Priority < player2Priority){
            pickerName = familyManager.choosePicker(player1);
        }else{
            pickerName = familyManager.choosePicker(player2);
        }
        FamilyMemberSharedPreferences.saveFamilyManagerToSharedPrefs(context);
        //return the name of the picker
        return pickerName;

    }

}
