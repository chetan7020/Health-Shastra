package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.test1.auth.LoginActivity;
import com.example.test1.databinding.ActivityMainBinding;
import com.example.test1.features.appointments.AppointmentsMainActivity;
import com.example.test1.features.dashboard.DashboardMainActivity;
import com.example.test1.features.patients.PatientMainActivity;
import com.example.test1.features.payments.PaymentsMainActivity;
import com.example.test1.features.profile.ProfileMainActivity;
import com.example.test1.model.AppointmentModel;
import com.example.test1.model.DoctorModel;
import com.example.test1.model.MedicalDTO;
import com.example.test1.model.PatientModel;
import com.example.test1.model.PaymentModel;
import com.example.test1.model.ReportModel;
import com.example.test1.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends BaseActivity {


    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setEventListeners();

    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void setEventListeners() {

        binding.llPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PatientMainActivity.class));
            }
        });

        binding.llDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DashboardMainActivity.class));
            }
        });

        binding.llAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AppointmentsMainActivity.class));
            }
        });

        binding.llTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PaymentsMainActivity.class));
            }
        });

        binding.llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileMainActivity.class));
            }
        });
    }


    private void addData(DoctorModel doctorModel) {
        firebaseFirestore
                .collection("doctors")
                .document(doctorModel.getDocEmail())
                .set(doctorModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "onSuccess: " + doctorModel.getDocEmail());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
