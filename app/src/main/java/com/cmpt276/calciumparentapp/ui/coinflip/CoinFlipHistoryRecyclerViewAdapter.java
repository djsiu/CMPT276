package com.cmpt276.calciumparentapp.ui.coinflip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFlipManager;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFlipGame;

import java.util.List;

/**
 * Converts task data into views that RecyclerView can display
 */
public class CoinFlipHistoryRecyclerViewAdapter extends RecyclerView.Adapter<CoinFlipHistoryRecyclerViewAdapter.ViewHolder> {
    private final CoinFlipManager coinFlipManager;
    private final Context mContext;

    public CoinFlipHistoryRecyclerViewAdapter(Context context, CoinFlipManager coinFlipManager) {
        mContext = context;
        this.coinFlipManager = coinFlipManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.history_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinFlipGame game = coinFlipManager.getGamesList().get(position);

        holder.gameTextView.setText(game.getGameText(mContext));
        //picker image
        if(game.getPickerID() == -1) {
            holder.pickerImage.setVisibility(View.GONE);
        }
        else {
            //put image here
            holder.pickerImage.setImageBitmap(game.getPickerPhotoId(mContext));
        }

        //result Icon
        if(game.isGameWonByPicker()){//if picker won
            holder.resultIcon.setImageResource(R.drawable.win_flip_history);
        }else{//if picker lost
            holder.resultIcon.setImageResource( R.drawable.lose_flip_history);
        }
    }

    @Override
    public int getItemCount() {
        return coinFlipManager.getGamesList().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView gameTextView;
        ImageView pickerImage;
        ImageView resultIcon;
        RelativeLayout historyLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTextView = itemView.findViewById(R.id.history_text);
            pickerImage = itemView.findViewById(R.id.picker_history_icon);
            resultIcon = itemView.findViewById(R.id.win_lose_history_icon);
            historyLayout = itemView.findViewById(R.id.history_layout_constraint);
        }
    }
}
