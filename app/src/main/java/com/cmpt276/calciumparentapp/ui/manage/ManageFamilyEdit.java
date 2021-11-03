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

import java.util.ArrayList;
import java.util.Map;

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

        familyManager = FamilyMembersManager.getInstance();

        // makes the current name appear in the editText
        EditText editTextName = findViewById(R.id.editTextMemberNameForEdit);
        editTextName.setText(getFamilyMemberNameString(getFamilyMemberPos()));

        setupCancelBtn();
        setupSaveBtn();
        setupDeleteBtn();
    }

    private int getFamilyMemberPos() {
        Intent intent = getIntent();
        return intent.getIntExtra(ManageFamilyMembers.EDIT_MEMBER, 0);
    }

    private void setupDeleteBtn() {
        Button deleteBtn = findViewById(R.id.deleteMemberBtn);

        deleteBtn.setOnClickListener(view -> {
            //deleting from shared preferences
            SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(familyManager.getMemberKeyByIndex(getFamilyMemberPos()));
            editor.apply();

            familyManager.deleteMember(getFamilyMemberPos());

            finish();
        });
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.saveEditMemberBtn);

        EditText editMemberName = findViewById(R.id.editTextMemberNameForEdit);
        saveBtn.setOnClickListener(view -> {
            familyManager.editMember(
                    editMemberName.getText().toString(),
                    getFamilyMemberPos()
            );

            finish();
        });
    }

    private void setupCancelBtn() {
        Button cancelBtn = findViewById(R.id.cancelEditMember);
        cancelBtn.setOnClickListener(view -> finish());
    }

    public void saveFamilyMember(String name, String memberKey) {
        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(memberKey, name);
        editor.apply();
    }

//    private int getNumOfMembers() {
//        int numOfMembers = 0;
//
//        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
//        Map<String, String> allEntries = (Map<String, String>) prefs.getAll();
//        for (Map.Entry<String, String> entry : allEntries.entrySet()) {
//            numOfMembers++;
//        }
//        return numOfMembers;
//    }

    public String getFamilyMemberNameString(int i) {
        ArrayList<String> names = new ArrayList<>();

        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        Map<String, String> allEntries = (Map<String, String>) prefs.getAll();
        for (Map.Entry<String, String> entry : allEntries.entrySet()) {
            names.add(entry.getValue());
        }

        return names.get(i);
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