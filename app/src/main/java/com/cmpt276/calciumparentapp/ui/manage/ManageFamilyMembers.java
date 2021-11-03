package com.cmpt276.calciumparentapp.ui.manage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

public class ManageFamilyMembers extends AppCompatActivity {

    public static final String EDIT_MEMBER = "com.cmpt276.calciumparentapp.manage.ManageFamilyMembers.EDIT_MEMBER";
    private FamilyMembersManager familyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_members);

        FloatingActionButton btnAddMember = findViewById(R.id.manage_family_add_button);

        setupManageFamilyAddButton(btnAddMember);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        familyManager = FamilyMembersManager.getInstance();
        restoreFamilyManager();

        populateListView();

    }

    private void populateListView() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.family_member_list_view,
                getFamilyMembersStrings());

        ListView familyMembersList = findViewById(R.id.familyMembersList);
        familyMembersList.setAdapter(adapter);

        //enabling clicking on list view to edit family members
        familyMembersList.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(view.getContext(), ManageFamilyEdit.class);
            intent.putExtra(EDIT_MEMBER, i);
            startActivityForResult(intent, 2);
        });
    }

    private void setupManageFamilyAddButton(FloatingActionButton button) {
        button.setOnClickListener(v -> {
            // Opens the ManageFamilyAdd activity
            Intent intent = new Intent(ManageFamilyMembers.this, ManageFamilyAdd.class);
            startActivityForResult(intent, 2);
        });
    }

    // insuring that the list view updates with the new values
    // code based of off: https://www.py4u.net/discuss/622982
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode > 0)
        {
            populateListView();
        }
    }

    public ArrayList<String> getFamilyMembersStrings() {
        ArrayList<String> names = new ArrayList<>();

        SharedPreferences prefs = this.getSharedPreferences("AppPrefs", MODE_PRIVATE);
        Map<String, String> allEntries = (Map<String, String>) prefs.getAll();
        for (Map.Entry<String, String> entry : allEntries.entrySet()) {
            names.add(entry.getValue());
        }

        return names;
    }

    //getting the data from shared preferences
    public void restoreFamilyManager() { //TODO: move to the main menu?
        int numOfFamilyMembers = familyManager.getFamilyMembersNames().length;
        if(numOfFamilyMembers < 1) {
            for (int i = 0; i < getFamilyMembersStrings().size(); i++) {
                familyManager.addMember(getFamilyMembersStrings().get(i));
            }
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
        return new Intent(context, ManageFamilyMembers.class);
    }
}