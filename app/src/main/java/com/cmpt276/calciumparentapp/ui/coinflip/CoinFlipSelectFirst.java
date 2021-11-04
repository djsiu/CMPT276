package com.cmpt276.calciumparentapp.ui.coinflip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;


public class CoinFlipSelectFirst extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void populateNames(){
        FamilyMembersManager familyMembersManager = FamilyMembersManager.getInstance();
        for( String name : familyMembersManager.getFamilyMembersNames()){
            addMember(name);
        }
    }

    private void addMember(String name) {


    }
}

