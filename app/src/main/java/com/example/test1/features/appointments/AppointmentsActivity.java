package com.example.test1.features.appointments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.test1.BaseActivity;
import com.example.test1.R;
import com.example.test1.adapter.AppointmentAdapter;
import com.example.test1.databinding.ActivityAppointmentsBinding;
import com.example.test1.model.AppointmentModel;
import com.example.test1.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsActivity extends BaseActivity {

    private ActivityAppointmentsBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    // List to hold the appointments
    private List<AppointmentModel> appointmentList;
    // Adapter for RecyclerView
    private AppointmentAdapter adapter;

    Intent intent ;
    String TYPE ;

    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();
        TYPE = intent.getStringExtra("TYPE");

        Constant.showToast(AppointmentsActivity.this, TYPE);

        init(); // Initialize necessary variables

        userEmail = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getEmail() : null;

        loadAppointmentsFromFirestore(); // Load data from Firestore
    }

    private void init() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        appointmentList = new ArrayList<>();

        // Initialize RecyclerView and set layout manager
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with an empty list initially
        adapter = new AppointmentAdapter(appointmentList);
        // Set the adapter to the RecyclerView
        binding.recyclerView.setAdapter(adapter);
    }

    // Method to load appointments from Firestore
    private void loadAppointmentsFromFirestore() {
        firebaseFirestore.collection("doctors")
                .document(userEmail)
                .collection("appointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Convert each document to AppointmentModel object
                                AppointmentModel appointment = document.toObject(AppointmentModel.class);
                                if(appointment.getAppointmentStatus().equals(TYPE)){
                                    appointmentList.add(appointment); // Add to the list
                                }
                            }
                            adapter.notifyDataSetChanged(); // Notify adapter of data change
                        }
                    } else {
                        Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        Toast.makeText(this, "Error loading appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
