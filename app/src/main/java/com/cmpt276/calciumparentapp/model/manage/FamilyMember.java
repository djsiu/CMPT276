package com.cmpt276.calciumparentapp.model.manage;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class FamilyMember {

    private String name;
    private String NAME_KEY;

    FamilyMember(String name) {
        this.name = name;
        this.NAME_KEY = name; //name key is the child's name from the first time it was created.
    }

    public String getMemberName() {
        return name;
    }

    public String getMemberKey() {
        return NAME_KEY;
    }

    public FamilyMember changeName(String name) {
        this.name = name;
        return this;
    }
}
