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
import com.cmpt276.calciumparentapp.model.manage.FamilyMemberSharedPreferences;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.google.gson.Gson;


public class ManageFamilyAdd extends AppCompatActivity {

    private FamilyMembersManager familyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_add);

        familyManager = FamilyMembersManager.getInstance();

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        setupCancelBtn();
        setupAddBtn();

        FamilyMemberSharedPreferences.getFamilyManagerFromSharedPrefs(this);
    }

    private void setupAddBtn() {
        Button addBtn = findViewById(R.id.addNewMemberButton);
        EditText newMemberName = findViewById(R.id.editTextFamilyMemberName);
        addBtn.setOnClickListener(view -> {

            String newMemberNameStr = newMemberName.getText().toString();
            boolean nameAlreadyExists = FamilyMemberSharedPreferences.addMember(newMemberNameStr);

            if(!nameAlreadyExists) {
                familyManager.addMember(newMemberNameStr);
                FamilyMemberSharedPreferences.saveFamilyManagerToSharedPrefs(this);

                String welcomeText = String.format(
                        getString(R.string.family_member_welcome_toast_text_format),
                        newMemberName.getText().toString());

                Toast.makeText(getApplicationContext(),
                        welcomeText,
                        Toast.LENGTH_SHORT)
                        .show();
                finish();
            } else {

                String alreadyPresentText = String.format(
                        getString(R.string.family_member_present_toast_text_format),
                        newMemberName.getText().toString());

                Toast.makeText(getApplicationContext(),
                        alreadyPresentText,
                        Toast.LENGTH_SHORT)
                        .show();
            }

        });
    }

    private void setupCancelBtn() {
        Button cancelBtn = findViewById(R.id.cancelAddNewMemberButton);
        cancelBtn.setOnClickListener(view -> finish());
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
//