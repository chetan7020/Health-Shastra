package com.example.test1.features.patients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.test1.BaseActivity;
import com.example.test1.adapter.ReportAdapter;
import com.example.test1.databinding.ActivityPatientDescriptionBinding;
import com.example.test1.model.PatientModel;
import com.example.test1.model.ReportModel;
import com.example.test1.utils.Constant;
import com.example.test1.utils.MedicalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatientDescriptionActivity extends BaseActivity {

    private ActivityPatientDescriptionBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private PatientModel patientModel;

    // List to store reports
    private List<ReportModel> reportList;
    private ReportAdapter reportAdapter;

    private String reportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientDescriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize patient data
        Intent intent = getIntent();
        patientModel = (PatientModel) intent.getSerializableExtra("patient");

        // Initialize Firestore and RecyclerView
        init();

        // Load Reports from Firestore
        loadReportsFromFirestore();


        registerListener();
    }

    private void registerListener() {
        reportAdapter.setOnItemClickListener(new ReportAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ReportModel selectedReport = reportList.get(position);

                Intent intent = new Intent(PatientDescriptionActivity.this, ViewReportActivity.class);
                intent.putExtra("reportModel", selectedReport);
                startActivity(intent);
            }
        });

    }

    private void init() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize report list and adapter
        reportList = new ArrayList<>();
        reportAdapter = new ReportAdapter(reportList);

        // Set up RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(reportAdapter);

        reportText = "";

        binding.tvPatientName.setText(patientModel.getPatientName());
        binding.tvPatientAge.setText(Integer.toString(patientModel.getPatientAge()));
        binding.tvPatientGender.setText(patientModel.getPatientGender());
        binding.tvPatientBloodGroup.setText(patientModel.getPatientBloodGroup());
    }

    private void loadReportsFromFirestore() {
        firebaseFirestore
                .collection("doctors")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("reports_" + patientModel.getPatientEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Clear the current list
                            reportList.clear();

                            // Loop through the results and populate the reportList
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ReportModel report = document.toObject(ReportModel.class);

                                String id = report.getBlockID();

                                Log.d("TAG", "onComplete: " + id);

                                String url = "https://blockchain-records.onrender.com/api/records/" + id;

                                // Create a request queue
                                RequestQueue requestQueue = Volley.newRequestQueue(PatientDescriptionActivity.this);

                                // Create a string request
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    // Parse the response as JSON
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    // Extract the value of the "record" key
                                                    String recordText = jsonObject.getString("record");

                                                    // Use the extracted text

                                                    MedicalData recoveredData = MedicalData.fromString(recordText);
                                                    Log.d("TAG", "onComplete: " + recoveredData.toString());

                                                    Constant.showToast(PatientDescriptionActivity.this, recoveredData.toString());

                                                    reportText = reportText + " " + recoveredData.toString() + "\n";
                                                    callApi(reportText);

//                            Toast.makeText(context, "Record: " + recordText, Toast.LENGTH_SHORT).show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
//                                                    Toast.makeText(context, "JSON parsing error", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Handle the error
                                                System.out.println("Error: " + error.toString());
                                            }
                                        });

                                // Set a custom retry policy to increase timeout duration
                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                        30000, // 30 seconds timeout
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Number of retries
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Backoff multiplier
                                ));

                                // Add the request to the RequestQueue
                                requestQueue.add(stringRequest);


//                                fetchRecord(PatientDescriptionActivity.this, blockID);

                                reportList.add(report);
                            }

                            Constant.showToast(PatientDescriptionActivity.this, reportText);

                            // Notify the adapter of data changes
                            reportAdapter.notifyDataSetChanged();

                            Constant.showToast(PatientDescriptionActivity.this, reportText);


                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void fetchRecord(Context context, String id) {
        String url = "https://blockchain-records.onrender.com/api/records/" + id;

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Create a string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            // Parse the response as JSON
                            JSONObject jsonObject = new JSONObject(response);
                            // Extract the value of the "record" key
                            String recordText = jsonObject.getString("record");

                            // Use the extracted text
                            Constant.showToast(PatientDescriptionActivity.this, recordText);

                            MedicalData recoveredData = MedicalData.fromString(recordText);

                            reportText = reportText + " " + recoveredData.toString() + "\n";

                            callApi(reportText);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        System.out.println("Error: " + error.toString());
                    }
                });

        // Set a custom retry policy to increase timeout duration
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000, // 30 seconds timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Number of retries
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT // Backoff multiplier
        ));

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);

    }


    private String getReportString(ReportModel report) {
        String res = "";

//        MedicalData medicalData = new MedicalData()

        return res;
    }

    private void callApi(String reportText) {
        // URL of the API
        String url = "https://patient-summary.onrender.com/summarize";

        Constant.showToast(PatientDescriptionActivity.this, reportText);

        // Create JSON object for the request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("description", reportText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                        // Handle the response here
                        Log.d("API Response", response.toString()); // Log the response
                        updateUI(response); // Call a method to update UI
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e("API Error", error.toString()); // Log the error
//                        binding.tvSummary.setText("Error: " + error.getMessage()); // Display error message
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void updateUI(JSONObject response) {
        // Extract data from the response and update the UI
        try {
            // Assuming the response has a field called "summary"
            String summary = response.getString("summary");
//            binding.tvSummary.setText(summary); // Display the summary in the TextView
        } catch (JSONException e) {
            e.printStackTrace();
//            binding.tvSummary.setText("Error parsing response");
        }
    }
}