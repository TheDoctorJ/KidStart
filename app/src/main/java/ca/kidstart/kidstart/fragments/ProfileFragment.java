package ca.kidstart.kidstart.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.activity.LoginActivity;
import ca.kidstart.kidstart.model.InterestCategory;
import ca.kidstart.kidstart.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private final int MAX_USERNAME_LENGTH = 30;

    private View fragmentView;
    private MaterialButton addInterestButton;
    private InterestCategory[] interestCategories;
    private boolean[] selectedInterests;
    private boolean editingProfile = false;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    // Edit profile stuff
    private MaterialButton editProfileButton, applyEditProfileButton;
    private CircularRevealCardView profileSummaryCard, editProfileCard;
    private MaterialTextView userName;
    private TextInputEditText userNameEdit;
    private ShapeableImageView avatar, avatarEdit;
    private BitmapDrawable committedAvatar;
    private MaterialTextView userEmail;

    // FireBase
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);


        // Load saved stuff
        handleEdits();
        loadUserInfo();
        loadProfilePreferences();
        loadInterestedCategories();
        // Buttons
        addInterestButton = fragmentView.findViewById(R.id.add_interest);
        addInterestButton.setOnClickListener(v -> showAddInterestMenu());

        handleSettingsAndLogoutButtons();


        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Don't touch
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        ContentResolver resolver = getContext().getContentResolver();
                        try {
                            InputStream stream = resolver.openInputStream(uri);
                            commitNewAvatar(new BitmapDrawable(getResources(), stream));
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
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

        SessionManager sessionManager = new SessionManager(requireContext());
        String savedChildAge = sessionManager.getChildAge();

        if (savedChildAge != null && !savedChildAge.isEmpty()) {
            ageAutoComplete.setText(savedChildAge, false);
        } else {
            ageAutoComplete.setText(getResources().getStringArray(R.array.age_array)[0], false);
        }

        distanceAutoComplete.setText(getResources().getStringArray(R.array.distance_array)[0], false);
        //else => load profilePreferences
        MaterialTextView email = fragmentView.findViewById(R.id.user_email);
        assert mAuth.getCurrentUser() != null;
        email.setText(mAuth.getCurrentUser().getEmail());
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

    /**
     * Gets setting and logout buttons and writes the onClickListener.
     */
    private void handleSettingsAndLogoutButtons() {
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

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
    /**
     * Shows the views to edit the profile and hides the regular profile summary.
     */
    private void openEditProfile() {
        editingProfile = true;
        profileSummaryCard.setVisibility(View.GONE);
        editProfileCard.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the regular profile summary and closes the edit mode. Saves any changes.
     * To do: Save changes
     */
    private void closeEditProfile() {
        editingProfile = false;
        profileSummaryCard.setVisibility(View.VISIBLE);
        editProfileCard.setVisibility(View.GONE);

        String newUserName = userNameEdit.getText().toString();
        if (newUserName.isEmpty()) {
            Snackbar.make(fragmentView, getString(R.string.empty_username_snack), Snackbar.LENGTH_SHORT).show();
            userNameEdit.setText(userName.getText().toString());
        }
        else {
            userName.setText(newUserName);
            // save newUserName
        }

        if (committedAvatar == null) {
            // Do nothing
        }
        else {
            avatar.setImageDrawable(committedAvatar);
            // save committedAvatar
            committedAvatar = null;
        }
    }

    /**
     * Gets all views for the profile and profile editing and writes listeners.
     */
    private void handleEdits() {
        profileSummaryCard = fragmentView.findViewById(R.id.profile_summary);
        editProfileCard = fragmentView.findViewById(R.id.profile_edit);

        userName = fragmentView.findViewById(R.id.user_name);
        userEmail = fragmentView.findViewById(R.id.user_email);
        userNameEdit = fragmentView.findViewById(R.id.user_name_edit);

        avatar = fragmentView.findViewById(R.id.user_avatar);
        avatarEdit = fragmentView.findViewById(R.id.user_avatar_edit);

        editProfileButton = fragmentView.findViewById(R.id.edit_profile);
        applyEditProfileButton = fragmentView.findViewById(R.id.apply_edit_profile);

        editProfileButton.setOnClickListener(v -> openEditProfile());
        applyEditProfileButton.setOnClickListener(v -> closeEditProfile());

        avatarEdit.setOnClickListener(v -> pickMedia.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()
        ));
    }

    /**
     * Given a new drawable avatar, set it, but don't save until apply button is pressed.
     * @param newAvatar new drawable avatar.
     */
    private void commitNewAvatar(BitmapDrawable newAvatar) {
        avatarEdit.setImageDrawable(newAvatar);
        committedAvatar = newAvatar;
    }

    private void loadUserInfo() {

        SharedPreferences preferences = requireActivity().getSharedPreferences("kidstart", MODE_PRIVATE);
        String savedName = preferences.getString("firstName", "")+ " " +preferences.getString("lastName", "");
        userName.setText(savedName);
        assert mAuth.getCurrentUser() != null;
        userEmail.setText(mAuth.getCurrentUser().getEmail());
        userNameEdit.setText(savedName);


    }
}