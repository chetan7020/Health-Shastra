package com.example.test1.features.payments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.test1.BaseActivity;
import com.example.test1.R;
import com.example.test1.adapter.PaymentAdapter;
import com.example.test1.databinding.ActivityPaymentsMainBinding;
import com.example.test1.databinding.ActivityProfileMainBinding;
import com.example.test1.model.PaymentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class PaymentsMainActivity extends BaseActivity {

    private ActivityPaymentsMainBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private PaymentAdapter paymentAdapter;
    private List<PaymentModel> paymentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentsMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setEventListeners();
        fetchPaymentsFromFirestore();
    }

    private void init() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize payment list and adapter
        paymentList = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(paymentList);

        // Set up RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(paymentAdapter);
    }

    private void setEventListeners() {
        // Add any button listeners or other UI interactions here
    }

    private void fetchPaymentsFromFirestore() {
        // Assume you have a collection named "payments"
        firebaseFirestore
                .collection("doctors")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("payments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            paymentList.clear();

                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                PaymentModel payment = document.toObject(PaymentModel.class);
                                paymentList.add(payment);  // Add payment to the list
                            }

                            // Notify adapter that data has changed
                            paymentAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("Firestore Error", "Error fetching documents: ", task.getException());
                    }
                });
    }
}
