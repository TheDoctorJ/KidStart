package ca.kidstart.kidstart.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonGroup;
import com.google.android.material.textview.MaterialTextView;

import java.util.LinkedList;

import ca.kidstart.kidstart.MainActivity;
import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.adapter.ActivityAdapter;
import ca.kidstart.kidstart.model.ActivityItem;
import ca.kidstart.kidstart.model.InterestCategory;

public class SavedFragment extends Fragment {

    private View fragmentView;
    private MaterialTextView activitiesFoundHeader;
    private RecyclerView activityItemsRecyclerView;
    private MaterialButtonGroup filtersButtonGroup;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_saved, container, false);

        activityItemsRecyclerView = fragmentView.findViewById(R.id.saved_activities_recycler);

        LinkedList<ActivityItem> savedActivities = loadSavedActivities();
        setActivityItemsInRecycler(savedActivities);

        handleFilterButtons();

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

        activitiesFoundHeader.setText(placeholderItems.size() + " " + getString(R.string.activities_found));
        return placeholderItems;
    }

    /**
     * Given some ActivityItems, display them in the recycler view.
     * @param items to be put in recycler view.
     */
    private void setActivityItemsInRecycler(LinkedList<ActivityItem> items) {
        activityItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        activityItemsRecyclerView.setAdapter(new ActivityAdapter(items));
    }

    /**
     * Prepare filter buttons and set click listeners.
     */
    private void handleFilterButtons() {

    }
}