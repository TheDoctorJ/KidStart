package ca.kidstart.kidstart.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.textview.MaterialTextView;

import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.model.InterestCategory;

public class InterestCategoryFragment extends Fragment {

    private InterestCategory interestCategory;
    private ImageView interestIcon, removeButton;
    private MaterialTextView interestTitle, interestDescription;

    public InterestCategoryFragment(InterestCategory newInterestCategory) {
        this.interestCategory = newInterestCategory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interest_category, container, false);

        interestIcon = view.findViewById(R.id.interest_icon);
        interestTitle = view.findViewById(R.id.interest_title);
        interestDescription = view.findViewById(R.id.interest_description);

        interestIcon.setImageDrawable(interestCategory.getIcon());
        interestTitle.setText(interestCategory.getName());
        interestDescription.setText(interestCategory.getDescription());

        return view;
    }
}