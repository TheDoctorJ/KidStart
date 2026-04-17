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
import ca.kidstart.kidstart.adapter.ChipAdapter;
import ca.kidstart.kidstart.adapter.FeaturedSliderAdapter;
import ca.kidstart.kidstart.adapter.HorizontalActivityAdapter;
import ca.kidstart.kidstart.model.ActivityItem;
import ca.kidstart.kidstart.model.ChipItem;
import ca.kidstart.kidstart.model.FeaturedSlide;

public class DiscoverFragment extends Fragment {

    private ViewPager2 viewPagerFeatured;
    private RecyclerView recyclerChips;
    private RecyclerView recyclerHappeningSoon;
    private RecyclerView recyclerTrending;

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
        recyclerChips = view.findViewById(R.id.recyclerChips);
        recyclerHappeningSoon = view.findViewById(R.id.recyclerHappeningSoon);
        recyclerTrending = view.findViewById(R.id.recyclerTrending);

        setupFeaturedSlider();
        setupChips();
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

    private void setupChips() {
        List<ChipItem> chipList = new ArrayList<>();
        chipList.add(new ChipItem("All", true));
        chipList.add(new ChipItem("Science", false));
        chipList.add(new ChipItem("Arts", false));
        chipList.add(new ChipItem("Sports", false));
        chipList.add(new ChipItem("Daycare", false));
        chipList.add(new ChipItem("Technology", false));

        ChipAdapter adapter = new ChipAdapter(requireContext(), chipList, position -> {
            RecyclerView.Adapter<?> recyclerAdapter = recyclerChips.getAdapter();
            if (recyclerAdapter instanceof ChipAdapter) {
                ((ChipAdapter) recyclerAdapter).setSelectedPosition(position);
            }
        });

        recyclerChips.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerChips.setAdapter(adapter);
    }

    private void setupHappeningSoon() {
        List<ActivityItem> items = new ArrayList<>();

        items.add(new ActivityItem(
                R.drawable.kids_science,
                "SCIENCE",
                "Junior Scientists Summer Camp",
                "Downtown Science Center",
                "7-12 yrs",
                "$250/wk",
                "4.8",
                "2.3 mi"
        ));

        items.add(new ActivityItem(
                R.drawable.kids_art,
                "ARTS",
                "Kids Creative Art Studio",
                "Maple Arts Centre",
                "5-10 yrs",
                "$30/class",
                "4.9",
                "1.8 mi"
        ));

        items.add(new ActivityItem(
                R.drawable.kids_sports,
                "SPORTS",
                "Beginner Soccer Camp",
                "Riverside Field",
                "6-11 yrs",
                "$95/program",
                "4.7",
                "2.6 mi"
        ));
        HorizontalActivityAdapter adapter = new HorizontalActivityAdapter(items);
        recyclerHappeningSoon.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerHappeningSoon.setAdapter(adapter);
    }

    private void setupTrending() {
        List<ActivityItem> items = new ArrayList<>();

        items.add(new ActivityItem(
                R.drawable.kids_science,
                "SCIENCE",
                "Junior Scientists",
                "Downtown Science Center",
                "7-12 yrs",
                "$250/wk",
                "4.8",
                "2.3 mi"
        ));

        items.add(new ActivityItem(
                R.drawable.kids_art,
                "ARTS",
                "Creative Painting Club",
                "West End Art Room",
                "6-10 yrs",
                "$20/class",
                "4.9",
                "1.7 mi"
        ));

        items.add(new ActivityItem(
                R.drawable.kids_library,
                "EDUCATION",
                "Storytime at the Library",
                "Central Public Library",
                "3-6 yrs",
                "Free",
                "4.7",
                "2.0 mi"
        ));

        items.add(new ActivityItem(
                R.drawable.kids_robotics,
                "TECHNOLOGY",
                "Robotics Workshop",
                "Innovation Hub",
                "10-14 yrs",
                "$45",
                "5.0",
                "3.1 mi"
        ));

        items.add(new ActivityItem(
                R.drawable.kids_sports,
                "SPORTS",
                "Kids Basketball Clinic",
                "Northside Recreation Centre",
                "8-13 yrs",
                "$60/program",
                "4.8",
                "2.9 mi"
        ));

        ActivityAdapter adapter = new ActivityAdapter(items);
        recyclerTrending.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerTrending.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}