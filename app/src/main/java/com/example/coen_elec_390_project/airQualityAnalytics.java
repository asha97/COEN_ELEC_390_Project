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
import android.widget.Toast;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_quality_analytics);

        //this is going to be displaying the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listview);
        barChart = findViewById(R.id.bar_chart);

        // fetch data from firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Sensor");

        //--------CREATION OF LIST OF DATA TO DISPLAY---------//
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_data, list);
        listView.setAdapter(adapter);

        //----------------------CREATION OF BARCHART--------------------------//

        // container for data
        BarDataSet dataSet = new BarDataSet(new ArrayList<BarEntry>(), "Sensor Data");

        // colors of chart
        dataSet.setColors(new int[] { R.color.purple_500 });
        dataSet.setValueTextColor(R.color.black);

        // add data of barchart into object
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();

        //display into the box the right metrics
        String [] nameMetric = {"Altitude (m)", "CO2 (ppm)", "Gas (KOhms)", "Humidity (%)", "Pressure (hPa)", "Temperature (*C)", "Elapsed Time (ms)", "tVOC (g*m^-3)" };

        // Attach a ValueEventListener to the Firebase database node to get the data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // clear data to make sure there isnt anything else
                dataSet.clear();
                list.clear();

                // looping in data
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    float value = Float.parseFloat(snapshot.getValue().toString());
                    //not going to be displaying the elapsed time
                    if (i !=6) {
                        dataSet.addEntry(new BarEntry(i, value, nameMetric[i]));
                        String addData = nameMetric[i] + ": " + snapshot.getValue().toString();
                        if (nameMetric[i].equals("Temperature (*C)")){
                            String addFahrenheit = "Temperature (*F): " + convertToFahrenheit(snapshot.getValue().toString());
                            list.add(addFahrenheit);
                        }
                        if (nameMetric[i].equals("Pressure (hPa)")){
                            String addBar = "Pressure (Bar): " + convertToBar(snapshot.getValue().toString());
                            list.add(addBar);
                        }
                        //we want to make sure that the air quality particles are not surpassing the threshold
                        if (nameMetric[i].equals("CO2 (ppm)")){
                            int co2PPM = Integer.parseInt(snapshot.getValue().toString());

                            if (co2PPM >= 1000){
                                Toast.makeText(airQualityAnalytics.this, "CO2 particle is above 1000ppm, be careful!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (nameMetric[i].equals("Gas (KOhms)")){
                            double gasMetric = Double.parseDouble(snapshot.getValue().toString());

                            if (gasMetric >= 100){ // need to change this value for the right gas
                                Toast.makeText(airQualityAnalytics.this, "TVOC level is too high, be careful!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        list.add(addData);
                        i++;
                    }
                }

                // notification that data in chart has been updated
                barData.notifyDataChanged();
                barChart.notifyDataSetChanged();
                barChart.invalidate();
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //go back to main activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String convertToFahrenheit(String value){
        double celsiusValue = Double.parseDouble(value);
        return String.valueOf((celsiusValue*1.80000)+32.00);
    }

    private String convertToBar(String value){
        double hpaValue = Double.parseDouble(value);
        return String.valueOf((hpaValue * 0.001));
    }
}
