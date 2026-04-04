package ca.kidstart.kidstart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.PopupMenu;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment {

    private View fragmentView;
    private MaterialButton addInterestButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        loadProfilePreferences();

        addInterestButton = fragmentView.findViewById(R.id.add_interest);
        addInterestButton.setOnClickListener(v -> { showAddInterestMenu(); });

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
     * Shows the popup menu for the user to add interested categories.
     */
    private void showAddInterestMenu() {

        PopupMenu popup = new PopupMenu(getContext(), addInterestButton);
        popup.getMenuInflater().inflate(R.menu.add_interest_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return tryAddNewInterest();
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
     *
     */
    private boolean tryAddNewInterest() {
        return false;
    }
}