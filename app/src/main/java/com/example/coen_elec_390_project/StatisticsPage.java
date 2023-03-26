package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import android.content.Intent;
import java.util.Collections;

public class StatisticsPage extends AppCompatActivity {
    TextView minimum, maximum;
    StatisticsHelper sh;
    //we are going to be storing the information about the altitude in the arraylist
    ArrayList<Double> altitude_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_page);

        // Display the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        minimum = findViewById(R.id.minimumSet);
        maximum = findViewById(R.id.maximumSet);
/*
        //we are going to be initializing the arraylist with the values fetched from the homepage
        Intent intent = getIntent();
        double[] altitudeArray = intent.getDoubleArrayExtra("altitude_history");
        altitude_history = new ArrayList<Double>();
        for (double altitude : altitudeArray) {
            altitude_history.add(altitude);
        }

        double minAltitude = Collections.min(altitude_history);
        minimum.setText(altitude_history.toString());
*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}