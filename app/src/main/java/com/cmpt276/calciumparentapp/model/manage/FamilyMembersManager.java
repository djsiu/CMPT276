package com.cmpt276.calciumparentapp.model.manage;


import java.util.ArrayList;

/*
Managing the members of the family.
adding, deleting and retreiving info about all the family members
 */

public class FamilyMembersManager {

    private ArrayList<FamilyMember> familyMembersList;
    public ArrayList<FamilyMember> getFamilyMembersList() {
        return familyMembersList;
    }
    private int keyGenerator;

    private FamilyMembersManager() {
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

    public void setFamilyMembersList(ArrayList<FamilyMember> familyMembersList) {
        this.familyMembersList = familyMembersList;
    }

    public int getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(int keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public void addMember(String name) {
        FamilyMember newMember = new FamilyMember(name, keyGenerator, familyMembersList.size());
        familyMembersList.add(newMember);
        keyGenerator++;
    }


    public void changeMemberName(String newName, String name) {
        for(int i = 0; i < familyMembersList.size(); i++) {
            if(name.equals(familyMembersList.get(i).getMemberName())) {
                familyMembersList.set(i, familyMembersList.get(i).changeName(newName));
            }
        }
    }

    public void deleteMember(String name) {
        for(int i = 0; i < familyMembersList.size(); i++) {
            if(name.equals(familyMembersList.get(i).getMemberName())) {
                choosePicker(i);
                familyMembersList.get(i).deleteChild();
            }
        }
    }

    public ArrayList<String> getFamilyMembersNames() {
        ArrayList<String> familyMembersStrings = new ArrayList<>();
        if (familyMembersList != null) {
            for (int i = 0; i < familyMembersList.size(); i++) {
                if(!familyMembersList.get(i).isDeleted()) {
                    familyMembersStrings.add(familyMembersList.get(i).getMemberName());
                }
            }
        }
        return familyMembersStrings;
    }
    public ArrayList<Integer> getFamilyMemberKeys() {
        ArrayList<Integer> familyMembersStrings = new ArrayList<>();
        if (familyMembersList != null) {
            for (int i = 0; i < familyMembersList.size(); i++) {
                if(!familyMembersList.get(i).isDeleted())
                familyMembersStrings.add(familyMembersList.get(i).getKey());
            }
        }
        return familyMembersStrings;
    }

    public int getCoinFlipPriority(int index){
        return familyMembersList.get(index).getCoinFlipPickPriority();
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

    public String choosePicker(int index){
        int playerPriority = familyMembersList.get(index).getCoinFlipPickPriority();
        int listSize = familyMembersList.size();
        for (int currentIndex = 0; currentIndex < listSize; currentIndex++ ){
            if(familyMembersList.get(currentIndex).getCoinFlipPickPriority() >playerPriority){
                familyMembersList.get(currentIndex).setCoinFlipPickPriority(familyMembersList.get(currentIndex).getCoinFlipPickPriority() -1);
            }
        }
        familyMembersList.get(index).setCoinFlipPickPriority(listSize-1);
        return familyMembersList.get(index).getMemberName();
    }

    public String getFamilyMemberNameFromID(int ID) {
        for(FamilyMember familyMember : familyMembersList) {
            if(familyMember.getKey() == ID){
                return familyMember.getMemberName();
            }
        }

        throw new IllegalArgumentException("No family member with provided ID");
    }

}