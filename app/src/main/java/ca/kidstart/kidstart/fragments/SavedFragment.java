package ca.kidstart.kidstart.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonGroup;
import com.google.android.material.textview.MaterialTextView;

import java.util.LinkedList;
import java.util.concurrent.ThreadPoolExecutor;

import ca.kidstart.kidstart.MainActivity;
import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.adapter.ActivityAdapter;
import ca.kidstart.kidstart.model.ActivityFilter;
import ca.kidstart.kidstart.model.ActivityItem;
import ca.kidstart.kidstart.model.DistanceFilter;
import ca.kidstart.kidstart.model.FreeFilter;
import ca.kidstart.kidstart.model.InterestCategory;
import ca.kidstart.kidstart.model.InterestCategoryFilter;

public class SavedFragment extends Fragment {

    private final int[] DISTANCES_TO_FILTER = {10,20,40};

    private View fragmentView;
    LinkedList<ActivityItem> savedActivities;
    private RecyclerView activityItemsRecyclerView;
    private MaterialButtonGroup filterButtonGroup;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_saved, container, false);

        activityItemsRecyclerView = fragmentView.findViewById(R.id.saved_activities_recycler);

        savedActivities = loadSavedActivities();
        setActivityItemsInRecycler(savedActivities);

        createFilterButtons();

        return fragmentView;
    }

    /**
     * Load saved activities for this user. Currently just loads placeholder activities.
     * @return A linked list of ActivityItems that the user has saved.
     */
    private LinkedList<ActivityItem> loadSavedActivities() {
        LinkedList<ActivityItem> placeholderItems = new LinkedList<ActivityItem>();
        placeholderItems.add(new ActivityItem(R.drawable.sample_1,
                MainActivity.interestCategories[InterestCategory.Categories.Science.ordinal()],
                "Title",
                "Location",
                "0-10",
                "$10",
                "4",
                "10km"));
        placeholderItems.add(new ActivityItem(R.drawable.sm_sample_1,
                MainActivity.interestCategories[InterestCategory.Categories.Art.ordinal()],
                "Title",
                "Location",
                "0-10",
                "$10",
                "4",
                "10km"));

        MaterialTextView activitiesFoundHeader = fragmentView.findViewById(R.id.activities_found_header);
        activitiesFoundHeader.setText(placeholderItems.size() + " " + getString(R.string.activities_found));

        return placeholderItems;
    }

    /**
     * Given some ActivityItems, display them in the recycler view.
     * @param items to be put in recycler view.
     */
    private void setActivityItemsInRecycler(LinkedList<ActivityItem> items) {
        if (activityItemsRecyclerView.getChildCount() > 0)
            activityItemsRecyclerView.removeAllViews();

        activityItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        activityItemsRecyclerView.setAdapter(new ActivityAdapter(items));
    }

    /**
     * Prepare filter buttons.
     */
    private void createFilterButtons() {
        filterButtonGroup = fragmentView.findViewById(R.id.filter_button_group);

        InterestCategory[] interestCategories = MainActivity.interestCategories;
        for (int i = 0; i < interestCategories.length; i++) {
            newFilterButton(new InterestCategoryFilter(interestCategories[i]));
        }

        for (int i = 0; i < DISTANCES_TO_FILTER.length; i++) {
            newFilterButton(new DistanceFilter(DISTANCES_TO_FILTER[i]));
        }

        newFilterButton(new FreeFilter());
    }

    /**
     * Instance one filter button.
     * @param filter for the button.
     */
    private void newFilterButton(ActivityFilter filter) {
        MaterialButton button = new MaterialButton(getContext());
        LinkedList<ActivityItem> filteredItems = getFilteredItems(filter);

        // Do not make a filter button if there are not items to be filtered.
        if (filteredItems.isEmpty())
            return;

        button.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText(filter.getName());

        button.setOnClickListener(v -> {
            setActivityItemsInRecycler(filteredItems);
        });

        filterButtonGroup.addView(button);
    }

    private LinkedList<ActivityItem> getFilteredItems(ActivityFilter filter) {
        LinkedList<ActivityItem> result = new LinkedList<ActivityItem>();
        for (int i = 0; i < savedActivities.size(); i++) {
            if (filter.isIncluded(savedActivities.get(i)))
                result.add(savedActivities.get(i));
        }
        return result;
    }
}