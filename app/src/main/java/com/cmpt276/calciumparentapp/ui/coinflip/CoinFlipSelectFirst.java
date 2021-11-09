package com.cmpt276.calciumparentapp.ui.coinflip;


import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.coinFlip.nameListAdapter;

import com.cmpt276.calciumparentapp.model.manage.FamilyMemberSharedPreferences;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.google.gson.Gson;

import java.util.ArrayList;


public class CoinFlipSelectFirst extends Fragment {

    private ArrayList<String> nameList;
    private FamilyMembersManager familyManager;

    // Add RecyclerView member
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_coin_flip_select_first, container, false);

        FamilyMembersManager familyMembersManager = FamilyMembersManager.getInstance();
        nameList = familyManager.getFamilyMembersNames();
        recyclerView = view.findViewById(R.id.recyclerview_coin_select);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new nameListAdapter(nameList));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getFamilyManagerFromSharedPrefs();
        //nameList = new
    }

    private void getFamilyManagerFromSharedPrefs() {
        SharedPreferences prefs = this.getContext().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("FamilyManager", "");

        familyManager = gson.fromJson(json, FamilyMembersManager.class);
        if(familyManager == null) {
            familyManager = FamilyMembersManager.getInstance();
        }
    }

    private void populateNames(){
        FamilyMembersManager familyMembersManager = FamilyMembersManager.getInstance();
        for( String name : nameList){
        }
    }


}

