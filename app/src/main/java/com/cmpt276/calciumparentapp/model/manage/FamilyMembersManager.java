package com.cmpt276.calciumparentapp.model.manage;

import java.util.ArrayList;

public class FamilyMembersManager {

    public ArrayList<FamilyMember> getFamilyMembersList() {
        return familyMembersList;
    }

    public void setFamilyMembersList(ArrayList<FamilyMember> familyMembersList) {
        this.familyMembersList = familyMembersList;
    }

    public int getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(int keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    private ArrayList<FamilyMember> familyMembersList;
    private int keyGenerator;

    FamilyMembersManager() {
        familyMembersList = new ArrayList<>();
        keyGenerator = 0;
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
        FamilyMember newMember = new FamilyMember(name, keyGenerator);
        familyMembersList.add(newMember);
        keyGenerator++;
    }

    public void editMember(String newName, String name) {
        for(int i = 0; i < familyMembersList.size(); i++) {
            if(name.equals(familyMembersList.get(i).getMemberName())) {
                familyMembersList.set(i, familyMembersList.get(i).changeName(newName));
            }
        }
    }

    public void deleteMember(String name) {
        for(int i = 0; i < familyMembersList.size(); i++) {
            if(name.equals(familyMembersList.get(i).getMemberName())) {
                familyMembersList.get(i).deleteChild();
            }
        }
    }

    public ArrayList<String> getFamilyMembersNames() {
        ArrayList<String> familyMembersStrings = new ArrayList<>();
        if (familyMembersList != null) {
            for (int i = 0; i < familyMembersList.size(); i++) {
                if(!familyMembersList.get(i).isDeleted())
                familyMembersStrings.add(familyMembersList.get(i).getMemberName());
            }
        }
        return familyMembersStrings;
    }

    public boolean isMemberNameUsed(String name) {
        boolean nameUsed = false;
        for(FamilyMember member : familyMembersList) {
            if(member.getMemberName().equals(name) && !member.isDeleted()){
                nameUsed = true;
            }
        }

        return nameUsed;
    }

    //retrieve the family members key by their index
    public int getMemberKey(int i) {
        return familyMembersList.get(i).getKey();
    }
}


