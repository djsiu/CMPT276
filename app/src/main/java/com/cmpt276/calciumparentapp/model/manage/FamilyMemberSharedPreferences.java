package com.cmpt276.calciumparentapp.model.manage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class FamilyMemberSharedPreferences {
    private static FamilyMembersManager familyManager;
    private static final String SHARED_PREFS_KEY = "AppPrefs";
    private static final String SHARED_PREFS_FAMILY_MANAGER_KEY = "FamilyManager";

    public static Boolean addMember(String newMemberNameStr){

        familyManager = FamilyMembersManager.getInstance();

        //String newMemberNameStr = newMemberName.getText().toString();
        boolean nameAlreadyExists = false;

        if(familyManager.getFamilyMembersNames().size() > 0) {
            for (int i = 0; i < familyManager.getFamilyMembersNames().size(); i++) {
                if (newMemberNameStr.equals(familyManager.getFamilyMembersNames().get(i))) {
                    nameAlreadyExists = true;
                }
            }
        }
        return nameAlreadyExists;
    }

    //credit to eamonnmcmanus on github
    public static void saveFamilyManagerToSharedPrefs(Context context) {
        familyManager = FamilyMembersManager.getInstance();
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(familyManager);
        editor.putString("FamilyManager", json);
        editor.apply();
    }

    public static void getFamilyManagerFromSharedPrefs(Context context) {
        familyManager = FamilyMembersManager.getInstance();
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(SHARED_PREFS_FAMILY_MANAGER_KEY, "");

        familyManager.setFamilyMembersList(gson.fromJson(json, FamilyMembersManager.class).getFamilyMembersList());
        familyManager.setKeyGenerator(gson.fromJson(json, FamilyMembersManager.class).getKeyGenerator());
        if(familyManager == null) {
            familyManager = FamilyMembersManager.getInstance();
        }
    }





}
