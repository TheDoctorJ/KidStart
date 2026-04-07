package ca.kidstart.kidstart;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment {

    private View fragmentView;
    private MaterialButton addInterestButton;
    private InterestCategory[] interestCategories;
    private boolean[] selectedInterests;
    private LinearLayout accountManagementButton, notificationsButton, helpSupportButton, logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Load saved stuff
        loadProfilePreferences();
        loadInterestedCategories();

        // Add interest button
        addInterestButton = fragmentView.findViewById(R.id.add_interest);
        addInterestButton.setOnClickListener(v -> { showAddInterestMenu(); });

        // Settings buttons
        accountManagementButton = fragmentView.findViewById(R.id.account_management_layout);
        notificationsButton = fragmentView.findViewById(R.id.notifications_layout);
        helpSupportButton = fragmentView.findViewById(R.id.help_and_support_layout);
        logoutButton = fragmentView.findViewById(R.id.logout_layout);

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

        return fragmentView;
    }

    /**
     * Loads the age and distance preference into the page.
     * To do: Load saved profile preference. Currently this method just sets the default.
     */
    private void loadProfilePreferences() {
        AutoCompleteTextView ageAutoComplete = fragmentView.findViewById(R.id.age_autocomplete);
        AutoCompleteTextView distanceAutoComplete = fragmentView.findViewById(R.id.distance_autocomplete);
        // if (profilePreferences is null) => set default
        ageAutoComplete.setText(getResources().getStringArray(R.array.age_array)[0], false);
        distanceAutoComplete.setText(getResources().getStringArray(R.array.distance_array)[0], false);
        //else => load profilePreferences
    }

    /**
     * Saves the user's changes when changing their profile preferences.
     * To do: The actual saving of data.
     */
    private void setOnChangedProfilePreferences() {
        TextInputLayout ageDropdown = fragmentView.findViewById(R.id.age_dropdown);
        TextInputLayout distanceDropdown = fragmentView.findViewById(R.id.distance_dropdown);

        ageDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save
            }
        });

        distanceDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save
            }
        });
    }

    /**
     * Loads the interested categories into the page.
     * To do: Load saved categories.
     */
    private void loadInterestedCategories() {
        interestCategories = new InterestCategory[]{
                new InterestCategory(ResourcesCompat.getDrawable(getResources(), R.drawable.science_interest, getContext().getTheme()),
                        getString(R.string.science_interest_title), getString(R.string.science_interest_description)),
                new InterestCategory(ResourcesCompat.getDrawable(getResources(), R.drawable.science_interest, getContext().getTheme()),
                        getString(R.string.computers_interest_title), getString(R.string.computers_interest_description)),
                new InterestCategory(ResourcesCompat.getDrawable(getResources(), R.drawable.science_interest, getContext().getTheme()),
                        getString(R.string.art_interest_title), getString(R.string.art_interest_description)),
                new InterestCategory(ResourcesCompat.getDrawable(getResources(), R.drawable.science_interest, getContext().getTheme()),
                        getString(R.string.music_interest_title), getString(R.string.music_interest_description)),
                new InterestCategory(ResourcesCompat.getDrawable(getResources(), R.drawable.science_interest, getContext().getTheme()),
                        getString(R.string.sports_interest_title), getString(R.string.sports_interest_description)),
                new InterestCategory(ResourcesCompat.getDrawable(getResources(), R.drawable.science_interest, getContext().getTheme()),
                        getString(R.string.reading_interest_title), getString(R.string.reading_interest_description))
        };

        // if (savedCategories == null) => set default
        selectedInterests = new boolean[interestCategories.length];
        for (int i = 0; i < selectedInterests.length; i++) selectedInterests[i] = false;
        // else => load saved categories
    }

    /**
     * Shows the popup menu for the user to add interested categories.
     */
    private void showAddInterestMenu() {

        PopupMenu popup = new PopupMenu(getContext(), addInterestButton);
        int[] menuItemIds = new int[interestCategories.length];

        // Create a correspondence between interests and menu item ids.
        for (int i = 0; i < interestCategories.length; i++) {
            if (!selectedInterests[i])
                menuItemIds[i] = popup.getMenu().add(0, i, 0, interestCategories[i].getName()).getItemId();
            else
                menuItemIds[i] = -1;
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // find corresponding interest from item id.
                for (int i = 0; i < menuItemIds.length; i++) {
                    if (menuItemIds[i] == item.getItemId()) {
                        addNewInterest(interestCategories[i]);
                        selectedInterests[i] = true;
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

        fragmentTransaction.commitNow();

        // Remove interest button.
        interestCategoryFragment.getView().findViewById(R.id.remove_interest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.remove(interestCategoryFragment);
                container.removeView(frame);
                // Allow interest to be selected again.
                for (int i = 0; i < interestCategories.length; i++) {
                    if (interestCategories[i] == interest)
                        selectedInterests[i] = false;
                }
            }
        });
    }
}