package ca.kidstart.kidstart.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.adapter.ActivityAdapter;
import ca.kidstart.kidstart.data.SavedItemsManager;
import ca.kidstart.kidstart.model.ActivityItem;

public class SavedFragment extends Fragment {

    private RecyclerView recyclerSaved;
    private TextView tvEmptySaved;
    private ActivityAdapter adapter;

    public SavedFragment() {
        super(R.layout.fragment_saved);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerSaved = view.findViewById(R.id.recyclerSaved);
        tvEmptySaved = view.findViewById(R.id.tvEmptySaved);

        recyclerSaved.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadSavedActivities();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSavedActivities();
    }

    private void loadSavedActivities() {
        List<ActivityItem> savedItems = SavedItemsManager.getSavedItems();

        adapter = new ActivityAdapter(savedItems);
        recyclerSaved.setAdapter(adapter);

        if (savedItems.isEmpty()) {
            recyclerSaved.setVisibility(View.GONE);
            tvEmptySaved.setVisibility(View.VISIBLE);
        } else {
            recyclerSaved.setVisibility(View.VISIBLE);
            tvEmptySaved.setVisibility(View.GONE);
        }
    }
}