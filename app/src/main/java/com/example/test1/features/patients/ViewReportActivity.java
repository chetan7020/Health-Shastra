package com.example.test1.features.patients;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.test1.BaseActivity;
import com.example.test1.R;
import com.example.test1.databinding.ActivityCreateReportBinding;
import com.example.test1.databinding.ActivityViewReportBinding;
import com.example.test1.model.ReportModel;
import com.example.test1.utils.Constant;
import com.example.test1.utils.MedicalData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewReportActivity extends BaseActivity {

    private ActivityViewReportBinding binding;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ReportModel report;

    private List<String> blockIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        report = (ReportModel) intent.getSerializableExtra("reportModel");

        blockIDs = new ArrayList<>();

        init();
        setEventListeners();
        setData();
    }

    private void init() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setEventListeners() {
    }

    private void setData() {
        binding.tvDocName.setText(report.getDocName());
        binding.tvDocEmail.setText(report.getDocEmail());

        Constant.showToast(ViewReportActivity.this, report.getBlockID());

        fetchRecord(this, report.getBlockID());
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
                            Constant.showToast(ViewReportActivity.this, recordText);

                            MedicalData recoveredData = MedicalData.fromString(recordText);

                            binding.tvDescription.setText(recoveredData.getDescription());

                            setSymptom(recoveredData);
                            setMedicine(recoveredData);
                            setMeasures(recoveredData);

                            Toast.makeText(context, "Record: " + recordText, Toast.LENGTH_SHORT).show();
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

    private void setSymptom(MedicalData recoveredData) {
        List<String> specializations = recoveredData.getSymptomsList();

        LinearLayout glSpecializations = findViewById(R.id.glSymptoms);
        glSpecializations.removeAllViews(); // Clear existing views if any

        for (String specialization : specializations) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.specialization_normal_layout, glSpecializations, false);

            TextView textView = cardView.findViewById(R.id.tvSpecialization); // Assuming the TextView has this id
            textView.setText(specialization);

            glSpecializations.addView(cardView);
        }
    }

    private List<String> getSymptoms() {
        List<String> symptoms = new ArrayList<>();

        return symptoms;
    }

    private void setMedicine(MedicalData recoveredData) {
        List<String> specializations = recoveredData.getMedicinesList();

        LinearLayout glSpecializations = findViewById(R.id.glMedicines);
        glSpecializations.removeAllViews(); // Clear existing views if any

        for (String specialization : specializations) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.specialization_normal_layout, glSpecializations, false);

            TextView textView = cardView.findViewById(R.id.tvSpecialization); // Assuming the TextView has this id
            textView.setText(specialization);

            glSpecializations.addView(cardView);
        }
    }

    private List<String> getMedicines() {
        List<String> medicines = new ArrayList<>();

        return medicines;

    }

    private void setMeasures(MedicalData recoveredData) {
        List<String> specializations = recoveredData.getMeasuresList();

        LinearLayout glSpecializations = findViewById(R.id.glMeasures);
        glSpecializations.removeAllViews(); // Clear existing views if any

        for (String specialization : specializations) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.specialization_normal_layout, glSpecializations, false);

            TextView textView = cardView.findViewById(R.id.tvSpecialization); // Assuming the TextView has this id
            textView.setText(specialization);

            glSpecializations.addView(cardView);
        }
    }

    private List<String> getMeasures() {
        List<String> measures = new ArrayList<>();

        return measures;
    }


}