package ca.kidstart.kidstart;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import ca.kidstart.kidstart.fragments.DiscoverFragment;
import ca.kidstart.kidstart.fragments.ProfileFragment;
import ca.kidstart.kidstart.fragments.SavedFragment;
import ca.kidstart.kidstart.fragments.SearchFragment;
import ca.kidstart.kidstart.model.InterestCategory;

public class MainActivity extends AppCompatActivity {

    public static InterestCategory[] interestCategories;

    private MaterialToolbar topBar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBar = findViewById(R.id.top_bar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            loadFragment(new DiscoverFragment(), "KidStart");
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                loadFragment(new DiscoverFragment(), "KidStart");
                return true;
            } else if (id == R.id.nav_search) {
                loadFragment(new SearchFragment(), "Search");
                return true;
            } else if (id == R.id.nav_saved) {
                loadFragment(new SavedFragment(), "Saved");
                return true;
            } else if (id == R.id.nav_profile) {
                loadFragment(new ProfileFragment(), "Profile");
                return true;
            }

            return false;
        });

        makeInterestCategories();
    }

    private void loadFragment(Fragment fragment, String title) {
        if (topBar != null) {
            topBar.setTitle(title);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.tab_frame_layout, fragment)
                .commit();
    }

    /**
     * Creates the public static field that contains all InterestCategories available.
     * See interest_categories.xml and InterestCategory.java.
     * This may not be the best place to store InterestCategories.
     * **/
    private void makeInterestCategories() {
        int categoryCount = InterestCategory.Categories.values().length;
        interestCategories = new InterestCategory[categoryCount];
        for (int i = 0; i < categoryCount; i++) {
            interestCategories[i] = new InterestCategory(
                    getResources().getDrawable(getResources().getIntArray(R.array.interest_category_drawables)[i], getTheme()),
                    getString(getResources().getIntArray(R.array.interest_category_names)[i]),
                    getString(getResources().getIntArray(R.array.interest_category_descriptions)[i]));
        }
    }
}