package com.cmpt276.calciumparentapp.ui.manage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cmpt276.calciumparentapp.R;

public class ManageFamilyMembers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_members);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, ManageFamilyMembers.class);
    }

}