package ca.kidstart.kidstart.data;

import java.util.ArrayList;
import java.util.List;

import ca.kidstart.kidstart.model.ActivityItem;

public class SavedItemsManager {

    private static final List<ActivityItem> savedItems = new ArrayList<>();

    public static boolean isSaved(ActivityItem item) {
        return savedItems.contains(item);
    }

    public static void toggleSaved(ActivityItem item) {
        if (savedItems.contains(item)) {
            savedItems.remove(item);
        } else {
            savedItems.add(item);
        }
    }

    public static List<ActivityItem> getSavedItems() {
        return new ArrayList<>(savedItems);
    }
}