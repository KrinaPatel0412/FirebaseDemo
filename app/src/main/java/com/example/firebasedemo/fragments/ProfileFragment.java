package com.example.firebasedemo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.firebasedemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private LinearLayout profileLayout;
    private TextView nameText, emailText, dobText, cityText, genderText;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        profileLayout = view.findViewById(R.id.profile_layout);
        nameText = view.findViewById(R.id.name_text);
        emailText = view.findViewById(R.id.email_text);
        dobText = view.findViewById(R.id.dob_text);
        cityText = view.findViewById(R.id.city_text);
        genderText = view.findViewById(R.id.gender_text);
        progressBar = view.findViewById(R.id.progress_bar);
        getData();
    }

    private void getData() {
        FirebaseUser user = auth.getCurrentUser();
        firestore.collection("users").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            String name = documentSnapshot.getString("name");
            String email = documentSnapshot.getString("email");
            String dob = documentSnapshot.getString("dob");
            String city = documentSnapshot.getString("city");
            String gender = documentSnapshot.getString("gender");
            nameText.setText(name);
            emailText.setText(email);
            dobText.setText(dob);
            cityText.setText(city);
            genderText.setText(gender);
            progressBar.setVisibility(View.GONE);
            profileLayout.setVisibility(View.VISIBLE);
        });
    }
}