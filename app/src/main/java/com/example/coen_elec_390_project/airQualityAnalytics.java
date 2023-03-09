package com.example.coen_elec_390_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import java.sql.Array;
import java.util.ArrayList;
public class airQualityAnalytics extends AppCompatActivity {
    private ListView listView;
    private Button saveData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_quality_analytics);

        //this is going to be displaying the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listview);
        saveData = findViewById(R.id.saveDataButton);

        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_data, list);
        listView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("BME680_Sensor");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String addData = "Metric #" + i + ": " + snapshot.getValue().toString();
                    list.add(addData);
                    i++;
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //only doing for tvoc for now!
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the selected metric
                String selectedMetric = (String) adapterView.getItemAtPosition(position);
                startTVOC_Activity(selectedMetric);
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

    public void startTVOC_Activity(String tVOC){
        //intent to start the tVOC activity
        Intent intent = new Intent(airQualityAnalytics.this, tVOC_Activity.class);
        intent.putExtra("metric_tvoc", tVOC);
        startActivity(intent);
    }
}