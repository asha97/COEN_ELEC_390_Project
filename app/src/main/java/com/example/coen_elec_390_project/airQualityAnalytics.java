package com.example.coen_elec_390_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.MenuItem;
import android.widget.Button;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;


public class airQualityAnalytics extends AppCompatActivity {
    private BarChart barChart;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_quality_analytics);

        // barchart from xml
        barChart = findViewById(R.id.bar_chart);

        // fetch data from firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Sensor");

        // container for data
        BarDataSet dataSet = new BarDataSet(new ArrayList<BarEntry>(), "Sensor Data");

        // colors of chart
        dataSet.setColors(new int[] { R.color.purple_500 });
        dataSet.setValueTextColor(R.color.black);

        // add data of barchart into object
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();

        // Attach a ValueEventListener to the Firebase database node to get the data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // clear data to make sure there isnt anything else
                dataSet.clear();

                // looping in data
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    float value = Float.parseFloat(snapshot.getValue().toString());
                    dataSet.addEntry(new BarEntry(i, value));
                    i++;
                }

                // notification that data in chart has been updated
                barData.notifyDataChanged();
                barChart.notifyDataSetChanged();
                barChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
