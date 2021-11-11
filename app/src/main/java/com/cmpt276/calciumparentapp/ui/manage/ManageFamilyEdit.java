package com.cmpt276.calciumparentapp.ui.manage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;

/**
 * Activity to edit family members when clicking on their name
 * user can edit name and delete child
 */
public class ManageFamilyEdit extends AppCompatActivity {

    private FamilyMembersManager familyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_edit);

        familyManager = FamilyMembersManager.getInstance(this);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

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
            Log.i("edit", "setupDeleteBtn: before delete");
            familyManager.deleteMember(getFamilyMemberName());

            finish();
        });
    }

    private void setupSaveBtn() {
        Button saveBtn = findViewById(R.id.saveEditMemberBtn);

        EditText editMemberName = findViewById(R.id.editTextMemberNameForEdit);
        saveBtn.setOnClickListener(view -> {
            String newMemberNameStr = editMemberName.getText().toString();
            boolean nameAlreadyExists = familyManager.isMemberNameUsed(newMemberNameStr);

            if(!nameAlreadyExists) {

                familyManager.changeMemberName(
                        editMemberName.getText().toString(),
                        getFamilyMemberName());

                finish();
            } else {
                String alreadyPresentText = String.format(
                        getString(R.string.family_member_present_toast_text_format),
                        editMemberName.getText().toString());

                Toast.makeText(getApplicationContext(),
                        alreadyPresentText,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void setupCancelBtn() {
        Button cancelBtn = findViewById(R.id.cancelEditMember);
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

}