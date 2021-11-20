package com.cmpt276.calciumparentapp.ui.coinflip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFlipManager;
import com.cmpt276.calciumparentapp.model.manage.FamilyMember;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;

import java.util.ArrayList;
import java.util.List;

/**
used to select two members who will play heads or tails
 */
public class CoinFlipSelection extends AppCompatActivity {

    private ArrayList<String> nameArrayList;
    private ArrayList<Integer> keyArrayList;
    private FamilyMembersManager familyManager;
    private final List<Integer> selectedIndexes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_selection);
        familyManager = FamilyMembersManager.getInstance(this);

        nameArrayList = familyManager.getFamilyMembersNames();
        keyArrayList = familyManager.getFamilyMemberKeys();

        if(!hasEnoughFamilyMembers()){
            beginMemberlessGame();
            Intent i = CoinFlip.makeIntent(this);
            finish();
            startActivity(i);
        }
        populateListView();

        Button button = findViewById(R.id.coin_selection_button_continue);
        button.setOnClickListener(view -> continueButtonOnClick());

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }



    private void continueButtonOnClick() {
        if(selectedIndexes.size() == 0){
            //player id of -1 means there is no player there
            beginMemberlessGame();
            Intent i = CoinFlip.makeIntent(CoinFlipSelection.this);
            startActivity(i);
        }else if(selectedIndexes.size() == 2){
            CoinFlipManager coinFlipManager = CoinFlipManager.getInstance(this);
            coinFlipManager.beginGame(keyArrayList.get(selectedIndexes.get(0)),
                    keyArrayList.get(selectedIndexes.get(1)));

            Intent i = CoinFlip.makeIntent(CoinFlipSelection.this);
            startActivity(i);
        }
        else{
            //TODO: rename this string resource to include no child answers
            Toast toast = Toast.makeText(this, R.string.coinflip_selection_incorrect_children_toast_text, Toast.LENGTH_SHORT);
            toast.show();
        }

        //Adds back button in top left corner
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Sets up a game that has no members, initializing their player ID values to -1
     */
    private void beginMemberlessGame(){
        CoinFlipManager coinFlipManager = CoinFlipManager.getInstance(this);
        coinFlipManager.beginGame(-1,-1);
    }


    private void populateListView() {
        //Build adapter
        ArrayAdapter<FamilyMember> adapter = new MyListAdapter();

        ListView list = findViewById(R.id.coin_list_names);
        list.setAdapter(adapter);

        list.setOnItemClickListener((adapterView, view, position, id) -> {
            //if already selected
            int selectedIndex = selectedIndexes.indexOf(position);
            if(selectedIndex != -1){//must remove at index of contained not at position in master list
                selectedIndexes.remove(selectedIndex);
                //reset color
                view.setBackgroundColor(Color.TRANSPARENT);

            }
            else {// if not already selected
                if(selectedIndexes.size() < 2){
                    selectedIndexes.add(position);
                    view.setBackgroundColor(Color.GRAY);
                }
            }
        });
    }

    private boolean hasEnoughFamilyMembers() {
        return nameArrayList.size() >= 2;
    }


    // display photos and text per family member
    private class MyListAdapter extends ArrayAdapter<FamilyMember> {
        public MyListAdapter() {
            super(CoinFlipSelection.this, R.layout.family_member_item_view, familyManager.getFamilyMemberObjects());
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
            ImageView imageView = itemView.findViewById(R.id.member_profile_photo);
            imageView.setImageResource(currentMember.getIconID());

            // Display member name
            TextView makeText = itemView.findViewById(R.id.member_name_text);
            makeText.setText(currentMember.getMemberName());

            return itemView;
        }
    }


    /**
     * Displays actionbar buttons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_coin_flip, menu);
        return true;
    }

    /**
     * Adds logic to action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_history) {      // History Button
            openCoinFlipHistory();
            return true;
        } else if (item.getItemId() == android.R.id.home){  // Top left back arrow
            finish();
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    private void openCoinFlipHistory() {
        Intent i = CoinFlipHistory.makeIntent(CoinFlipSelection.this);
        startActivity(i);
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, CoinFlipSelection.class);
    }
}