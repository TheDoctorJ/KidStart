package ca.kidstart.kidstart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.kidstart.kidstart.MainActivity;
import ca.kidstart.kidstart.R;
import ca.kidstart.kidstart.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button btnLogin;
    private TextView tvGoSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoSignup = findViewById(R.id.tvGoSignup);

        btnLogin.setOnClickListener(v -> loginUser());

        tvGoSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        SessionManager sessionManager = new SessionManager(this);

        etLoginEmail.setError(null);
        etLoginPassword.setError(null);

        if (email.isEmpty()) {
            etLoginEmail.setError("Enter your email");
            etLoginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etLoginPassword.setError("Enter your password");
            etLoginPassword.requestFocus();
            return;
        }

        if (!sessionManager.hasUser()) {
            Toast.makeText(this, "No account found. Please sign up first.", Toast.LENGTH_SHORT).show();
            return;
        }

        String savedEmail = sessionManager.getUserEmail();
        String savedPassword = sessionManager.getUserPassword();

        if (!email.equals(savedEmail)) {
            etLoginEmail.setError("Email does not exist");
            etLoginEmail.requestFocus();
            return;
        }

        if (!password.equals(savedPassword)) {
            etLoginPassword.setError("Incorrect password");
            etLoginPassword.requestFocus();
            return;
        }

        sessionManager.setLogin(true);
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}