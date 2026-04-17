package ca.kidstart.kidstart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.data.SavedItemsManager;
import ca.kidstart.kidstart.model.ActivityItem;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private List<ActivityItem> activityList;

    public ActivityAdapter(List<ActivityItem> activityList) {
        this.activityList = new ArrayList<>(activityList);
    }

    public void updateList(List<ActivityItem> newList) {
        this.activityList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ActivityItem item = activityList.get(position);

        holder.ivImage.setImageResource(item.getImageResId());
        holder.tvCategory.setText(item.getCategory());
        holder.tvTitle.setText(item.getTitle());
        holder.tvLocation.setText(item.getLocation());
        holder.tvAge.setText(item.getAgeRange());
        holder.tvPrice.setText(item.getPrice());
        holder.tvRating.setText(item.getRating());

        updateHeartIcon(holder.ivFavorite, item);

        holder.ivFavorite.setOnClickListener(v -> {
            SavedItemsManager.toggleSaved(item);
            updateHeartIcon(holder.ivFavorite, item);
        });
    }

    private void updateHeartIcon(ImageView imageView, ActivityItem item) {
        if (SavedItemsManager.isSaved(item)) {
            imageView.setImageResource(R.drawable.ic_heart_filled);
        } else {
            imageView.setImageResource(R.drawable.ic_heart_outline);
        }
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivFavorite;
        TextView tvCategory, tvTitle, tvLocation, tvAge, tvPrice, tvRating;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivActivityImage);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}