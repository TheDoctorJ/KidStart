package ca.kidstart.kidstart;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static final HomeFragment homeFragment = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adaptTabs();
        setCurrentTabFragment(homeFragment);
    }

    /**
     * Adapt tab items on the BottomNavigationView to change tab.
     */
    private void adaptTabs() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        FrameLayout frameLayout = findViewById(R.id.tab_frame_layout);

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.home_nav_item) {
                setCurrentTabFragment(homeFragment);
                Toast.makeText(MainActivity.this,
                        "Home tab is not implemented. Example tab is shown.", Toast.LENGTH_SHORT).show();
                return true;
            }
            else if (menuItem.getItemId() == R.id.search_nav_item) {
                setCurrentTabFragment(new ExampleFragment());
                Toast.makeText(MainActivity.this,
                        "Search tab is not implemented. Example tab is shown.", Toast.LENGTH_SHORT).show();
                return true;
            }
            else if (menuItem.getItemId() == R.id.saved_nav_item) {
                setCurrentTabFragment(new ExampleFragment());
                Toast.makeText(MainActivity.this,
                        "Saved tab is not implemented. Example tab is shown.", Toast.LENGTH_SHORT).show();
                return true;
            }
            else if (menuItem.getItemId() == R.id.profile_nav_item) {
                setCurrentTabFragment(new ExampleFragment());
                Toast.makeText(MainActivity.this,
                        "Profile tab is not implemented. Example tab is shown.", Toast.LENGTH_SHORT).show();
                return true;
            }

            Toast.makeText(MainActivity.this, "Selected menu item not found.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Selected menu item not found.");
            return false;
        });
    }

    /**
     * Changes the FrameLayout contents.
     * @param tabFragment is the tab to change to.
     */
    private void setCurrentTabFragment(Fragment tabFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.tab_frame_layout, tabFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}