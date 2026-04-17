package ca.kidstart.kidstart.fragments;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContentProviderCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import ca.kidstart.kidstart.MainActivity;
import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.model.InterestCategory;
import ca.kidstart.kidstart.model.PreferencesHandler;

public class ProfileFragment extends Fragment {

    private final int MAX_USERNAME_LENGTH = 30;

    private View fragmentView;
    private MaterialButton addInterestButton;
    private List<InterestCategory> selectedInterests;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    // Edit profile stuff
    private MaterialButton editProfileButton, applyEditProfileButton;
    private CircularRevealCardView profileSummaryCard, editProfileCard;
    private MaterialTextView profileName;
    private TextInputEditText profileNameEdit;
    private ShapeableImageView avatar, avatarEdit;
    private String committedAvatarPath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        handleEdits();

        // Load saved stuff
        loadProfilePreferences();
        loadInterestedCategories();
        loadProfile();

        // Setup all views.
        handleProfilePreferencesSelections();
        addInterestButton = fragmentView.findViewById(R.id.add_interest);
        addInterestButton.setOnClickListener(v -> { showAddInterestMenu(); });
        handleSettingsAndLogoutButtons();

        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Don't touch I have no clue how this works. It's for the image selector.
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        committedAvatarPath = uri.getPath();
                        avatarEdit.setImageDrawable(getAvatarFromPath(committedAvatarPath));
                    }
                    else {
                        //
                    }
                });
    }

    /**
     * Loads the age and distance preference into the page.
     * To do: Load saved profile preference. Currently this method just sets the default.
     */
    private void loadProfilePreferences() {
        AutoCompleteTextView ageAutoComplete = fragmentView.findViewById(R.id.age_autocomplete);
        AutoCompleteTextView distanceAutoComplete = fragmentView.findViewById(R.id.distance_autocomplete);
        if (PreferencesHandler.getInstance(getContext()).getAgePreference().equals(PreferencesHandler.UNDEFINED_STRING)) {
            ageAutoComplete.setText(getResources().getStringArray(R.array.age_array)[0], false);
        }
        else {
            ageAutoComplete.setText(PreferencesHandler.getInstance(getContext()).getAgePreference(), false);
        }
        if (PreferencesHandler.getInstance(getContext()).getDistancePreference().equals(PreferencesHandler.UNDEFINED_STRING)) {
            distanceAutoComplete.setText(getResources().getStringArray(R.array.distance_array)[0], false);
        }
        else {
            distanceAutoComplete.setText(PreferencesHandler.getInstance(getContext()).getDistancePreference(), false);
        }
    }

    /**
     * Loads interested categories.
     */
    private void loadInterestedCategories() {
        selectedInterests = PreferencesHandler.getInstance(getContext()).getInterestedCategories();
        for (int i = 0; i < selectedInterests.size(); i++) {
            addNewInterest(selectedInterests.get(i));
        }
    }

    private void loadProfile() {
        String name = PreferencesHandler.getInstance(getContext()).getProfileName();
        String drawablepath = PreferencesHandler.getInstance(getContext()).getAvatarDrawablePath();
        if (name.equals(PreferencesHandler.UNDEFINED_STRING)) {
            profileName.setText(R.string.placeholder_profile_name);
            profileNameEdit.setText(R.string.placeholder_profile_name);
        }
        else {
            profileName.setText(name);
            profileNameEdit.setText(name);
        }


        if (drawablepath.equals(PreferencesHandler.UNDEFINED_STRING)) {
            avatar.setImageDrawable(getResources().getDrawable(R.drawable.bg_chip, getActivity().getTheme()));
            avatarEdit.setImageDrawable(getResources().getDrawable(R.drawable.bg_chip, getActivity().getTheme()));
        }
        else {
            Drawable avatarDrawable = getAvatarFromPath(drawablepath);
            avatar.setImageDrawable(avatarDrawable);
            avatarEdit.setImageDrawable(avatarDrawable);
        }
    }

    /**
     * Saves the user's changes when changing their profile preferences.
     */
    private void handleProfilePreferencesSelections() {
        AutoCompleteTextView ageAutocomplete = fragmentView.findViewById(R.id.age_autocomplete);
        AutoCompleteTextView distanceAutocomplete = fragmentView.findViewById(R.id.distance_autocomplete);

        ageAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PreferencesHandler.getInstance(getContext()).setAgePreference(
                        getResources().getStringArray(R.array.age_array)[position]
                );
            }
        });

        distanceAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PreferencesHandler.getInstance(getContext()).setDistancePreference(
                        getResources().getStringArray(R.array.distance_array)[position]);
            }
        });
    }

    /**
     * Shows the popup menu for the user to add interested categories.
     */
    private void showAddInterestMenu() {

        PopupMenu popup = new PopupMenu(getContext(), addInterestButton);
        int[] menuItemIds = new int[MainActivity.interestCategories.length];

        // Create a correspondence between interests and menu item ids.
        for (int i = 0; i < MainActivity.interestCategories.length; i++) {
            if (!selectedInterests.contains(MainActivity.interestCategories[i]))
                menuItemIds[i] = popup.getMenu().add(0, i, 0, MainActivity.interestCategories[i].getName()).getItemId();
            else
                menuItemIds[i] = -1;
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // find corresponding interest from item id.
                for (int i = 0; i < menuItemIds.length; i++) {
                    if (menuItemIds[i] == item.getItemId()) {
                        addNewInterest(MainActivity.interestCategories[i]);
                        selectedInterests.add(MainActivity.interestCategories[i]);
                        PreferencesHandler.getInstance(getContext()).setInterestedCategories(selectedInterests);
                        return true;
                    }
                }
                return false;
            }
        });

        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });

        popup.show();
    }

    /**
     * Add new interest to the profile.
     * To do: save added interests
     */
    private void addNewInterest(InterestCategory interest) {
        // Add new frame to layout
        LinearLayout container = fragmentView.findViewById(R.id.interests_container);

        FrameLayout frame = new FrameLayout(getContext());
        frame.setId(View.generateViewId());
        frame.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        container.addView(frame);

        // Add fragment to frame
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        InterestCategoryFragment interestCategoryFragment = new InterestCategoryFragment(interest);
        fragmentTransaction.add(frame.getId(), interestCategoryFragment);

        fragmentTransaction.commit();
        fragmentTransaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                // Remove interest button.
                interestCategoryFragment.getView().findViewById(R.id.remove_interest).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentTransaction.remove(interestCategoryFragment);
                        container.removeView(frame);
                        // Allow interest to be selected again.
                        for (int i = 0; i < MainActivity.interestCategories.length; i++) {
                            if (MainActivity.interestCategories[i] == interest) {
                                selectedInterests.remove(MainActivity.interestCategories[i]);
                                PreferencesHandler.getInstance(getContext()).setInterestedCategories(selectedInterests);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * Gets setting and logout buttons and writes the onClickListener.
     */
    private void handleSettingsAndLogoutButtons() {
        // Settings buttons
        LinearLayout accountManagementButton = fragmentView.findViewById(R.id.account_management_layout);
        LinearLayout notificationsButton = fragmentView.findViewById(R.id.notifications_layout);
        LinearLayout helpSupportButton = fragmentView.findViewById(R.id.help_and_support_layout);
        LinearLayout logoutButton = fragmentView.findViewById(R.id.logout_layout);

        accountManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open account settings
            }
        });

        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open notification settings
            }
        });

        helpSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open help and support
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout stuff
            }
        });
    }

    /**
     * Shows the views to edit the profile and hides the regular profile summary.
     */
    private void openEditProfile() {
        profileSummaryCard.setVisibility(View.GONE);
        editProfileCard.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the regular profile summary and closes the edit mode. Saves any changes.
     * To do: Save changes
     */
    private void closeEditProfile() {
        profileSummaryCard.setVisibility(View.VISIBLE);
        editProfileCard.setVisibility(View.GONE);

        String newUserName = profileNameEdit.getText().toString();
        if (newUserName.isEmpty()) {
            Snackbar.make(fragmentView, getString(R.string.empty_username_snack), Snackbar.LENGTH_SHORT).show();
            profileNameEdit.setText(profileName.getText().toString());
        }
        else {
            profileName.setText(newUserName);
            PreferencesHandler.getInstance(getContext()).setProfileName(newUserName);
        }

        if (committedAvatarPath == null) {
            // Do nothing
        }
        else {
            avatar.setImageDrawable(getAvatarFromPath(committedAvatarPath));
            PreferencesHandler.getInstance(getContext()).setAvatarDrawablePath(committedAvatarPath);
            committedAvatarPath = "";
        }
    }

    /**
     * Gets all views for the profile and profile editing and writes listeners.
     */
    private void handleEdits() {
        profileSummaryCard = fragmentView.findViewById(R.id.profile_summary);
        editProfileCard = fragmentView.findViewById(R.id.profile_edit);
        profileName = fragmentView.findViewById(R.id.user_name);
        profileNameEdit = fragmentView.findViewById(R.id.user_name_edit);
        avatar = fragmentView.findViewById(R.id.user_avatar);
        avatarEdit = fragmentView.findViewById(R.id.user_avatar_edit);
        editProfileButton = fragmentView.findViewById(R.id.edit_profile);
        applyEditProfileButton = fragmentView.findViewById(R.id.apply_edit_profile);

        editProfileButton.setOnClickListener(v -> { openEditProfile(); });
        applyEditProfileButton.setOnClickListener(v -> { closeEditProfile(); });
        avatarEdit.setOnClickListener(v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));
    }


    private Drawable getAvatarFromPath(String path) {
        if (path.isEmpty()) {
            return null;
        }

//        Drawable result;
//        ContentResolver resolver = getContext().getContentResolver();
//
//        Uri uri = new Uri.Builder().path(path).build();
//        getContext().permission
//        getContext().grantUriPermission(getContext().getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        try {
//            InputStream stream = resolver.openInputStream(uri);
//            result = new BitmapDrawable(getResources(), stream);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        return result;

        return null;
    }
}