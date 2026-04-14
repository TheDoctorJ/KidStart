package ca.kidstart.kidstart.model;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ca.kidstart.kidstart.MainActivity;
import ca.kidstart.kidstart.adapter.ActivityAdapter;
import ca.kidstart.kidstart.adapter.ChipAdapter;

public class FilterBuilder {

    private final int[] DISTANCES_TO_FILTER = {10,20,40};

    private Context context;
    private List<ActivityItem> activities;
    private LinkedList<ActivityFilter> filters;
    private LinkedList<Integer> filtersCount;
    private RecyclerView chipsContainer, activitiesContainer;

    public FilterBuilder(List<ActivityItem> activities, Context context,
                         RecyclerView chipsContainer, RecyclerView activitiesContainer) {
        this.activities = activities;
        this.context = context;
        this.chipsContainer = chipsContainer;
        this.activitiesContainer = activitiesContainer;
        buildFilters();
    }

    /**
     * Creates a set of filters. Excludes any redundant or empty filters.
     */
    private void buildFilters() {
        filters = new LinkedList<>();
        filtersCount = new LinkedList<>();
        int filteredItemCount;

        filters.add(new AllFilter());
        filtersCount.add(activities.size());

        InterestCategory[] interestCategories = MainActivity.interestCategories;
        InterestCategoryFilter icFilter;
        for (int i = 0; i < interestCategories.length; i++) {
            icFilter = new InterestCategoryFilter(interestCategories[i]);
            filteredItemCount = getFilteredItems(icFilter).size();
            if (filteredItemCount > 0) {
                filters.add(icFilter);
                filtersCount.add(filteredItemCount);
            }
        }

        DistanceFilter distanceFilter;
        for (int i = 0; i < DISTANCES_TO_FILTER.length; i++) {
            distanceFilter = new DistanceFilter(DISTANCES_TO_FILTER[i]);
            filteredItemCount = getFilteredItems(distanceFilter).size();
            if (filteredItemCount > 0
                    && getFilteredItems(filters.getLast()).size() < filteredItemCount) {
                filters.add(distanceFilter);
                filtersCount.add(filteredItemCount);
            }
        }

        filters.add(new FreeFilter());
        filtersCount.add(getFilteredItems(filters.getLast()).size());
    }

    public void setUpFilterChips() {
        LinkedList<ChipItem> filterChipList = new LinkedList<ChipItem>();

        ActivityFilter current;
        for (int i = 0; i < filters.size(); i++) {
            current = filters.get(i);
            filterChipList.add(new ChipItem(
                    current.getName() + " (" + filtersCount.get(i) + ")",
                    false, current
            ));
        }

        // Adapter stuff
        ChipAdapter adapter = new ChipAdapter(context, filterChipList, position -> {
            RecyclerView.Adapter<?> recyclerAdapter = chipsContainer.getAdapter();
            if (recyclerAdapter instanceof ChipAdapter) {
                ((ChipAdapter) recyclerAdapter).setSelectedPosition(position);
                filtersActivities(filters.get(position));
            }
        });

        chipsContainer.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        );
        chipsContainer.setAdapter(adapter);

        // Initialize as all filter
        adapter.setSelectedPosition(0);
        filtersActivities(filters.get(0));
    }

    private LinkedList<ActivityItem> getFilteredItems(ActivityFilter filter) {
        LinkedList<ActivityItem> result = new LinkedList<ActivityItem>();
        ActivityItem current;
        for (int i = 0; i < activities.size(); i++) {
            current = activities.get(i);
            if (filter.isIncluded(current))
                result.add(current);
        }
        return result;
    }

    /**
     * Display filtered activities in their container.
     * @param filter for which activities will be displayed.
     */
    private void filtersActivities(ActivityFilter filter) {
        if (activitiesContainer.getChildCount() > 0)
            activitiesContainer.removeAllViews();

        activitiesContainer.setLayoutManager(new LinearLayoutManager(context));
        activitiesContainer.setAdapter(new ActivityAdapter(getFilteredItems(filter)));
    }
}
