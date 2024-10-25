package com.example.test1.features.appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.test1.BaseActivity;
import com.example.test1.R;
import com.example.test1.adapter.RequestAppointmentAdapter;
import com.example.test1.databinding.ActivityRequestedAppointmentBinding;
import com.example.test1.model.AppointmentModel;
import com.example.test1.model.PatientModel;
import com.example.test1.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RequestedAppointmentActivity extends BaseActivity implements RequestAppointmentAdapter.OnAppointmentActionListener {

    private ActivityRequestedAppointmentBinding binding;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private RequestAppointmentAdapter adapter;
    private List<AppointmentModel> appointmentList;

    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestedAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        loadAppointmentsFromFirestore();
    }

    // Initialize Firestore and RecyclerView
    private void init() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userEmail = firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getEmail() : null;

        appointmentList = new ArrayList<>();

        // Set up RecyclerView with adapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RequestAppointmentAdapter(appointmentList, this);
        binding.recyclerView.setAdapter(adapter);
    }

    // Load data from Firestore and notify the adapter
    private void loadAppointmentsFromFirestore() {

        Constant.showToast(this, userEmail);

        firebaseFirestore
                .collection("doctors")
                .document(userEmail)
                .collection("appointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        Constant.showToast(RequestedAppointmentActivity.this, String.valueOf(querySnapshot.size()));

                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                AppointmentModel appointment = document.toObject(AppointmentModel.class);

//                                Constant.showToast(RequestedAppointmentActivity.this, appointment.getAppointmentStatus());

                                if (appointment.getAppointmentStatus().equals(new String("waiting"))) {
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

    private void updateObject(AppointmentModel appointmentModel) {
        firebaseFirestore
                .collection("doctors")
                .document(userEmail)
                .collection("appointments")
                .document(appointmentModel.getAppointmentID())
                .set(appointmentModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        appointmentList.remove(appointmentModel);
                        adapter.notifyDataSetChanged();
                        Constant.showToast(RequestedAppointmentActivity.this, "Updated");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Constant.showToast(RequestedAppointmentActivity.this, "ERROR : " + e.getMessage());
                    }
                });

    }

    @Override
    public void onAccept(AppointmentModel appointment) {
        appointment.setAppointmentStatus("accepted");
        addPatientToDoctor(appointment);
        addAppointmentToPatient(appointment);
        updateObject(appointment);
    }

    private void addAppointmentToPatient(AppointmentModel appointment) {
        Constant.showToast(RequestedAppointmentActivity.this, appointment.getPatientEmail());

        firebaseFirestore
                .collection("patients")
                .document(appointment.getPatientEmail())
                .collection("appointments")
                .document(appointment.getAppointmentID())
                .set(appointment);
    }

    @Override
    public void onDecline(AppointmentModel appointment) {
        appointment.setAppointmentStatus("declined");
        updateObject(appointment);
    }


    private void addPatientToDoctor(AppointmentModel appointment) {

        firebaseFirestore
                .collection("patients")
                .document(appointment.getPatientEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        PatientModel patientModel = documentSnapshot.toObject(PatientModel.class);

                        firebaseFirestore
                                .collection("doctors")
                                .document(firebaseAuth.getCurrentUser().getEmail())
                                .collection("patients")
                                .document(patientModel.getPatientEmail())
                                .set(patientModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Constant.showToast(RequestedAppointmentActivity.this, "Patient Assingned to Doctor");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Constant.showToast(RequestedAppointmentActivity.this, "ERROR : " + e.getMessage());
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Constant.showToast(RequestedAppointmentActivity.this, "ERROR : " + e.getMessage());
                    }
                });
    }
}
