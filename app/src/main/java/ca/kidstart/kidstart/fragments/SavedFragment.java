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
import ca.kidstart.kidstart.model.FilterBuilder;
import ca.kidstart.kidstart.model.FreeFilter;
import ca.kidstart.kidstart.model.InterestCategory;
import ca.kidstart.kidstart.model.InterestCategoryFilter;

public class SavedFragment extends Fragment {

    private View fragmentView;
    LinkedList<ActivityItem> savedActivities;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_saved, container, false);

        savedActivities = loadSavedActivities();
        new FilterBuilder(savedActivities,
                getContext(),
                fragmentView.findViewById(R.id.filter_recycler),
                fragmentView.findViewById(R.id.saved_activities_recycler))
                .setUpFilterChips();

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
}