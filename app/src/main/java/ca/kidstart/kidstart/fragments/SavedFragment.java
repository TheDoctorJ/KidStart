package ca.kidstart.kidstart.fragments;

import static androidx.core.content.ContextCompat.startActivities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import ca.kidstart.kidstart.MainActivity;
import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.adapter.ActivityAdapter;
import ca.kidstart.kidstart.adapter.ChipAdapter;
import ca.kidstart.kidstart.model.ActivityFilter;
import ca.kidstart.kidstart.model.ActivityItem;
import ca.kidstart.kidstart.model.AllFilter;
import ca.kidstart.kidstart.model.ChipItem;
import ca.kidstart.kidstart.model.DistanceFilter;
import ca.kidstart.kidstart.model.FreeFilter;
import ca.kidstart.kidstart.model.InterestCategory;
import ca.kidstart.kidstart.model.InterestCategoryFilter;

public class SavedFragment extends Fragment {

    private final int[] DISTANCES_TO_FILTER = {10,20,40};

    private View fragmentView;
    LinkedList<ActivityItem> savedActivities;
    private RecyclerView activityItemsRecyclerView;
    private RecyclerView filterRecycler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_saved, container, false);

        activityItemsRecyclerView = fragmentView.findViewById(R.id.saved_activities_recycler);

        savedActivities = loadSavedActivities();
        setActivityItemsInRecycler(savedActivities);

        setupFilterChips();
        handleSyncCalendarButton();

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
                "17km",
                new GregorianCalendar(2026, 3, 14, 14, 0),
                new GregorianCalendar(2026, 3, 14, 16, 30)
        ));
        placeholderItems.add(new ActivityItem(R.drawable.sm_sample_1,
                MainActivity.interestCategories[InterestCategory.Categories.Art.ordinal()],
                "Title",
                "Location",
                "0-10",
                "$10",
                "4",
                "18km",
                new GregorianCalendar(2026, 4, 16, 10, 0),
                new GregorianCalendar(2026, 4, 17, 16, 30)
        ));

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
    private void setupFilterChips() {
        LinkedList<ChipItem> filterChipList = new LinkedList<ChipItem>();
        filterRecycler = fragmentView.findViewById(R.id.filter_recycler);

        filterChipList.add(new ChipItem("All" + " (" + savedActivities.size() + ")",
                true, new AllFilter())); // All filter is always visible;

        int filteredItemCount;

        InterestCategory[] interestCategories = MainActivity.interestCategories;
        InterestCategoryFilter icFilter;
        for (int i = 0; i < interestCategories.length; i++) {
            icFilter = new InterestCategoryFilter(interestCategories[i]);
            filteredItemCount = getFilteredItems(icFilter).size();
            if (filteredItemCount > 0)
                filterChipList.add(new ChipItem(icFilter.getName() + " (" + filteredItemCount + ")",
                        false, icFilter));
        }

        DistanceFilter distanceFilter;
        for (int i = 0; i < DISTANCES_TO_FILTER.length; i++) {
            distanceFilter = new DistanceFilter(DISTANCES_TO_FILTER[i]);
            filteredItemCount = getFilteredItems(distanceFilter).size();
            if (filteredItemCount > 0
                    && getFilteredItems(filterChipList.peekLast().getFilter()).size() < filteredItemCount)
                filterChipList.add(new ChipItem(distanceFilter.getName() + " (" + filteredItemCount + ")",
                        false, distanceFilter));
        }

        filteredItemCount = getFilteredItems(new FreeFilter()).size();
        filterChipList.add(new ChipItem("Free" + " (" + filteredItemCount + ")",
                false, new FreeFilter())); // Free filter is always visible

        // Adapter stuff
        ChipAdapter adapter = new ChipAdapter(requireContext(), filterChipList, position -> {
            RecyclerView.Adapter<?> recyclerAdapter = filterRecycler.getAdapter();
            if (recyclerAdapter instanceof ChipAdapter) {
                ((ChipAdapter) recyclerAdapter).setSelectedPosition(position);
                setActivityItemsInRecycler(getFilteredItems(filterChipList.get(position).getFilter()));
            }
        });

        filterRecycler.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        filterRecycler.setAdapter(adapter);
    }

    private LinkedList<ActivityItem> getFilteredItems(ActivityFilter filter) {
        LinkedList<ActivityItem> result = new LinkedList<ActivityItem>();
        for (int i = 0; i < savedActivities.size(); i++) {
            if (filter.isIncluded(savedActivities.get(i)))
                result.add(savedActivities.get(i));
        }
        return result;
    }

    private void handleSyncCalendarButton() {
        MaterialButton button = fragmentView.findViewById(R.id.calendar_sync);

        button.setOnClickListener(v -> {
            Intent[] intentArray = new Intent[savedActivities.size()];
            ActivityItem currentActivity;
            for (int i = 0; i < intentArray.length; i++) {
                currentActivity = savedActivities.get(i);
                intentArray[i] = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, currentActivity.getStartTime().getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, currentActivity.getEndTime().getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, currentActivity.getTitle())
                        //.putExtra(CalendarContract.Events.DESCRIPTION, currentActivity.get)
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, currentActivity.getLocation())
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
            }
            startActivities(getContext(), intentArray);
        });
    }


//    /**
//     * Instance one filter button.
//     * @param filter for the button.
//     */
//    private ChipItem newFilterButton(ActivityFilter filter) {
//        ChipItem chip = new ChipItem(filter);
//        LinkedList<ActivityItem> filteredItems = getFilteredItems(filter);
//
//        // Do not make a filter button if there are not items to be filtered by the button.
//        if (filteredItems.isEmpty())
//            return null;
//
//        /*
//        button.setLayoutParams(
//                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//        button.setText(filter.getName() + " (" + filteredItems.size() + ")");
//
//        button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.surface));
//        button.setStrokeColorResource(R.color.stroke);
//        button.setTextColor(ContextCompat.getColor(getContext(), R.color.text_primary));
//         */
////        chip.setOnClickListener(v -> {
////            setSelectedButton(button);
////        });
//
//        filterRecycler.addView(button);
//        return button;
//    }

}