package com.cmpt276.calciumparentapp.ui.coinflip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMemberSharedPreferences;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//List View: {views: recycler_name_list_items.xml}

public class CoinFlipSelection extends AppCompatActivity {

    private FamilyMembersManager familyManager;

    private ArrayList<String> nameArrayList;
    private List<String> selectedIndexes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_selection);


        FamilyMemberSharedPreferences.getFamilyManagerFromSharedPrefs(this);
        familyManager = FamilyMembersManager.getInstance();

        nameArrayList = familyManager.getFamilyMembersNames();

        if(!hasEnoughFamilyMembers()){
            Intent i = CoinFlip.makeIntent(this);
            finish();
            startActivity(i);
        }
        populateListView();

        Button button = findViewById(R.id.coin_selection_button_continue);
        button.setOnClickListener(view -> {
            continueButtonOnClick();
        });
    }



    private void continueButtonOnClick() {
        if(selectedIndexes.size() != 2){
            Toast toast = Toast.makeText(this, R.string.coinflip_selection_two_children_toast_text, Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            Intent i = CoinFlip.makeIntent(CoinFlipSelection.this);
            i.putExtra("player1", selectedIndexes.get(0));
            i.putExtra("player2", selectedIndexes.get(1));
            finish();
            startActivity(i);
        }
    }


    private void populateListView() {
        //create List of items
        //populateArray();

        //Build adapter
        ArrayAdapter<String> adapter = new MyListAdapter();

        ListView list = (ListView) findViewById(R.id.coin_list_names);
        list.setAdapter(adapter);

        list.setOnItemClickListener((adapterView, view, position, id) -> {
            //if already selected
            if(selectedIndexes.contains(nameArrayList.get(position))){
                selectedIndexes.remove(nameArrayList.get(position));
                //reset color
                view.setBackgroundColor(Color.TRANSPARENT);

            }else{// if not already selected
                if(selectedIndexes.size() < 2){
                    selectedIndexes.add(nameArrayList.get(position));
                    view.setBackgroundColor(Color.GRAY);

                }
            }

        });
    }

    private boolean hasEnoughFamilyMembers() {
        return nameArrayList.size() >= 2;
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        public MyListAdapter() {
            super(CoinFlipSelection.this, R.layout.recycler_name_list_items, nameArrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Make sure we have a view to work with (may have been given null
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.recycler_name_list_items, parent, false);
            }

            //find the name
            String name = nameArrayList.get(position);

            //fill the view
            TextView textView = itemView.findViewById(R.id.textView_list_name);
            textView.setText(name);
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