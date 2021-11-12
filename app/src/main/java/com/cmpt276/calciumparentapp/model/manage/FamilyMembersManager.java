package com.cmpt276.calciumparentapp.model.manage;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
Managing the members of the family.
adding, deleting and retrieving info about all the family members
 */
public class FamilyMembersManager {

    private final ArrayList<FamilyMember> familyMembersList;
    private static final String SHARED_PREFS_KEY = "ParentAppSharedPrefs";
    private static final String SHARED_PREFS_FAMILY_MANAGER_KEY = "FamilyManagerSharedPrefsKey";

    private int keyGenerator;
    // Context is transient so it does not get saved to shared prefs
    private final transient Context context;

    //singleton support
    // By using getApplicationContext in the singleton the memory leak is fixed
    @SuppressLint("StaticFieldLeak")
    private static FamilyMembersManager instance;

    public static FamilyMembersManager getInstance(Context context) {
        if(instance == null) {
            generateInstance(context.getApplicationContext());
        }
        return instance;
    }

    private FamilyMembersManager(Context context) {
        this.context = context;
        familyMembersList = new ArrayList<>();
        keyGenerator = 0;
    }

    // Sets the instance variable to an instance of this class
    // Loads the class from shared preferences if one exists
    // otherwise calls the constructor.
    private static void generateInstance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(FamilyMembersManager.class,
                new FamilyMembersManagerInstanceCreator(context));

        Gson gson = gsonBuilder.create();
        String json = prefs.getString(SHARED_PREFS_FAMILY_MANAGER_KEY, "");
        if(!json.equals("")){
            instance = gson.fromJson(json, FamilyMembersManager.class);
        }
        else{
            instance = new FamilyMembersManager(context);
        }

    }

    /**
     * Gets the next family member in task order.
     * @param id The ID of the family member
     * @return The ID of the next family member
     */
    public int getNextFamilyMemberInOrder(int id) {
        if(id >= keyGenerator) {
            throw new IllegalArgumentException("Invalid ID");
        }

        // loop until reaching a family member that is not deleted
        // if the end is reached, loop back to the start
        do {
            id++;
            if(id == keyGenerator){
                id = 0;
            }
        }
        while(getFamilyMemberFromID(id).isDeleted());

        return id;

    }

    private static class FamilyMembersManagerInstanceCreator implements InstanceCreator<FamilyMembersManager> {
        private final Context context;

        public FamilyMembersManagerInstanceCreator(Context context){
            this.context = context;
        }

        @Override
        public FamilyMembersManager createInstance(Type type) {
            return new FamilyMembersManager(context);
        }
    }

    public void addMember(String name, int profilePhotoID) {
        FamilyMember newMember = new FamilyMember(name, keyGenerator, familyMembersList.size(), profilePhotoID);
        familyMembersList.add(newMember);
        keyGenerator++;
        saveToSharedPrefs();
    }

    private void saveToSharedPrefs() {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(this);
        editor.putString(SHARED_PREFS_FAMILY_MANAGER_KEY, json);
        editor.apply();
    }

    public void changeMemberName(String newName, String name) {
        for(int i = 0; i < familyMembersList.size(); i++) {
            if(name.equals(familyMembersList.get(i).getMemberName())) {
                familyMembersList.set(i, familyMembersList.get(i).changeName(newName));
            }
        }
        saveToSharedPrefs();
    }

    public void deleteMember(String name) {
        for(int i = 0; i < familyMembersList.size(); i++) {
            if(name.equals(familyMembersList.get(i).getMemberName())) {
                choosePicker(i);
                familyMembersList.get(i).deleteChild();
            }
        }
        saveToSharedPrefs();
    }

    // returns all non-deleted family members' names
    public ArrayList<String> getFamilyMembersNames() {
        ArrayList<String> familyMembersStrings = new ArrayList<>();
        for (int i = 0; i < familyMembersList.size(); i++) {
            if(!familyMembersList.get(i).isDeleted()) {
                familyMembersStrings.add(familyMembersList.get(i).getMemberName());
            }
        }
        return familyMembersStrings;
    }

    public ArrayList<Integer> getFamilyMemberKeys() {
        ArrayList<Integer> familyMembersStrings = new ArrayList<>();
        for (int i = 0; i < familyMembersList.size(); i++) {
            if(!familyMembersList.get(i).isDeleted())
            familyMembersStrings.add(familyMembersList.get(i).getKey());
        }
        return familyMembersStrings;
    }

    // returns all non-deleted family member objects
    public ArrayList<FamilyMember> getFamilyMemberObjects() {
        ArrayList<FamilyMember> activeMembers = new ArrayList<>();

        for(int i = 0; i < familyMembersList.size(); i++) {
            if(!familyMembersList.get(i).isDeleted()) {
                activeMembers.add(familyMembersList.get(i));
            }
        }
        return activeMembers;
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

    public FamilyMember getFamilyMemberFromID(int ID) {
        for(FamilyMember familyMember : familyMembersList) {
            if(familyMember.getKey() == ID){
                return familyMember;
            }
        }
        throw new IllegalArgumentException("No family member with provided ID");
    }

}