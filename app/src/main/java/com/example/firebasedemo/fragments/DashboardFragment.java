package com.example.firebasedemo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.firebasedemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class DashboardFragment extends Fragment {

    private final String[] colors = {"#9c27b0", "#673ab7", "#4caf50", "#ff5722", "#795548"};
    private FirebaseAuth auth;
    private CardView messageCard;
    private TextView welcomeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        messageCard = view.findViewById(R.id.message_card);
        welcomeText = view.findViewById(R.id.welcome_text);
        FirebaseUser user = auth.getCurrentUser();
        String message = "Hey " + user.getEmail() + ", Welcome to the Firebase Demo.";
        welcomeText.setText(message);
        int randomNumber = new Random().nextInt(colors.length);
        int randomColor = Color.parseColor(colors[randomNumber]);
        messageCard.setCardBackgroundColor(randomColor);
    }
}