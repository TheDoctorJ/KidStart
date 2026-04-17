package ca.kidstart.kidstart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.utils.SessionManager;

public class SignupActivity extends AppCompatActivity {

    private EditText etSignupName;
    private EditText etSignupEmail;
    private EditText etSignupPassword;
    private Spinner spAgeGroup;
    private Button btnCreateAccount;
    private TextView tvGoLogin;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etSignupName = findViewById(R.id.etSignupName);
        etSignupEmail = findViewById(R.id.etSignupEmail);
        etSignupPassword = findViewById(R.id.etSignupPassword);
        spAgeGroup = findViewById(R.id.spAgeGroup);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvGoLogin = findViewById(R.id.tvGoLogin);
        btnBack = findViewById(R.id.btnBack);

        String[] ageOptions = {
                "0-3 years (Toddler)",
                "3-6 years (Preschool)",
                "7-12 years (Early Elementary)",
                "10-13 Years (Late Elementary)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                ageOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAgeGroup.setAdapter(adapter);

        btnCreateAccount.setOnClickListener(v -> registerUser());

        tvGoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String name = etSignupName.getText().toString().trim();
        String email = etSignupEmail.getText().toString().trim();
        String password = etSignupPassword.getText().toString().trim();
        String childAge = spAgeGroup.getSelectedItem().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etSignupEmail.setError("Enter a valid email");
            return;
        }

        if (password.length() < 6) {
            etSignupPassword.setError("Password must be at least 6 characters");
            return;
        }

        SessionManager sessionManager = new SessionManager(this);
        sessionManager.saveUserInfo(name, email, password, childAge);

        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}