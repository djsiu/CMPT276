package com.cmpt276.calciumparentapp.model.manage;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class FamilyMembersManager {

    ArrayList<FamilyMember> familyMembersList;

    FamilyMembersManager() {
        familyMembersList = new ArrayList<>();
    }

    //singleton support
    private static FamilyMembersManager instance;

    public static FamilyMembersManager getInstance() {
        if(instance == null) {
            instance = new FamilyMembersManager();
        }
        return instance;
    }

    public void addMember(String name) {
        FamilyMember newMember = new FamilyMember(name);
        familyMembersList.add(newMember);
    }

    //only used for adding the family member in shared preferences
    public String getAddedMemberKey() {
        return familyMembersList.get(getFamilyMembersNames().length - 1).getMemberKey();
    }

    public String getMemberKeyByIndex(int i) {
        return familyMembersList.get(i).getMemberKey();
    }

    public void editMember(String name, int i) {
        familyMembersList.set(i, familyMembersList.get(i).changeName(name));
    }

    public void deleteMember(int i) {
        familyMembersList.remove(i);
    }

    public String[] getFamilyMembersNames() {
        String[] fm = {};
        if (familyMembersList != null) {
            fm = new String[familyMembersList.size()];

            for (int i = 0; i < familyMembersList.size(); i++) {
                fm[i] = familyMembersList.get(i).getMemberName();
            }
        }
        return fm;
    }
}

