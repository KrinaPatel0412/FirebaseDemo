package com.example.firebasedemo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.firebasedemo.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private FirebaseAuth auth;
    private LinearLayout loginLayout;
    private TextInputLayout emailInputLayout, passwordInputLayout;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        loginLayout = view.findViewById(R.id.login_layout);
        emailInputLayout = view.findViewById(R.id.email_input_layout);
        passwordInputLayout = view.findViewById(R.id.password_input_layout);
        loginButton = view.findViewById(R.id.login_button);
        progressBar = view.findViewById(R.id.progress_bar);
        loginButton.setOnClickListener(v -> {
            String email = emailInputLayout.getEditText().getText().toString();
            String password = passwordInputLayout.getEditText().getText().toString();
            if (validate(email) && validate(password)) {
                login(v, email, password);
            } else {
                Toast.makeText(getContext(), "Both fields are required.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(View view, String email, String password) {
        showProgressBar(true);
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Navigation.findNavController(view).navigateUp();
            Toast.makeText(getContext(), "Logged in successfully.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            showProgressBar(false);
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validate(String value) {
        return value != null && !value.isEmpty();
    }

    private void showProgressBar(boolean show) {
        if (show) {
            loginLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        }
    }
}