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

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.google.gson.Gson;

public class ManageFamilyEdit extends AppCompatActivity {

    private FamilyMembersManager familyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_edit);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        getFamilyManagerFromSharedPrefs();

        // makes the current name appear in the editText
        EditText editTextName = findViewById(R.id.editTextMemberNameForEdit);
        editTextName.setText(getFamilyMemberName());

        setupCancelBtn();
        setupSaveBtn();
        setupDeleteBtn();
    }

    //retrieving the name that was clicked on in the list view
    private String getFamilyMemberName() {
        Intent intent = getIntent();
        return intent.getStringExtra(ManageFamilyMembers.EDIT_MEMBER);
    }

    private void setupDeleteBtn() {
        Button deleteBtn = findViewById(R.id.deleteMemberBtn);

        deleteBtn.setOnClickListener(view -> {

            familyManager.deleteMember(getFamilyMemberName());
            saveFamilyManagerToSharedPrefs();

            //updating the list view in ManageFamilyMembers activity
            Intent intent=new Intent();
            setResult(2,intent);
            finish();
        });
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.saveEditMemberBtn);

        EditText editMemberName = findViewById(R.id.editTextMemberNameForEdit);
        saveBtn.setOnClickListener(view -> {

            //edit the person in family manager
            familyManager.editMember(
                    editMemberName.getText().toString(),
                    getFamilyMemberName()
            );

            saveFamilyManagerToSharedPrefs();
            finish();
        });
    }

    private void setupCancelBtn() {
        Button cancelBtn = findViewById(R.id.cancelEditMember);
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
        return new Intent(context, ManageFamilyEdit.class);
    }
}