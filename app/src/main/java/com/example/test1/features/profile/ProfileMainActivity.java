package com.example.test1.features.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.BaseActivity;
import com.example.test1.MainActivity;
import com.example.test1.R;
import com.example.test1.auth.LoginActivity;
import com.example.test1.databinding.ActivityProfileMainBinding;
import com.example.test1.model.DoctorModel;
import com.example.test1.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfileMainActivity extends BaseActivity {

    private ActivityProfileMainBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setEventListeners();
        fetchDoctorData();
    }

    private void init() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setEventListeners() {
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.showToast(ProfileMainActivity.this, "go to update");
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(ProfileMainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }


    private void fetchDoctorData() {
        String userEmail = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getEmail() : null;

        if (userEmail != null) {
            firebaseFirestore.collection("doctors") // Replace with your Firestore collection name
                    .document(userEmail)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Map the document to DoctorModel
                            DoctorModel doctor = documentSnapshot.toObject(DoctorModel.class);
                            if (doctor != null) {
                                updateUIWithDoctorData(doctor);
                            }
                        } else {
                            Toast.makeText(ProfileMainActivity.this, "Doctor not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProfileMainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUIWithDoctorData(DoctorModel doctor) {
        binding.tvDocName.setText(doctor.getDocName());
        binding.tvDocMobileNumber.setText(doctor.getDocMobileNumber());
        binding.tvDocAddress.setText("Katraj, Pune, Maharashtra - 411046");
//        binding.tvDocAddress.setText(getAddress(doctor.getDocLat(), doctor.getDocLang()));
        binding.tvDocEmail.setText(firebaseAuth.getCurrentUser().getEmail());
        binding.tvExperience.setText(Integer.toString(doctor.getExperience()));
        binding.tvCharges.setText(Integer.toString(doctor.getCharges()));

        setSpecialization(doctor.getDocSpecializations());

    }

    private void setSpecialization(List<String> specializations) {
        LinearLayout glSpecializations = findViewById(R.id.glSpecializations);
        glSpecializations.removeAllViews(); // Clear existing views if any

        for (String specialization : specializations) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.specialization_normal_layout, glSpecializations, false);

            TextView textView = cardView.findViewById(R.id.tvSpecialization); // Assuming the TextView has this id
            textView.setText(specialization);

            glSpecializations.addView(cardView);
        }
    }


    private String getAddress(double docLat, double docLang) {
        String address = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(docLat, docLang, 1); // Get one address
            if (addresses != null && !addresses.isEmpty()) {
                Address addressObj = addresses.get(0);
                // Construct the address string (you can customize this as needed)
                address = addressObj.getAddressLine(0); // Get the full address line
            }
        } catch (IOException e) {
            e.printStackTrace();
            address = "Unable to get address";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            address = "Invalid latitude or longitude";
        }

        return address;
    }
}
