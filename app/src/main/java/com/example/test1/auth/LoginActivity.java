package com.example.test1.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.test1.BaseActivity;
import com.example.test1.MainActivity;
import com.example.test1.utils.Constant;  // Assuming the Constant class is in this package
import com.example.test1.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        setEventListeners();
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void setEventListeners() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    binding.etEmail.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    binding.etPassword.setError("Password is required.");
                    return;
                }

                Log.d("TAG", "onClick: " + email + "\n" + password);

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                Constant.showToast(LoginActivity.this, "Login successful");
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Constant.showToast(LoginActivity.this, "Login failed: " + task.getException().getMessage());
                            }
                        });
            }
        });

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
