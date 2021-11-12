package com.cmpt276.calciumparentapp.ui.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMember;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
Activity for displaying family members, choosing members to edit and adding a new member
 */
public class ManageFamilyMembers extends AppCompatActivity {

    public static final String EDIT_MEMBER = "com.cmpt276.calciumparentapp.manage.ManageFamilyMembers.EDIT_MEMBER";

    private FamilyMembersManager familyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_members);

        familyManager = FamilyMembersManager.getInstance(this);

        FloatingActionButton btnAddMember = findViewById(R.id.manage_family_add_button);

        setupManageFamilyAddButton(btnAddMember);

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        populateListViewAndLaunchEdit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateListViewAndLaunchEdit();
    }

    private void setupManageFamilyAddButton(FloatingActionButton button) {
        button.setOnClickListener(v -> {
            // Opens the ManageFamilyAdd activity
            Intent intent = new Intent(this, ManageFamilyAdd.class);
            startActivity(intent);
        });
    }

    private void populateListViewAndLaunchEdit() {
        showNoMembersMessage();

        ArrayAdapter<FamilyMember> adapter = new MyListAdapter();
        ListView familyMembersListView = (ListView) findViewById(R.id.family_members_list);
        familyMembersListView.setAdapter(adapter);

        //enabling clicking on list view to edit family members
        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.family_members_list);
        list.setOnItemClickListener((parent, viewClicked, position, id) -> {

            Intent intent = new Intent(viewClicked.getContext(), ManageFamilyEdit.class);
            FamilyMember clickedMember = familyManager.getFamilyMemberObjects().get(position);

            intent.putExtra(EDIT_MEMBER, clickedMember.getMemberName());
            startActivity(intent);
        });
    }

    // display photos and text per family member
    private class MyListAdapter extends ArrayAdapter<FamilyMember> {
        public MyListAdapter() {
            super(ManageFamilyMembers.this, R.layout.family_member_item_view, familyManager.getFamilyMemberObjects());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.family_member_item_view, parent, false);
            }

            // Find family member to work with
            FamilyMember currentMember = familyManager.getFamilyMemberObjects().get(position);

            // Retrieve image
            ImageView imageView = (ImageView)itemView.findViewById(R.id.member_profile_photo);
            imageView.setImageResource(currentMember.getIconID());

            // Display member name
            TextView makeText = (TextView) itemView.findViewById(R.id.member_name_text);
            makeText.setText(currentMember.getMemberName());

            return itemView;
        }
    }

    private void showNoMembersMessage() {
        TextView textView = findViewById(R.id.no_family_members_text);
        textView.setVisibility(TextView.VISIBLE);
        if(familyManager.getFamilyMembersNames().size() > 0) {
            textView.setVisibility(TextView.INVISIBLE);
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