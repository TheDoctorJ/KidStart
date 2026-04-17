package ca.kidstart.kidstart.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ca.kidstart.kidstart.MainActivity;
import kotlin.collections.builders.SetBuilder;

public class PreferencesHandler {
    public static final String UNDEFINED_STRING = "undefined";
    private static PreferencesHandler instance;
    private final String FILE_NAME = "user_preferences";
    private final String SAVED_ACTIVITIES_ID_STRING_SET = "saved_activities";
    private final String AGE_PREFERENCE = "age_preference";
    private final String DISTANCE_PREFERENCE = "distance_preference";
    private final SharedPreferences sharedPreferences;

    private PreferencesHandler(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
    }

    public static PreferencesHandler getInstance(Context context) {
        if (instance == null)
            instance = new PreferencesHandler(context);
        return instance;
    }

    public List<InterestCategory> getInterestedCategories() {
        InterestCategory[] interestCategories = MainActivity.interestCategories;
        LinkedList<InterestCategory> result = new LinkedList<>();
        for (int i = 0; i < interestCategories.length; i++) {
            if (sharedPreferences.contains(interestCategories[i].getName()))
                result.add(interestCategories[i]);
        }
        return result;
    }

    public List<ActivityItem> getSavedActivities() {
        Set<String> idSet = sharedPreferences.getStringSet(SAVED_ACTIVITIES_ID_STRING_SET, new SetBuilder<String>(0).build());
        String[] ids = new String[idSet.size()];
        idSet.toArray(ids);

        List<ActivityItem> result = new LinkedList<>();
        ActivityItem current;
        for (int i = 0; i < ids.length; i++) {
            current = ActivityManager.getInstance().getActivity(ids[i]);
            if (current == null);
                // idk do something maybe
            else
                result.add(current);
        }

        return result;
    }

    public String getAgePreference() {
        return sharedPreferences.getString(AGE_PREFERENCE,UNDEFINED_STRING);
    }

    public String getDistancePreference() {
        return sharedPreferences.getString(DISTANCE_PREFERENCE,UNDEFINED_STRING);
    }

    public void setInterestedCategories(List<InterestCategory> interestCategories) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear all old interestedCategories
        for (int i = 0; i < MainActivity.interestCategories.length; i++) {
            if (sharedPreferences.contains(MainActivity.interestCategories[i].getName())) {
                editor.remove(MainActivity.interestCategories[i].getName());
            }
        }

        // Set new ones
        for (int i = 0; i < interestCategories.size(); i++) {
            // name:categoryOrdinal
            editor.putInt(interestCategories.get(i).getName(), interestCategories.get(i).getCategory().ordinal());
        }

        editor.apply();
    }

    public void setSavedActivities(List<ActivityItem> savedActivities) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(SAVED_ACTIVITIES_ID_STRING_SET);

        SetBuilder<String> stringSetBuilder = new SetBuilder<>(savedActivities.size());
        for (int i = 0; i < savedActivities.size(); i++) {
            stringSetBuilder.add(ActivityManager.getId(savedActivities.get(i)));
        }

        editor.putStringSet(SAVED_ACTIVITIES_ID_STRING_SET, stringSetBuilder.build());

        editor.apply();
    }

    public void setAgePreference(String age) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AGE_PREFERENCE, age);
        editor.apply();
    }

    public void setDistancePreference(String distance) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DISTANCE_PREFERENCE, distance);
        editor.apply();
    }

}
