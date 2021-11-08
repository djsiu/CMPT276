package com.cmpt276.calciumparentapp.model.coinFlip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpt276.calciumparentapp.R;

import java.util.ArrayList;

public class nameListAdapter extends RecyclerView.Adapter<nameListAdapter.MyViewHolder> {
    private ArrayList<String> nameList;

    public nameListAdapter(ArrayList<String> nameList){
        this.nameList = nameList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTxt;

        public MyViewHolder(final View view){
            super((view));
            nameTxt = view.findViewById(R.id.textView_list_name);
        }
    }


    @NonNull
    @Override
    public nameListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_name_list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull nameListAdapter.MyViewHolder holder, int position) {
        String name = nameList.get(position);
        holder.nameTxt.setText(name);

    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }
}
