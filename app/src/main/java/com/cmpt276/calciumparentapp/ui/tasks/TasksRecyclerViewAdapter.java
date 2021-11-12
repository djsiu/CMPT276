package com.cmpt276.calciumparentapp.ui.tasks;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.tasks.TaskManager;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "TasksRecyclerViewAdapter";
    private final TaskManager taskManager;
    private Context mContext;

    public TasksRecyclerViewAdapter(Context context, TaskManager taskManager) {
        mContext = context;
        this.taskManager  = taskManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.task_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.childNameView.setText(taskManager.getChildName(position));
        holder.taskNameView.setText(taskManager.getTaskName(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // position is not fixed. Must use getAdapterPosition
                int index = holder.getAdapterPosition();
                Intent i = ViewTask.makeIntent(mContext, index);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskManager.getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView taskNameView;
        TextView childNameView;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNameView = itemView.findViewById(R.id.task_item_name);
            childNameView = itemView.findViewById(R.id.task_item_child_name);
            parentLayout = itemView.findViewById(R.id.task_item_parent_layout);

        }
    }

}
