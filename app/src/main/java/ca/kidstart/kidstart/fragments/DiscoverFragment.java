package ca.kidstart.kidstart.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.adapter.ActivityAdapter;
import ca.kidstart.kidstart.adapter.FeaturedSliderAdapter;
import ca.kidstart.kidstart.adapter.HorizontalActivityAdapter;
import ca.kidstart.kidstart.data.ActivityDataProvider;
import ca.kidstart.kidstart.model.ActivityItem;
import ca.kidstart.kidstart.model.FeaturedSlide;

public class DiscoverFragment extends Fragment {

    private ViewPager2 viewPagerFeatured;
    private RecyclerView recyclerHappeningSoon;
    private RecyclerView recyclerTrending;

    private HorizontalActivityAdapter happeningSoonAdapter;
    private ActivityAdapter trendingAdapter;

    private final Handler sliderHandler = new Handler(Looper.getMainLooper());

    public DiscoverFragment() {
        super(R.layout.fragment_discover);
    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewPagerFeatured != null && viewPagerFeatured.getAdapter() != null) {
                int count = viewPagerFeatured.getAdapter().getItemCount();
                if (count > 1) {
                    int next = (viewPagerFeatured.getCurrentItem() + 1) % count;
                    viewPagerFeatured.setCurrentItem(next, true);
                    sliderHandler.postDelayed(this, 3000);
                }
            }
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPagerFeatured = view.findViewById(R.id.viewPagerFeatured);
        recyclerHappeningSoon = view.findViewById(R.id.recyclerHappeningSoon);
        recyclerTrending = view.findViewById(R.id.recyclerTrending);

        setupFeaturedSlider();
        setupHappeningSoon();
        setupTrending();
    }

    private void setupFeaturedSlider() {
        List<FeaturedSlide> slideList = new ArrayList<>();
        slideList.add(new FeaturedSlide(R.drawable.kids_sports, "FEATURED", "Weekend Sports for Kids"));
        slideList.add(new FeaturedSlide(R.drawable.kids_science, "POPULAR", "Top STEM Programs"));
        slideList.add(new FeaturedSlide(R.drawable.kids_art, "NEW", "Creative Classes Near You"));

        FeaturedSliderAdapter adapter = new FeaturedSliderAdapter(slideList);
        viewPagerFeatured.setAdapter(adapter);
        viewPagerFeatured.setOffscreenPageLimit(3);
        viewPagerFeatured.setClipToPadding(false);
        viewPagerFeatured.setClipChildren(false);
        viewPagerFeatured.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer((page, position) -> {
            float scale = 0.9f + (1 - Math.abs(position)) * 0.1f;
            page.setScaleY(scale);
            page.setAlpha(0.7f + (1 - Math.abs(position)) * 0.3f);
        });
        viewPagerFeatured.setPageTransformer(transformer);
    }

    private void setupHappeningSoon() {
        List<ActivityItem> items = ActivityDataProvider.getHappeningSoonActivities();

        happeningSoonAdapter = new HorizontalActivityAdapter(
                items,
                item -> refreshFavoriteIcons()
        );

        recyclerHappeningSoon.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerHappeningSoon.setAdapter(happeningSoonAdapter);
    }

    private void setupTrending() {
        List<ActivityItem> items = ActivityDataProvider.getTrendingActivities();

        trendingAdapter = new ActivityAdapter(
                items,
                item -> refreshFavoriteIcons()
        );

        recyclerTrending.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerTrending.setAdapter(trendingAdapter);
    }

    private void refreshFavoriteIcons() {
        if (trendingAdapter != null) {
            trendingAdapter.notifyDataSetChanged();
        }

        if (happeningSoonAdapter != null) {
            happeningSoonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
        refreshFavoriteIcons();
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}