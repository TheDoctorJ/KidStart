package ca.kidstart.kidstart.data;

import java.util.ArrayList;
import java.util.List;

import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.model.ActivityItem;

public class ActivityDataProvider {

    public static List<ActivityItem> getAllActivities() {
        List<ActivityItem> items = new ArrayList<>();

        items.add(new ActivityItem(
                R.drawable.sm_sample_1,
                "SCIENCE",
                "Junior Scientists",
                "Downtown Science Center",
                "7-12 yrs",
                "$250/wk",
                "4.8",
                "2.3 mi"
        ));

        items.add(new ActivityItem(
                R.drawable.sm_sample_1,
                "DAYCARE",
                "Little Learners Daycare",
                "Maple Street Studio",
                "0-3 yrs",
                "$1,200/mo",
                "4.9",
                "1.5 mi"
        ));

        items.add(new ActivityItem(
                R.drawable.sm_sample_1,
                "EDUCATION",
                "Storytime at the Library",
                "Central Public Library",
                "3-6 yrs",
                "Free",
                "4.7",
                "2.0 mi"
        ));

        items.add(new ActivityItem(
                R.drawable.sm_sample_1,
                "TECHNOLOGY",
                "Robotics Workshop",
                "Innovation Hub",
                "10-14 yrs",
                "$45",
                "5.0",
                "3.1 mi"
        ));

        items.add(new ActivityItem(
                R.drawable.sm_sample_1,
                "SPORTS",
                "Soccer Stars Camp",
                "Westside Field House",
                "7-12 yrs",
                "$120/wk",
                "4.6",
                "4.2 mi"
        ));

        items.add(new ActivityItem(
                R.drawable.sm_sample_1,
                "ARTS",
                "Creative Kids Studio",
                "Downtown Arts Center",
                "3-6 yrs",
                "$90",
                "4.5",
                "1.8 mi"
        ));

        return items;
    }

    public static List<ActivityItem> getTrendingActivities() {
        return new ArrayList<>(getAllActivities());
    }

    public static List<ActivityItem> getHappeningSoonActivities() {
        List<ActivityItem> all = getAllActivities();
        List<ActivityItem> result = new ArrayList<>();

        for (int i = 0; i < Math.min(3, all.size()); i++) {
            result.add(all.get(i));
        }

        return result;
    }
}