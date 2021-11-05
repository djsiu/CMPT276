package com.cmpt276.calciumparentapp.ui.coinflip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.coinFlip.nameListAdapter;

import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;

import org.w3c.dom.NameList;

import java.util.ArrayList;
import java.util.List;


public class CoinFlipSelectFirst extends Fragment {

    private String[] nameList;

    // Add RecyclerView member
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_coin_flip_select_first, container, false);

        FamilyMembersManager familyMembersManager = FamilyMembersManager.getInstance();
        nameList = familyMembersManager.getFamilyMembersNames();
        recyclerView = view.findViewById(R.id.recyclerview_coin_select);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new nameListAdapter(nameList));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //nameList = new




    }


    private void populateNames(){
        FamilyMembersManager familyMembersManager = FamilyMembersManager.getInstance();
        for( String name : nameList){
        }
    }


}

