package ca.kidstart.kidstart.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import ca.kidstart.kidstart.MainActivity;
import ca.kidstart.kidstart.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Intent appLinkIntent;
    String appLinkAction;
    Uri appLinkData;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coordinator), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MaterialToolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host);

        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.loginFragment
                ).build();

        NavigationUI.setupWithNavController(
                toolbar,
                navController,
                appBarConfiguration
        );

        // Get the auth
        mAuth = FirebaseAuth.getInstance();
        db.enableNetwork();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // ATTENTION: This was auto-generated to handle app links.
        appLinkIntent = getIntent();
        appLinkAction = appLinkIntent.getAction();
        appLinkData = appLinkIntent.getData();

        // If the user is logged in redirect them to the main activity.
        if (mAuth.getCurrentUser() != null)
        {
            redirectProfileOrMain();
        }
        if (Objects.equals(appLinkAction, Intent.ACTION_VIEW))
        {
            checkSignInOrSignUp();
        }
    }

    private void checkSignInOrSignUp() {

        String emailLink = Objects.requireNonNull(appLinkIntent.getData()).toString();
        SharedPreferences preferences = getSharedPreferences("kidstart", MODE_PRIVATE);
        String email = preferences.getString("email", "");

        // Also show a spinner
        FrameLayout spinner = findViewById(R.id.loading_overlay);
        spinner.setVisibility(View.VISIBLE);

        if (emailLink.isEmpty())
        {
            TextInputLayout emailLayout = findViewById(R.id.login_email_layout);
            emailLayout.setError("The link you used has expired.");
            return;
        }
        mAuth.signInWithEmailLink(email, emailLink).addOnCompleteListener((task) ->{
            if (task.isSuccessful())
            {
                redirectProfileOrMain();
            }
        });


    }

    private void redirectProfileOrMain() {
        // Also show a spinner
        FrameLayout spinner = findViewById(R.id.loading_overlay);
        spinner.setVisibility(View.VISIBLE);

        db.collection("users")
                .document(mAuth.getUid())
                .get()
                .addOnCompleteListener(sub -> {
                    DocumentSnapshot document = sub.getResult();

                    // TODO add shared preferences or persistence.
                    if (document.exists() )
                    {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else{
                        navController.navigate(R.id.action_loginFragment_to_signUpFragment);
                    }

                });
    }
}