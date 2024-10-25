package com.example.test1.features.patients;

import androidx.annotation.NonNull;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.test1.BaseActivity;
import com.example.test1.R;
import com.example.test1.databinding.ActivityCreateReportBinding;
import com.example.test1.databinding.ActivityPatientMainBinding;
import com.example.test1.features.profile.ProfileMainActivity;
import com.example.test1.model.DoctorModel;
import com.example.test1.model.PatientModel;
import com.example.test1.model.ReportModel;
import com.example.test1.utils.Constant;
import com.example.test1.utils.MedicalData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateReportActivity extends BaseActivity {
    private ActivityCreateReportBinding binding;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String reportDate;
    private String docEmail; //change to docEmail
    private String docName;

    private String blockID;

    private String image;
    private String PDF;

    private String description;

    private List<String> symptomsList = new ArrayList<>();
    private List<String> medicinesList = new ArrayList<>();
    private List<String> measuresList = new ArrayList<>();


    private PatientModel patientModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        patientModel = (PatientModel) intent.getSerializableExtra("patient");

        init();
        setEventListeners();
    }

    private void init() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        description = "";
    }

    private void setEventListeners() {
        binding.btnAddReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                firebaseFirestore
                        .collection("doctors")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {


                                DoctorModel doctor = documentSnapshot.toObject(DoctorModel.class);

                                description = binding.etDescription.getText().toString();
                                docEmail = firebaseAuth.getCurrentUser().getEmail();
                                docName = doctor.getDocName();
                                String dataString = "";

                                reportDate = String.valueOf(System.currentTimeMillis());
                                docEmail = firebaseAuth.getCurrentUser().getEmail();


                                image = "";
                                PDF = "";

//                                callApi();

                                MedicalData medicalData = new MedicalData(description, symptomsList, medicinesList, measuresList);

                                String reportText = medicalData.convertToString();


                                Constant.showToast(CreateReportActivity.this, reportText);

                                sendReportAndGetSavedId(reportText, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(String savedId) {
                                        // Use the savedId here
                                        blockID = savedId;
                                        Toast.makeText(CreateReportActivity.this, "saveId : " + savedId, Toast.LENGTH_SHORT).show();

                                        ReportModel reportModel = new ReportModel(reportDate, docEmail, docName, patientModel.getPatientEmail(), patientModel.getPatientName(), blockID, image, PDF);

                                        firebaseFirestore
                                                .collection("patients")
                                                .document(patientModel.getPatientEmail())
                                                .collection("reports")
                                                .document(reportDate)
                                                .set(reportModel)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        firebaseFirestore
                                                                .collection("doctors")
                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                .collection("reports_" + patientModel.getPatientEmail())
                                                                .document(reportModel.getReportDate())
                                                                .set(reportModel)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Constant.showToast(CreateReportActivity.this, "Report Generated");
                                                                        startActivity(new Intent(CreateReportActivity.this, PatientMainActivity.class));
                                                                        finish();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Constant.showToast(CreateReportActivity.this, "ERROR : " + e.getMessage());
                                                                    }
                                                                });

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Constant.showToast(CreateReportActivity.this, "ERROR : " + e.getMessage());
                                                    }
                                                });


                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
        });

        binding.ivAddSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSymptoms();
            }
        });

        binding.ivAddMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMeasures();
            }
        });

        binding.ivAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedicines();
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
                                docName = doctor.getDocName();
                            }
                        } else {
                            Toast.makeText(CreateReportActivity.this, "Doctor not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CreateReportActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }


    public void sendReportAndGetSavedId(String reportText, final VolleyCallback callback) {
        // API URL
        String url = "https://blockchain-records.onrender.com/api/records";

        // Create a request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create the JSON object to send in the request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("record", reportText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String savedId = response.getString("savedId");
                            Toast.makeText(CreateReportActivity.this, "Success " + savedId, Toast.LENGTH_SHORT).show();
                            callback.onSuccess(savedId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String responseBody = new String(error.networkResponse.data);
                            System.out.println("Error Status Code: " + statusCode);
                            System.out.println("Error Response Body: " + responseBody);
                            Toast.makeText(CreateReportActivity.this, "Error " + statusCode + ": " + responseBody, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateReportActivity.this, "Timeout or Network Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000, // 30 seconds timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Default retry count
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Default backoff multiplier
        ));

// Add the request to the request queue
        queue.add(jsonObjectRequest);

    }

    // Callback interface to return the savedId
    public interface VolleyCallback {
        void onSuccess(String savedId);
    }


    private void callApi() {

        MedicalData medicalData = new MedicalData(description, symptomsList, medicinesList, measuresList);

        String reportText = medicalData.convertToString();


        // URL of the API
        String url = "https://blockchain-records.onrender.com/api/records";

        // Create JSON object for the request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("record", reportText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Constant.showToast(CreateReportActivity.this, "IN callAPI");

        // Create a new request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Constant.showToast(CreateReportActivity.this, "response");

                            blockID = String.valueOf(response.getInt("savedId"));

                            Constant.showToast(CreateReportActivity.this, response.toString());

                            Constant.showToast(CreateReportActivity.this, blockID);
                            Constant.showToast(CreateReportActivity.this, reportText);


                        } catch (JSONException e) {
                            Constant.showToast(CreateReportActivity.this, e.getMessage());

                            e.printStackTrace();
//                            binding.tvSummary.setText("Error parsing response");
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Handle the error
                        Constant.showToast(CreateReportActivity.this, e.getMessage());
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    private void addSymptoms() {
        String specialization = binding.etSymptoms.getText().toString().trim();

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

            symptomsList.add(specialization);

            binding.llSymptoms.addView(specializationLayout);

            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeSymptom(specializationLayout, specialization);
                }
            });

            binding.etSymptoms.setText("");
        }
    }

    private void removeSymptom(View symptomLayout, String symptom) {
        binding.llSymptoms.removeView(symptomLayout);
        symptomsList.remove(symptom);
    }

    public List<String> getAllSymptoms() {
        return new ArrayList<>(symptomsList);
    }


    private void addMedicines() {
        String specialization = binding.etMedicines.getText().toString().trim();

        if (!specialization.isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout specializationLayout = (LinearLayout) inflater.inflate(R.layout.specialization_layout, null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(10, 0, 0, 0);  // Left, Top, Right, Bottom margins
            specializationLayout.setLayoutParams(params);

            TextView tvSpecialization = specializationLayout.findViewById(R.id.tvSpecialization);
            ImageView tvDelete = specializationLayout.findViewById(R.id.tvDelete);

            tvSpecialization.setText(specialization);

            medicinesList.add(specialization);

            binding.llMedicines.addView(specializationLayout);

            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeMedicine(specializationLayout, specialization);
                }
            });

            binding.etMedicines.setText("");
        }
    }

    private void removeMedicine(View medicineLayout, String medicine) {
        binding.llMedicines.removeView(medicineLayout);
        symptomsList.remove(medicine);
    }

    public List<String> getAllMedicine() {
        return new ArrayList<>(symptomsList);
    }


    private void addMeasures() {
        String measure = binding.etMeasures.getText().toString().trim();

        if (!measure.isEmpty()) {
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

            tvSpecialization.setText(measure);

            measuresList.add(measure);

            binding.llMeasures.addView(specializationLayout);

            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeMeasure(specializationLayout, measure);
                }
            });

            binding.etMeasures.setText("");
        }
    }

    private void removeMeasure(View measureLayout, String measure) {
        binding.llMeasures.removeView(measureLayout);
        measuresList.remove(measure);
    }

    public List<String> getAllMeasures() {
        return new ArrayList<>(measuresList);
    }

}