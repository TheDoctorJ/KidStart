package ca.kidstart.kidstart.data;

import java.util.ArrayList;
import java.util.List;

import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.model.ActivityItem;

public class ActivityDataProvider {

    public static List<ActivityItem> getAllActivities() {
        List<ActivityItem> items = new ArrayList<>();

        items.add(new ActivityItem(
                R.drawable.kids_science,
                "SCIENCE",
                "Junior Scientists",
                "Downtown Science Center",
                "7-12 yrs",
                "$250/wk",
                "4.8",
                "2.3 mi",
                "A fun hands-on science class where children explore simple experiments, creativity, and teamwork in a safe learning environment."
        ));

        items.add(new ActivityItem(
                R.drawable.kids_art,
                "ARTS",
                "Creative Painting Club",
                "West End Art Room",
                "3-6 yrs",
                "$20/mo",
                "4.9",
                "1.7 mi",
                "A fun hands-on science class where children explore simple experiments, creativity, and teamwork in a safe learning environment."
        ));

        items.add(new ActivityItem(
                R.drawable.kids_library,
                "EDUCATION",
                "Storytime at the Library",
                "Central Public Library",
                "3-6 yrs",
                "Free",
                "4.7",
                "2.0 mi",
                "A fun hands-on science class where children explore simple experiments, creativity, and teamwork in a safe learning environment."
        ));

        items.add(new ActivityItem(
                R.drawable.kids_robotics,
                "TECHNOLOGY",
                "Robotics Workshop",
                "Innovation Hub",
                "10-14 yrs",
                "$45",
                "5.0",
                "3.1 mi",
                "A fun hands-on science class where children explore simple experiments, creativity, and teamwork in a safe learning environment."
        ));

        items.add(new ActivityItem(
                R.drawable.kids_sports,
                "SPORTS",
                "Kids Soccer Clinic",
                "Northside Recreation Centre",
                "7-12 yrs",
                "$60/wk",
                "4.8",
                "2.9 mi",
                "A fun hands-on science class where children explore simple experiments, creativity, and teamwork in a safe learning environment."
        ));


        items.add(new ActivityItem(
                R.drawable.kids_sport2,
                "SPORTS",
                "Kids Basketball Clinic",
                "Kamloops Riverside Park",
                "7-12 yrs",
                "$310/wk",
                "4.9",
                "1.9 mi",
                "A fun hands-on science class where children explore simple experiments, creativity, and teamwork in a safe learning environment."
        ));


        items.add(new ActivityItem(
                R.drawable.kids_edu,
                "EDUCATION",
                "National Geography Kids",
                "Thomson Valley Kids",
                "10-14 yrs",
                "$330/wk",
                "5.0",
                "4.1 mi",
                "A fun hands-on science class where children explore simple experiments, creativity, and teamwork in a safe learning environment."
        ));


        return items;
    }

    public static List<ActivityItem> getTrendingActivities() {
        List<ActivityItem> all = getAllActivities();
        List<ActivityItem> result = new ArrayList<>();

        for (int i = 0; i < Math.min(5, all.size()); i++) {
            result.add(all.get(i));
        }

        return result;
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