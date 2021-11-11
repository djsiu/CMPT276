package com.cmpt276.calciumparentapp.model.coinflip;

import android.content.Context;
import android.util.Log;

import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;

/*
returns the picker of two options and sets their changes in the stored preferences
 */
public class TurnPicker {

    public static String choosePicker(Context context, int player1, int player2){
        FamilyMembersManager familyManager = FamilyMembersManager.getInstance(context);
        Log.i("startupBug", "choosePicker: " + familyManager.getFamilyMemberNames());

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
        //return the name of the picker
        return pickerName;

    }

}
