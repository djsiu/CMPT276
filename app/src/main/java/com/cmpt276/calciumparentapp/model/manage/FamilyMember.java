package com.cmpt276.calciumparentapp.model.manage;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class FamilyMember {

    private String name;

    FamilyMember(String name) {
        this.name = name;
    }

    public String getMemberName() {
        return name;
    }

    public FamilyMember changeName(String name) {
        this.name = name;
        return this;
    }
}
