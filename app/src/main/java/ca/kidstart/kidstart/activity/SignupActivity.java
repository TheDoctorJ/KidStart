package ca.kidstart.kidstart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.kidstart.kidstart.MainActivity;
import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.utils.SessionManager;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etLocation;
    private Spinner spAgeGroup;
    private Button btnCreateAccount;
    private TextView tvGoLogin;
    private ImageButton btnBack;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sessionManager = new SessionManager(this);

        etName = findViewById(R.id.etSignupName);
        etEmail = findViewById(R.id.etSignupEmail);
        etPassword = findViewById(R.id.etSignupPassword);
        etLocation = findViewById(R.id.etLocation);
        spAgeGroup = findViewById(R.id.spAgeGroup);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvGoLogin = findViewById(R.id.tvGoLogin);
        btnBack = findViewById(R.id.btnBack);

        String[] ageGroups = {
                "Toddler (3 - 5 years)",
                "Child (6 - 8 years)",
                "Pre-Teen (9 - 12 years)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ageGroups
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAgeGroup.setAdapter(adapter);

        btnCreateAccount.setOnClickListener(v -> createAccount());

        tvGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void createAccount() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(location)) {
            etLocation.setError("Location is required");
            etLocation.requestFocus();
            return;
        }

        // test sign up
        sessionManager.setLogin(true);
        sessionManager.saveUserInfo(name, email);

        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}