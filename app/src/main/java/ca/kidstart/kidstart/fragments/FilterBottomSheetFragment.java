package ca.kidstart.kidstart.fragments;

import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ca.kidstart.kidstart.R;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment {

    public interface OnFilterAppliedListener {
        void onFilterApplied(String age, String category, String price, String distance);
        void onClearAll();
    }

    private OnFilterAppliedListener listener;

    private String currentAge;
    private String currentCategory;
    private String currentPrice;
    private String currentDistance;

    public void setListener(OnFilterAppliedListener listener) {
        this.listener = listener;
    }

    public static FilterBottomSheetFragment newInstance(
            String age,
            String category,
            String price,
            String distance
    ) {
        FilterBottomSheetFragment fragment = new FilterBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("age", age);
        args.putString("category", category);
        args.putString("price", price);
        args.putString("distance", distance);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentAge = getArguments().getString("age");
            currentCategory = getArguments().getString("category");
            currentPrice = getArguments().getString("price");
            currentDistance = getArguments().getString("distance");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_filter, container, false);

        AutoCompleteTextView age = view.findViewById(R.id.dropdown_age);
        AutoCompleteTextView category = view.findViewById(R.id.dropdown_category);
        AutoCompleteTextView price = view.findViewById(R.id.dropdown_price);
        AutoCompleteTextView distance = view.findViewById(R.id.dropdown_distance);

        String[] ageList = {"All Ages", "0-3 yrs", "3-6 yrs", "7-12 yrs", "10-14 yrs"};
        String[] categoryList = {"All Categories", "SCIENCE", "DAYCARE", "EDUCATION", "TECHNOLOGY", "SPORTS", "ARTS"};
        String[] priceList = {"Any Price", "Free", "Under $50", "Under $300", "Under $1500"};
        String[] distanceList = {"Any Distance", "Within 2 mi", "Within 3 mi", "Within 5 mi"};

        age.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, ageList));
        category.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categoryList));
        price.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, priceList));
        distance.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, distanceList));

        setupDropdown(age);
        setupDropdown(category);
        setupDropdown(price);
        setupDropdown(distance);

        age.setText(currentAge != null ? currentAge : "All Ages", false);
        category.setText(currentCategory != null ? currentCategory : "All Categories", false);
        price.setText(currentPrice != null ? currentPrice : "Any Price", false);
        distance.setText(currentDistance != null ? currentDistance : "Any Distance", false);

        view.findViewById(R.id.btn_apply_filters).setOnClickListener(v -> {
            if (listener != null) {
                listener.onFilterApplied(
                        age.getText().toString(),
                        category.getText().toString(),
                        price.getText().toString(),
                        distance.getText().toString()
                );
            }
            dismiss();
        });

        view.findViewById(R.id.btn_clear_all).setOnClickListener(v -> {
            if (listener != null) {
                listener.onClearAll();
            }
            dismiss();
        });

        return view;
    }

    private void setupDropdown(AutoCompleteTextView dropdown) {
        dropdown.setInputType(0);
        dropdown.setKeyListener((KeyListener) null);
        dropdown.setCursorVisible(false);
        dropdown.setFocusable(false);
        dropdown.setOnClickListener(v -> dropdown.showDropDown());
    }
}