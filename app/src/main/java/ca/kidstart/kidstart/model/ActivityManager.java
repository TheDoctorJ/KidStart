package ca.kidstart.kidstart.model;

import java.util.Hashtable;

public class ActivityManager {
    private static ActivityManager instance;
    private Hashtable<String, ActivityItem> activityStore;

    private ActivityManager() {
        activityStore = new Hashtable<>();
    }

    public static ActivityManager getInstance() {
        if (instance == null)
            instance = new ActivityManager();
        return instance;
    }

    public void putActivity(ActivityItem activity) {
        activityStore.put(getId(activity), activity);
    }

    public ActivityItem getActivity(String id) {
        return activityStore.get(id);
    }

    public static String getId(ActivityItem activity) {
        // no conflicts please
        return activity.getTitle() + activity.getStartTime().toString() + activity.getEndTime().toString();
    }
}
