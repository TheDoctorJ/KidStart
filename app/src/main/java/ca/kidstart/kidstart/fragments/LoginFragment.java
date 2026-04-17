package ca.kidstart.kidstart.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.kidstart.kidstart.R;


public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // Firebase auth provider
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static TextInputEditText emailField;
    private static MaterialTextView accountSubtitleText;
    private static MaterialButton loginButton;
    private static MaterialTextView signUpText;
    private static final String NO_EMAIL_ERROR = "Please enter your email address.";
    private TextInputLayout loginEmailLayout;
    // Hacky save for signup or login, literally no difference.
    private boolean login = true;

    // NavController helps us get where we want to go.
    private NavController navController;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        emailField = view.findViewById(R.id.login_email_input);
        loginButton = view.findViewById(R.id.login_button);
        loginEmailLayout = view.findViewById(R.id.login_email_layout);
        signUpText = view.findViewById(R.id.sign_up);
        accountSubtitleText = view.findViewById(R.id.account_subtitle);


        // Checking valid email.
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0 )
                {
                    loginEmailLayout.setError(NO_EMAIL_ERROR);
                }
                else{
                    loginEmailLayout.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        });
        // Login clicked.
        loginButton.setOnClickListener((b) ->{
            buttonClicked();
        });

        // Sign up clicked.
        signUpText.setOnClickListener(v ->{
            // Hacky switch mode from signup to login, literally no difference.
            if(login){
                signUpText.setText(R.string.login_instead);
                accountSubtitleText.setText(R.string.already_have_an_account);
                loginButton.setText(R.string.sign_up);

                login = !login;
            }else{
                signUpText.setText(getText(R.string.sign_up_here));
                accountSubtitleText.setText(getText(R.string.don_t_have_an_account));
                loginButton.setText(R.string.login);
                login = !login;
            }
        });

    }

    private void buttonClicked() {
        if(emailField.getText().isEmpty())
        {
            loginEmailLayout.setError("Please enter your email address.");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailField.getText()).matches()) {
            loginEmailLayout.setError("Please enter a valid email address.");
            return;
        }
        // Everything gud so now we send email link.
        ActionCodeSettings settings = ActionCodeSettings.newBuilder()
                .setUrl("https://kidstart.web.app")
                .setHandleCodeInApp(true)
                .setIOSBundleId("")
                .setAndroidPackageName(
                        "ca.kidstart.kidstart",
                        true,
                        "1").build();

        // Have to store email in shared preferences.
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("kidstart", MODE_PRIVATE).edit();
        editor.putString("email", emailField.getText().toString());
        editor.apply();


        mAuth.sendSignInLinkToEmail(emailField.getText().toString().trim(), settings)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Snackbar.make(this.getView(), "Email sent!", 5).show();
                    }
                });

        // Notify the user that we've sent them an email link.
        loginEmailLayout.setHelperText("We've sent you an email with a link to login.");
        loginEmailLayout.setHelperTextEnabled(true);
        loginButton.setEnabled(false);
    }


}