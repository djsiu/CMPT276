package com.cmpt276.calciumparentapp.ui.coinflip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.cmpt276.calciumparentapp.ui.manage.ManageFamilyEdit;
import com.cmpt276.calciumparentapp.ui.manage.ManageFamilyMembers;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

// array of options --> array adapter --> ListView

//List View: {views: recycler_name_list_items.xml}

public class CoinFlipSelection extends AppCompatActivity {
    
    private String TAG = "CoinFlipSelection";

    private ArrayList<String> nameArrayList = new ArrayList<String>();

    private FamilyMembersManager familyMembersManager;

    private int selected;
    private List<String> selectedIndex = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_selection);

        populateListView();
        selected = 0;

        Button button = findViewById(R.id.coin_selection_button_continue);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = CoinFlip.makeIntent(CoinFlipSelection.this);
                startActivity(i);
            }
        });
        button.setClickable(false);
        button.setFocusable(false);


    }


    private void populateArray(){
        nameArrayList.add("a");
        nameArrayList.add("b");
        nameArrayList.add("c");
        nameArrayList.add("d");

    }

    private void populateListView() {
        //create List of items
        populateArray();

        //Build adapter
        ArrayAdapter<String> adapter = new MyListAdapter();
        Log.i(TAG, "populateListView: After build adapter");

        ListView list = (ListView) findViewById(R.id.coin_list_names);
        Log.i(TAG, "populateListView: assign list");
        list.setAdapter(adapter);
        Log.i(TAG, "populateListView: set adapter");

        list.setOnItemClickListener((adapterView, view, position, id) -> {
            //if already selected
            if(selectedIndex.contains(nameArrayList.get(position))){
                selectedIndex.remove(nameArrayList.get(position));
                //reset color
                view.setBackgroundColor(Color.TRANSPARENT);

            }else{// if not already selected
                if(selectedIndex.size() < 2){
                    selectedIndex.add(nameArrayList.get(position));
                    view.setBackgroundColor(Color.GRAY);

                }

            }

            Button button = (Button) findViewById(R.id.coin_selection_button_continue);
            Log.i(TAG, "populateListView: selectedIndex size is " + selectedIndex.size());
            if (selectedIndex.size() != 2){
                button.setClickable(false);
                button.setFocusable(false);
            }else{
                button.setClickable(true);
                button.setFocusable(true);
            }

        });
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
            Log.i(TAG, "getView: Fill view");
            TextView textView = itemView.findViewById(R.id.textView_list_name);
            Log.i(TAG, "getView: name is \n\n\n" + name + "\n\n\n ");
            if(textView == null){
                Log.i(TAG, "getView: tv was null");
            }else{
                Log.i(TAG, "getView: set "+ name);
                textView.setText(name);
            }


            return itemView;
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, CoinFlipSelection.class);
    }
}