package ca.kidstart.kidstart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.model.ChipItem;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ChipViewHolder> {

    public interface OnChipClickListener {
        void onChipClick(int position);
    }

    private final Context context;
    private final List<ChipItem> chipList;
    private final OnChipClickListener listener;

    public ChipAdapter(Context context, List<ChipItem> chipList, OnChipClickListener listener) {
        this.context = context;
        this.chipList = chipList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chip, parent, false);
        return new ChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
        ChipItem item = chipList.get(position);

        holder.tvChip.setText(item.getLabel());

        if (item.isSelected()) {
            holder.cardChip.setCardBackgroundColor(ContextCompat.getColor(context, R.color.purple_primary));
            holder.cardChip.setStrokeColor(ContextCompat.getColor(context, R.color.purple_primary));
            holder.tvChip.setTextColor(ContextCompat.getColor(context, R.color.text_on_primary));
        } else {
            holder.cardChip.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface));
            holder.cardChip.setStrokeColor(ContextCompat.getColor(context, R.color.stroke));
            holder.tvChip.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onChipClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return chipList.size();
    }

    public void setSelectedPosition(int selectedPosition) {
        for (int i = 0; i < chipList.size(); i++) {
            chipList.get(i).setSelected(i == selectedPosition);
        }
        notifyDataSetChanged();
    }

    static class ChipViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardChip;
        TextView tvChip;

        public ChipViewHolder(@NonNull View itemView) {
            super(itemView);
            cardChip = (MaterialCardView) itemView;
            tvChip = itemView.findViewById(R.id.tvChip);
        }
    }
}