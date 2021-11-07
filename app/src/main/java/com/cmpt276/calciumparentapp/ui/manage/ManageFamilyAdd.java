package com.cmpt276.calciumparentapp.ui.manage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMember;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.google.gson.Gson;


public class ManageFamilyAdd extends AppCompatActivity {

    private FamilyMembersManager familyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_add);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        setupCancelBtn();
        setupAddBtn();

        getFamilyManagerFromSharedPrefs();
    }

    private void setupAddBtn() {
        Button addBtn = findViewById(R.id.addNewMemberButton);
        EditText newMemberName = findViewById(R.id.editTextFamilyMemberName);
        addBtn.setOnClickListener(view -> {

            String newMemberNameStr = newMemberName.getText().toString();
            boolean nameAlreadyExists = false;

            if(familyManager.getFamilyMembersNames().size() > 0) {
                for (int i = 0; i < familyManager.getFamilyMembersNames().size(); i++) {
                    if (newMemberNameStr.equals(familyManager.getFamilyMembersNames().get(i))) {
                        nameAlreadyExists = true;
                    }
                }
            }

            if(!nameAlreadyExists) {
                familyManager.addMember(newMemberNameStr);
                saveFamilyManagerToSharedPrefs();

                Toast.makeText(getApplicationContext(),
                        "Welcome to the family " + newMemberName.getText().toString() + "!",
                        Toast.LENGTH_SHORT)
                        .show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        newMemberName.getText().toString() + " is already in the family!",
                        Toast.LENGTH_SHORT)
                        .show();
            }

//            //updating the list view in ManageFamilyMembers activity
//            Intent intent=new Intent();
//            setResult(2,intent);
//            finish();
        });
    }

    private void setupCancelBtn() {
        Button cancelBtn = findViewById(R.id.cancelAddNewMemberButton);
        cancelBtn.setOnClickListener(view -> finish());
    }

    //credit to eamonnmcmanus on github
    private void saveFamilyManagerToSharedPrefs() {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(familyManager);
        editor.putString("FamilyManager", json);
        editor.apply();
    }

    private void getFamilyManagerFromSharedPrefs() {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("FamilyManager", "");

        familyManager = gson.fromJson(json, FamilyMembersManager.class);
        if(familyManager == null) {
            familyManager = FamilyMembersManager.getInstance();
        }
    }

    /**
     * Adds logic to action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Top left back arrow
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, ManageFamilyAdd.class);
    }
}