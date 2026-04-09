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
import ca.kidstart.kidstart.model.FeaturedSlide;

public class FeaturedSliderAdapter extends RecyclerView.Adapter<FeaturedSliderAdapter.SliderViewHolder> {

    private final List<FeaturedSlide> slideList;

    public FeaturedSliderAdapter(List<FeaturedSlide> slideList) {
        this.slideList = slideList;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feature_slide, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        FeaturedSlide item = slideList.get(position);
        holder.ivSlideImage.setImageResource(item.getImageResId());
        holder.tvSlideTag.setText(item.getTag());
        holder.tvSlideTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return slideList.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSlideImage;
        TextView tvSlideTag;
        TextView tvSlideTitle;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSlideImage = itemView.findViewById(R.id.ivSlideImage);
            tvSlideTag = itemView.findViewById(R.id.tvSlideTag);
            tvSlideTitle = itemView.findViewById(R.id.tvSlideTitle);
        }
    }
}