package ca.kidstart.kidstart.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "KidStartSession";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_PASSWORD = "userPassword";
    private static final String KEY_CHILD_AGE = "childAge";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void saveUserInfo(String name, String email, String password, String childAge) {
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PASSWORD, password);
        editor.putString(KEY_CHILD_AGE, childAge);
        editor.apply();
    }

    public String getUserName() {
        return preferences.getString(KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return preferences.getString(KEY_USER_EMAIL, "");
    }

    public String getUserPassword() {
        return preferences.getString(KEY_USER_PASSWORD, "");
    }

    public String getChildAge() {
        return preferences.getString(KEY_CHILD_AGE, "");
    }

    public boolean hasUser() {
        return !getUserEmail().isEmpty();
    }

    public boolean checkLogin(String email, String password) {
        return email.equals(getUserEmail()) && password.equals(getUserPassword());
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}