package ca.kidstart.kidstart.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ca.kidstart.kidstart.MainActivity;
import ca.kidstart.kidstart.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MaterialButton signUpButton;
    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText email;
    TextInputEditText phone;


    public SignUpFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signUpButton = view.findViewById(R.id.sign_up_button);
        firstName = view.findViewById(R.id.first_name_input);
        lastName = view.findViewById(R.id.last_name_input);
        phone = view.findViewById(R.id.phone_input);
        email = view.findViewById(R.id.email_input);
        email.setText(mAuth.getCurrentUser().getEmail());
        email.setEnabled(false);


        signUpButton.setOnClickListener((v) ->{
            signUpClicked();
        });


    }

    private void signUpClicked() {
        // TODO add validation
        String firstName = Objects.requireNonNull(this.firstName.getText()).toString();
        String lastName = Objects.requireNonNull(this.lastName.getText()).toString();
        String email = Objects.requireNonNull(mAuth.getCurrentUser().getEmail());
        String phone = Objects.requireNonNull(this.phone.getText()).toString();

        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("email", email);
        user.put("phone", phone);

        db.collection("users").document(mAuth.getUid()).set(user);

        // Redirect user to main activity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);


    }
}