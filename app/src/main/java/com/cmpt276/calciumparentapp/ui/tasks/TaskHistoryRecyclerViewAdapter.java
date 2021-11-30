package com.cmpt276.calciumparentapp.ui.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.manage.FamilyMembersManager;
import com.cmpt276.calciumparentapp.model.tasks.Task;
import com.cmpt276.calciumparentapp.model.tasks.TaskIteration;
import com.cmpt276.calciumparentapp.model.tasks.TaskManager;

//TODO Debug and refactor

/**
 * Converts task data into views that RecyclerView can display
 */
public class TaskHistoryRecyclerViewAdapter extends RecyclerView.Adapter<TaskHistoryRecyclerViewAdapter.ViewHolder> {
    private final TaskManager taskManager;
    private final FamilyMembersManager familyManager;
    private final Context mContext;
    int taskIndex;

    public TaskHistoryRecyclerViewAdapter(Context context, TaskManager taskManager, FamilyMembersManager familyManager, int taskIndex) {
        mContext = context;
        this.taskManager = taskManager;
        this.familyManager = familyManager;
        this.taskIndex = taskIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_task_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (taskManager.getTaskHistory(taskIndex) != null) {
            // Grab Data for ViewHolder
            TaskIteration currentIteration = taskManager.getTaskHistory(taskIndex).get(position);

            int currentChildID = currentIteration.getChildID();
            String currentChildName = familyManager.getFamilyMemberFromID(currentChildID).getMemberName();

            String currentTime = currentIteration.getDate();

            // Sets data inside of ViewHolder
            holder.nameText.setText(currentChildName);
            holder.dateText.setText(currentTime);
            holder.childPictureImage.setImageBitmap(familyManager.getFamilyMemberFromID(currentChildID).getProfileBitmap());
        }
    }

    @Override
    public int getItemCount() {
        if (taskManager.getTaskHistory(taskIndex) != null) {
            return taskManager.getTaskHistory(taskIndex).size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView dateText;
        ImageView childPictureImage;
        ConstraintLayout taskHistoryLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.task_history_text);
            dateText = itemView.findViewById(R.id.task_history_date);
            childPictureImage = itemView.findViewById(R.id.task_history_image);
            taskHistoryLayout = itemView.findViewById(R.id.task_history_layout);
        }
    }
}
