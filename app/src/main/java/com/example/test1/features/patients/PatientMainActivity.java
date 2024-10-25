package com.example.test1.features.patients;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AppCompatActivity; // Fixed: Using AppCompatActivity for better compatibility

import com.example.test1.BaseActivity;
import com.example.test1.R;
import com.example.test1.adapter.PatientAdapter;
import com.example.test1.databinding.ActivityPatientMainBinding;
import com.example.test1.model.PatientModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientMainActivity extends BaseActivity { // Changed: Extend AppCompatActivity

    private ActivityPatientMainBinding binding;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setEventListeners();
    }

    private void init() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userEmail = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getEmail() : null;

    }

    private void setEventListeners() {
        fetchPatientsFromFirestore();
    }

    private void fetchPatientsFromFirestore() {
        firebaseFirestore.collection("doctors")
                .document(userEmail)
                .collection("patients")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<PatientModel> patientList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                PatientModel patient = document.toObject(PatientModel.class);
                                if (patient != null) { // Added: Null check for safety
                                    patientList.add(patient);
                                }
                            } catch (Exception e) {
                                Log.e("PatientMainActivity", "Error parsing document: ", e);
                            }
                        }
                        setupRecyclerView(patientList);
                    } else {
                        Log.e("PatientMainActivity", "Error getting documents: ", task.getException()); // Changed: Improved error logging
                    }
                });
    }

    private void setupRecyclerView(List<PatientModel> patientList) {
        // Initialize the LayoutManager first
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the adapter after the LayoutManager is set
        PatientAdapter patientAdapter = new PatientAdapter(patientList);
        binding.recyclerView.setAdapter(patientAdapter);
    }

}
