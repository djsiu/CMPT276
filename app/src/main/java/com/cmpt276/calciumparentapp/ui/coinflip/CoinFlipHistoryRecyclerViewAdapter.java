package com.cmpt276.calciumparentapp.ui.coinflip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpt276.calciumparentapp.R;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFlipGame;
import com.cmpt276.calciumparentapp.model.coinflip.CoinFlipManager;

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
                R.layout.list_item_coin_flip_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinFlipGame game = coinFlipManager.getGamesList().get(position);

        holder.gameTextView.setText(game.getGameText(mContext));
        //picker image
        if(game.getPickerID() == -1 || game.isPickerDeleted(mContext)) {
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTextView = itemView.findViewById(R.id.coin_flip_history_text);
            pickerImage = itemView.findViewById(R.id.coin_flip_history_child_image);
            resultIcon = itemView.findViewById(R.id.coin_flip_history_win_loss_icon);
        }
    }
}
