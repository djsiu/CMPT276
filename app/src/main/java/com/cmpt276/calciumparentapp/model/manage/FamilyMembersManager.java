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
        System.out.println("inputed old name: "+ name);
        for(int i = 0; i < familyMembersList.size(); i++) {
            System.out.println("comparing with all names: " + familyMembersList.get(i).getMemberName());
            if(name.equals(familyMembersList.get(i).getMemberName())) {
//                boolean sameName = (name == familyMembersList.get(i).getMemberName());
//                System.out.println("names are the same" + (String) sameName);
                familyMembersList.set(i, familyMembersList.get(i).changeName(newName));
                System.out.println("new name: " + newName);
            }
        }
    }

    public void deleteMember(String name) {
        for(int i = 0; i < familyMembersList.size(); i++) {
            if(name.equals(familyMembersList.get(i).getMemberName())) {
                familyMembersList.get(i).deleteChild();
            }
        }
        //familyMembersList.remove(i);
    }

    public ArrayList<String> getFamilyMembersNames() {
        ArrayList<String> familyMembersStrings = new ArrayList<>();
        if (familyMembersList != null) {
            for (int i = 0; i < familyMembersList.size(); i++) {
                if(!familyMembersList.get(i).getDeleted())
                familyMembersStrings.add(familyMembersList.get(i).getMemberName());
            }
        }
        return familyMembersStrings;
    }

    //retrieve the family members key by their index
    public int getMemberKey(int i) {
        return familyMembersList.get(i).getKey();
    }
}

//TODO: check for duplicate names
