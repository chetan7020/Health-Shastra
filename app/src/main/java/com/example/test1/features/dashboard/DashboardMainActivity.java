package com.example.test1.features.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.test1.BaseActivity;
import com.example.test1.databinding.ActivityDashboardMainBinding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DashboardMainActivity extends BaseActivity {

    private ActivityDashboardMainBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setEventListeners();

        BarDataSet barDataSetPatientGrowth = getDataSetPatientGrowthChart("Patient Growth");
        BarData data = new BarData(barDataSetPatientGrowth); // Set only BarDataSet
        binding.PatientGrowthChart.setData(data);
        binding.PatientGrowthChart.animateXY(2000, 2000);
        binding.PatientGrowthChart.invalidate();

        BarDataSet barDataSetDistribution = getDataSetDistribution("Disease Distribution");
        BarData dataDistribution = new BarData(barDataSetDistribution); // Set only BarDataSet
        binding.DistributionChart.setData(dataDistribution);
        binding.DistributionChart.animateXY(2000, 2000);
        binding.DistributionChart.invalidate();

    }

    private void init() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setEventListeners() {
    }

    // Fixing BarEntry parameters and returning a BarDataSet directly
    private BarDataSet getDataSetPatientGrowthChart(String name) {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry(0, 110.0f)); // Jan
        valueSet1.add(new BarEntry(1, 40.0f));  // Feb
        valueSet1.add(new BarEntry(2, 60.0f));  // Mar
        valueSet1.add(new BarEntry(3, 30.0f));  // Apr
        valueSet1.add(new BarEntry(4, 90.0f));  // May
        valueSet1.add(new BarEntry(5, 100.0f)); // Jun
        valueSet1.add(new BarEntry(6, 110.0f)); // Jul
        valueSet1.add(new BarEntry(7, 40.0f));  // Aug
        valueSet1.add(new BarEntry(8, 60.0f));  // Sep
        valueSet1.add(new BarEntry(9, 30.0f));  // Oct
        valueSet1.add(new BarEntry(10, 90.0f)); // Nov
        valueSet1.add(new BarEntry(11, 100.0f)); // Dec

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, name);
        barDataSet1.setColor(Color.rgb(0, 103, 255));
        return barDataSet1;
    }

    private BarDataSet getDataSetDistribution(String name) {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry(0, 40.0f)); // Jan
        valueSet1.add(new BarEntry(1, 70.0f));  // Feb
        valueSet1.add(new BarEntry(2, 60.0f));  // Mar
        valueSet1.add(new BarEntry(3, 30.0f));  // Apr
        valueSet1.add(new BarEntry(4, 90.0f));  // May
        valueSet1.add(new BarEntry(5, 100.0f)); // Jun
        valueSet1.add(new BarEntry(6, 110.0f)); // Jul
        valueSet1.add(new BarEntry(7, 70.0f));  // Aug
        valueSet1.add(new BarEntry(8, 60.0f));  // Sep
        valueSet1.add(new BarEntry(9, 30.0f));  // Oct
        valueSet1.add(new BarEntry(10, 30.0f)); // Nov
        valueSet1.add(new BarEntry(11, 30.0f)); // Dec

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, name);
        barDataSet1.setColor(Color.rgb(0, 103, 255));
        return barDataSet1;
    }

}
