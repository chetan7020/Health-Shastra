package com.example.test1.features.appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.test1.BaseActivity;
import com.example.test1.databinding.ActivityMainAppointmentsBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppointmentsMainActivity extends BaseActivity {

    private ActivityMainAppointmentsBinding binding;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAppointmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        setEventListeners();
    }

    private void init() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void setEventListeners() {
        binding.llUpcomingAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TYPE = "accepted"; // The string you want to pass
                Intent intent = new Intent(AppointmentsMainActivity.this, AppointmentsActivity.class);
                intent.putExtra("TYPE", TYPE); // Use a unique key to identify the extra data
                startActivity(intent);
            }
        });

        binding.llRequestedAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppointmentsMainActivity.this, RequestedAppointmentActivity.class));
            }
        });

        binding.llDeclinedAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TYPE = "declined"; // The string you want to pass
                Intent intent = new Intent(AppointmentsMainActivity.this, AppointmentsActivity.class);
                intent.putExtra("TYPE", TYPE); // Use a unique key to identify the extra data
                startActivity(intent);
            }
        });
    }
}