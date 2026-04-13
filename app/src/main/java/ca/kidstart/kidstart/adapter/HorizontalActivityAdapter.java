package ca.kidstart.kidstart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.model.ActivityItem;

public class HorizontalActivityAdapter extends RecyclerView.Adapter<HorizontalActivityAdapter.HorizontalViewHolder> {

    private final List<ActivityItem> activityList;

    public HorizontalActivityAdapter(List<ActivityItem> activityList) {
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity_horizontal, parent, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        ActivityItem item = activityList.get(position);

        holder.ivImage.setImageResource(item.getImageResId());
        holder.tvAgeBadge.setText(item.getAgeRange());
        holder.tvCategory.setText(item.getCategory().getName());
        holder.tvTitle.setText(item.getTitle());
        holder.tvDistance.setText(item.getDistance());
        holder.tvPrice.setText(item.getPrice());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    static class HorizontalViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ImageView ivFavorite;
        TextView tvAgeBadge;
        TextView tvCategory;
        TextView tvTitle;
        TextView tvDistance;
        TextView tvPrice;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivActivityImage);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvAgeBadge = itemView.findViewById(R.id.tvAgeBadge);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}