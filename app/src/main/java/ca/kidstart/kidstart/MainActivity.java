package ca.kidstart.kidstart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import ca.kidstart.kidstart.activity.LoginActivity;
import ca.kidstart.kidstart.fragments.DiscoverFragment;
import ca.kidstart.kidstart.fragments.ProfileFragment;
import ca.kidstart.kidstart.fragments.SavedFragment;
import ca.kidstart.kidstart.fragments.SearchFragment;
import ca.kidstart.kidstart.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar topBar;
    private BottomNavigationView bottomNavigationView;
    private TextView tvToolbarTitle;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        topBar = findViewById(R.id.top_bar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        View customView = getLayoutInflater().inflate(R.layout.toolbar_kidstart, topBar, false);
        topBar.addView(customView);

        tvToolbarTitle = customView.findViewById(R.id.tvToolbarTitle);

        if (savedInstanceState == null) {
            loadFragment(new DiscoverFragment(), "Home");
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                loadFragment(new DiscoverFragment(), "Home");
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
    }
    private void loadFragment(Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.tab_frame_layout, fragment)
                .commit();
    }
}
