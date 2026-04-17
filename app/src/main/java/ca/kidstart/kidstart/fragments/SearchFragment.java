package ca.kidstart.kidstart.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.adapter.ActivityAdapter;
import ca.kidstart.kidstart.data.ActivityDataProvider;
import ca.kidstart.kidstart.model.ActivityItem;

public class SearchFragment extends Fragment {

    private EditText etSearch;
    private MaterialButton btnFilters;
    private ChipGroup chipGroupFilters;
    private TextView tvResultCount;
    private RecyclerView recyclerSearchResults;

    private ActivityAdapter activityAdapter;

    private final List<ActivityItem> allActivities = new ArrayList<>();
    private final List<ActivityItem> filteredActivities = new ArrayList<>();

    private String selectedAgeGroup = null;
    private String selectedCategory = null;
    private String selectedMaxPrice = null;
    private String selectedDistance = null;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.et_search);
        btnFilters = view.findViewById(R.id.btn_filters);
        chipGroupFilters = view.findViewById(R.id.chip_group_filters);
        tvResultCount = view.findViewById(R.id.tv_result_count);
        recyclerSearchResults = view.findViewById(R.id.recycler_search_results);

        recyclerSearchResults.setLayoutManager(new LinearLayoutManager(requireContext()));

        allActivities.clear();
        allActivities.addAll(ActivityDataProvider.getAllActivities());

        filteredActivities.clear();
        filteredActivities.addAll(allActivities);

        activityAdapter = new ActivityAdapter(filteredActivities);
        recyclerSearchResults.setAdapter(activityAdapter);

        updateResultCount();
        updateFilterButtonCount();
        updateSelectedFilterChips();

        btnFilters.setOnClickListener(v -> openFilterBottomSheet());

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            boolean isSearchAction = actionId == EditorInfo.IME_ACTION_SEARCH;
            boolean isEnterKey = event != null
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN;

            if (isSearchAction || isEnterKey) {
                applyFilters();
                etSearch.clearFocus();
                return true;
            }
            return false;
        });
    }

    private void openFilterBottomSheet() {
        FilterBottomSheetFragment sheet = FilterBottomSheetFragment.newInstance(
                selectedAgeGroup,
                selectedCategory,
                selectedMaxPrice,
                selectedDistance
        );

        sheet.setListener(new FilterBottomSheetFragment.OnFilterAppliedListener() {
            @Override
            public void onFilterApplied(String age, String category, String price, String distance) {
                selectedAgeGroup = normalizeValue(age, "All Ages");
                selectedCategory = normalizeValue(category, "All Categories");
                selectedMaxPrice = normalizeValue(price, "Any Price");
                selectedDistance = normalizeValue(distance, "Any Distance");

                applyFilters();
                updateSelectedFilterChips();
                updateFilterButtonCount();
            }

            @Override
            public void onClearAll() {
                selectedAgeGroup = null;
                selectedCategory = null;
                selectedMaxPrice = null;
                selectedDistance = null;

                applyFilters();
                updateSelectedFilterChips();
                updateFilterButtonCount();
            }
        });

        sheet.show(getParentFragmentManager(), "FilterBottomSheet");
    }

    private String normalizeValue(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty() || value.equals(defaultValue)) {
            return null;
        }
        return value;
    }

    private void applyFilters() {
        filteredActivities.clear();

        String query = etSearch.getText() == null
                ? ""
                : etSearch.getText().toString().trim().toLowerCase(Locale.getDefault());

        for (ActivityItem item : allActivities) {
            boolean matches = true;

            if (!query.isEmpty()) {
                String title = item.getTitle().toLowerCase(Locale.getDefault());
                String category = item.getCategory().toLowerCase(Locale.getDefault());
                String location = item.getLocation().toLowerCase(Locale.getDefault());

                boolean matchesSearch = title.contains(query)
                        || category.contains(query)
                        || location.contains(query);

                if (!matchesSearch) {
                    matches = false;
                }
            }

            if (matches && selectedCategory != null
                    && !item.getCategory().equalsIgnoreCase(selectedCategory)) {
                matches = false;
            }

            if (matches && selectedAgeGroup != null
                    && !item.getAgeRange().equalsIgnoreCase(selectedAgeGroup)) {
                matches = false;
            }

            if (matches && selectedMaxPrice != null
                    && !matchesPrice(item, selectedMaxPrice)) {
                matches = false;
            }

            if (matches && selectedDistance != null
                    && !matchesDistance(item, selectedDistance)) {
                matches = false;
            }

            if (matches) {
                filteredActivities.add(item);
            }
        }

        activityAdapter.updateList(filteredActivities);
        updateResultCount();
    }

    private boolean matchesPrice(ActivityItem item, String selectedPrice) {
        double itemPrice = item.getNumericPrice();

        switch (selectedPrice) {
            case "Free":
                return itemPrice == 0;
            case "Under $50":
                return itemPrice <= 50;
            case "Under $300":
                return itemPrice <= 300;
            case "Under $1500":
                return itemPrice <= 1500;
            default:
                return true;
        }
    }

    private boolean matchesDistance(ActivityItem item, String selectedDistance) {
        double itemDistance = item.getNumericDistance();

        switch (selectedDistance) {
            case "Within 2 mi":
                return itemDistance <= 2;
            case "Within 3 mi":
                return itemDistance <= 3;
            case "Within 5 mi":
                return itemDistance <= 5;
            default:
                return true;
        }
    }

    private void updateResultCount() {
        tvResultCount.setText(filteredActivities.size() + " activities found");
    }

    private void updateFilterButtonCount() {
        int count = 0;
        if (selectedCategory != null) count++;
        if (selectedMaxPrice != null) count++;
        if (selectedAgeGroup != null) count++;
        if (selectedDistance != null) count++;

        if (count > 0) {
            btnFilters.setText("Filters " + count);
        } else {
            btnFilters.setText("Filters");
        }
    }

    private void updateSelectedFilterChips() {
        chipGroupFilters.removeAllViews();

        addFilterChip(selectedCategory, () -> {
            selectedCategory = null;
            applyFilters();
            updateSelectedFilterChips();
            updateFilterButtonCount();
        });

        addFilterChip(selectedMaxPrice, () -> {
            selectedMaxPrice = null;
            applyFilters();
            updateSelectedFilterChips();
            updateFilterButtonCount();
        });

        addFilterChip(selectedAgeGroup, () -> {
            selectedAgeGroup = null;
            applyFilters();
            updateSelectedFilterChips();
            updateFilterButtonCount();
        });

        addFilterChip(selectedDistance, () -> {
            selectedDistance = null;
            applyFilters();
            updateSelectedFilterChips();
            updateFilterButtonCount();
        });
    }

    private void addFilterChip(String text, Runnable onRemove) {
        if (text == null) return;

        Chip chip = new Chip(requireContext());
        chip.setText(text);
        chip.setCloseIconVisible(true);
        chip.setClickable(false);
        chip.setCheckable(false);
        chip.setEnsureMinTouchTargetSize(false);
        chip.setChipStartPadding(16f);
        chip.setChipEndPadding(10f);
        chip.setCloseIconEndPadding(6f);
        chip.setOnCloseIconClickListener(v -> onRemove.run());

        chip.setChipBackgroundColorResource(R.color.purple_primary);
        chip.setTextColor(requireContext().getColor(R.color.text_on_primary));
        chip.setCloseIconTintResource(R.color.text_on_primary);

        chipGroupFilters.addView(chip);
    }
}