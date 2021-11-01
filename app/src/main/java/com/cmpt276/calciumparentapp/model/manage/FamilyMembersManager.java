package com.cmpt276.calciumparentapp.model.manage;

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
        familyMembersList.add(new FamilyMember(name)
        );
    }

    public void editMember(String name, int i) {
        familyMembersList.set(i, familyMembersList.get(i).changeName(name));
    }

    public void deleteMember(int i) {
        familyMembersList.remove(i);
    }

    public ArrayList<FamilyMember> getFamilyMembersList() {
        return familyMembersList;
    }

    //used for the list view ui
    public String[] getFamilyMembersNames() {
        String[] fm = {"hi"};
        System.out.println("size of fam mems: " + familyMembersList.size());
        if(familyMembersList != null) {
            fm = new String[familyMembersList.size()];

            for (int i = 0; i < familyMembersList.size(); i++) {
                fm[i] = familyMembersList.get(i).getMemberName();
            }
        }
        return fm;
    }
}

