package com.example.test1.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.BaseActivity;
import com.example.test1.MainActivity;
import com.example.test1.R;
import com.example.test1.databinding.ActivityRegisterBinding;
import com.example.test1.model.DoctorModel;
import com.example.test1.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private List<String> specializationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        setEventListeners();
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void setEventListeners() {
        binding.ivAddSpecialization.setOnClickListener(view -> addSpecialization());

        binding.btnSignUp.setOnClickListener(view -> registerUser());

        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void registerUser() {
        String name = binding.etName.getText().toString().trim();
        String mobileNumber = binding.etMobileNumber.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        List<Integer> timeSlot = getSlots();
        String password = binding.etPassword.getText().toString().trim();
        double lat = 0.0;
        double lang = 0.0;
        int experience = Integer.parseInt(binding.etExperience.getText().toString().trim());
        int totalBookedSlots = 0;
        int totalRevenue = 0;
        int charges = Integer.parseInt(binding.etCharges.getText().toString().trim());

        if (validateInputs(name, mobileNumber, email, password)) {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<String> specializations = getAllSpecializations();

                    DoctorModel doctor = new DoctorModel(name, mobileNumber, email, timeSlot, specializations, lat, lang, experience, totalBookedSlots, totalRevenue, charges);

                    firebaseFirestore
                            .collection("doctors")
                            .document(email)
                            .set(doctor)
                            .addOnSuccessListener(aVoid -> {
                                Constant.showToast(this, "User Registered Successfully");
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }).addOnFailureListener(e -> {
                                Constant.showToast(this, "Error Registering User: " + e.getMessage());
                            });
                } else {
                    Constant.showToast(this, "Error: " + task.getException().getMessage());
                }
            });
        }
    }


    private List<Integer> getSlots() {
        List<Integer> slots = Arrays.asList(
                0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1
        );
        return slots;
    }

    private boolean validateInputs(String name, String mobileNumber, String email, String password) {
        if (name.isEmpty()) {
            binding.etName.setError("Name is required");
            return false;
        }
        if (mobileNumber.isEmpty()) {
            binding.etMobileNumber.setError("Mobile number is required");
            return false;
        }
        if (email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            return false;
        }
        if (password.isEmpty()) {
            binding.etPassword.setError("Password is required");
            return false;
        }
        return true;
    }

    private void addSpecialization() {
        String specialization = binding.etSpecialization.getText().toString().trim();

        if (!specialization.isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout specializationLayout = (LinearLayout) inflater.inflate(R.layout.specialization_layout, null);

// Set layout parameters with margin
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

// Set the start margin to 10
            params.setMargins(10, 0, 0, 0);  // Left, Top, Right, Bottom margins
            specializationLayout.setLayoutParams(params);

            TextView tvSpecialization = specializationLayout.findViewById(R.id.tvSpecialization);
            ImageView tvDelete = specializationLayout.findViewById(R.id.tvDelete);

            tvSpecialization.setText(specialization);

            specializationList.add(specialization);

            binding.llSpecializations.addView(specializationLayout);

            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeSpecialization(specializationLayout, specialization);
                }
            });

            binding.etSpecialization.setText("");
        }
    }

    private void removeSpecialization(View specializationLayout, String specialization) {
        binding.llSpecializations.removeView(specializationLayout);
        specializationList.remove(specialization);
    }

    public List<String> getAllSpecializations() {
        return new ArrayList<>(specializationList);
    }

}