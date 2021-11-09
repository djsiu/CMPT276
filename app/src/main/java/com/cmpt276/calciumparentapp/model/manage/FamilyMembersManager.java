package com.cmpt276.calciumparentapp.model.manage;

import android.util.Log;

import java.util.ArrayList;

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
        Log.i("famlistsize", "addMember: " + familyMembersList.size());
        FamilyMember newMember = new FamilyMember(name, keyGenerator, familyMembersList.size());
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

    //TODO: Remove the get size function and get prios
    public ArrayList<Integer> getPrios() {
        ArrayList<Integer> prios = new ArrayList<>();
        if (familyMembersList != null) {
            for (int i = 0; i < familyMembersList.size(); i++) {
                if(!familyMembersList.get(i).getDeleted())
                    prios.add(familyMembersList.get(i).getCoinFlipPickPriority());
            }
        }
        return prios;
    }

    public void deleteMember(String name) {
        for(int i = 0; i < familyMembersList.size(); i++) {
            if(name.equals(familyMembersList.get(i).getMemberName())) {
                choosePicker(i);
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
    public ArrayList<Integer> getFamilyMemberKeys() {
        ArrayList<Integer> familyMembersStrings = new ArrayList<>();
        if (familyMembersList != null) {
            for (int i = 0; i < familyMembersList.size(); i++) {
                if(!familyMembersList.get(i).getDeleted())
                familyMembersStrings.add(familyMembersList.get(i).getKey());
            }
        }
        return familyMembersStrings;
    }

    //retrieve the family members key by their index
    public int getMemberKey(int i) {
        return familyMembersList.get(i).getKey();
    }


    public int getCoinFlipPriority(int index){
        Log.i("famMemMan", "getCoinFlipPriority: list size is " + familyMembersList.size());

        return familyMembersList.get(index).getCoinFlipPickPriority();
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

}

//TODO: check for duplicate names
