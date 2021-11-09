package com.cmpt276.calciumparentapp.ui.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMemberSharedPreferences;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManageFamilyMembers extends AppCompatActivity {

    public static final String EDIT_MEMBER = "com.cmpt276.calciumparentapp.manage.ManageFamilyMembers.EDIT_MEMBER";

    private FamilyMembersManager familyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_members);

        familyManager = FamilyMembersManager.getInstance();

        FloatingActionButton btnAddMember = findViewById(R.id.manage_family_add_button);

        setupManageFamilyAddButton(btnAddMember);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        FamilyMemberSharedPreferences.getFamilyManagerFromSharedPrefs(this);
        populateListView();

    }


    @Override
    protected void onResume() {
        super.onResume();

        populateListView();
    }

    private void populateListView() {

        FamilyMemberSharedPreferences.getFamilyManagerFromSharedPrefs(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.family_member_list_view,
                familyManager.getFamilyMembersNames());

        ListView familyMembersListView = findViewById(R.id.familyMembersList);
        familyMembersListView.setAdapter(adapter);

        //enabling clicking on list view to edit family members
        familyMembersListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(view.getContext(), ManageFamilyEdit.class);
            intent.putExtra(EDIT_MEMBER, (String) familyMembersListView.getItemAtPosition(i));
            startActivity(intent);
        });

    }

    private void setupManageFamilyAddButton(FloatingActionButton button) {
        button.setOnClickListener(v -> {
            // Opens the ManageFamilyAdd activity
            Intent intent = new Intent(this, ManageFamilyAdd.class);
            startActivity(intent);
        });
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
        return new Intent(context, ManageFamilyMembers.class);
    }
}