package com.example.test1.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.test1.BaseActivity;
import com.example.test1.MainActivity;
import com.example.test1.R;
import com.example.test1.UserNotification;
import com.example.test1.databinding.ActivitySplashBinding;
import com.example.test1.features.appointments.AppointmentsMainActivity;
import com.example.test1.features.appointments.RequestedAppointmentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        checkLoginStatus();

        binding = ActivitySplashBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        firebaseFirestore
                .collection("doctors")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("appointments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        createAbstractNotification();
                    }
                });
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void checkLoginStatus() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 1000);
    }

    private void createAbstractNotification() {
        Intent intent = new Intent(this, RequestedAppointmentActivity.class);
        // Send a notification to the user
        UserNotification userNotification = new UserNotification(this, "user_channel_id", "John Doe");
        userNotification.sendNotification(1, intent);  // Pass notification ID and intent
    }

}