package com.example.firebasedemo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.firebasedemo.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class RegisterFragment extends Fragment {

    private final String[] genders = {"Male", "Female"};
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private LinearLayout registerLayout;
    private TextInputLayout nameInputLayout, emailInputLayout, dobInputLayout, cityInputLayout, genderInputLayout, passwordInputLayout;
    private Button registerButton;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        registerLayout = view.findViewById(R.id.register_layout);
        nameInputLayout = view.findViewById(R.id.name_input_layout);
        emailInputLayout = view.findViewById(R.id.email_input_layout);
        dobInputLayout = view.findViewById(R.id.dob_input_layout);
        cityInputLayout = view.findViewById(R.id.city_input_layout);
        genderInputLayout = view.findViewById(R.id.gender_input_layout);
        passwordInputLayout = view.findViewById(R.id.password_input_layout);
        registerButton = view.findViewById(R.id.register_button);
        progressBar = view.findViewById(R.id.progress_bar);
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date of Birth")
                .build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dobInputLayout.getEditText().setText(dateFormat.format(selection));
        });
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_gender, genders);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) genderInputLayout.getEditText();
        autoCompleteTextView.setAdapter(genderAdapter);
        dobInputLayout.getEditText().setOnClickListener(v -> {
            datePicker.show(getChildFragmentManager(), "datePicker");
        });
        registerButton.setOnClickListener(v -> {
            String name = nameInputLayout.getEditText().getText().toString();
            String email = emailInputLayout.getEditText().getText().toString();
            String dob = dobInputLayout.getEditText().getText().toString();
            String city = cityInputLayout.getEditText().getText().toString();
            String gender = genderInputLayout.getEditText().getText().toString();
            String password = passwordInputLayout.getEditText().getText().toString();
            if (validate(name) && validate(email) && validate(dob) && validate(city) && validate(gender) && validate(password)) {
                register(v, name, email, dob, city, gender, password);
            } else {
                Toast.makeText(getContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(View view, String name, String email, String dob, String city, String gender, String password) {
        showProgressBar(true);
        HashMap<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("dob", dob);
        data.put("city", city);
        data.put("gender", gender);
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            String userId = authResult.getUser().getUid();
            firestore.collection("users").document(userId).set(data).addOnSuccessListener(aVoid -> {
                Navigation.findNavController(view).navigateUp();
                Toast.makeText(getContext(), "Registered successfully.", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                showProgressBar(false);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            });
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
            registerLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            registerLayout.setVisibility(View.VISIBLE);
        }
    }
}