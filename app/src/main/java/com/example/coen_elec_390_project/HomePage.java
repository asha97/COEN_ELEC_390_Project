package com.example.coen_elec_390_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import android.app.AlertDialog;
import android.content.DialogInterface;


public class HomePage extends AppCompatActivity {
    FirebaseAuth auth;
    Button logoutButton,startStopButton, notifTest;
    ImageView generalSettings, airQualityBtn, userProfileGo, statsButton;
    TextView greetingText;
    FirebaseUser user;
    TextView stopwatch_tv;
    long counter = 0;
    DatabaseReference reference;
    private int seconds = 0;
    private boolean functioning;
    private boolean wasFunctioning;
    String timeElapsed;
    Handler handler;
    private LineChart lineChart;
    ArrayList<Entry> temperature_data;
    ArrayList<Entry> pressure_data;

    StatisticsHelper globalHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        startStopButton = findViewById(R.id.start_stop_button);
        logoutButton = findViewById(R.id.logoutBtn);
        generalSettings = findViewById(R.id.settingButton);
        airQualityBtn = findViewById(R.id.airQualityData);
        userProfileGo = findViewById(R.id.userProfileAccess);
        statsButton = findViewById(R.id.statsIcon);
        stopwatch_tv = findViewById(R.id.stopwatch_tv);
        greetingText = findViewById(R.id.userDetails);
        lineChart = findViewById(R.id.chart);

        ArrayList<Float> temperature_history = new ArrayList<>();
        ArrayList<Float> humidity_history = new ArrayList<>();
        ArrayList<Float> altitude_history = new ArrayList<>();
        ArrayList<Float> co2_history = new ArrayList<>();
        ArrayList<Float> gas_history = new ArrayList<>();
        ArrayList<Float> pressure_history = new ArrayList<>();
        ArrayList<Float> tVOC_history = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser(); //initialize the current user
        reference = FirebaseDatabase.getInstance().getReference().child("Sensor");

        handler = new Handler();
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> arrayList_result = new ArrayList<String>();
                Iterable<DataSnapshot> it_list = dataSnapshot.getChildren();
                temperature_data = new ArrayList<>();
                pressure_data = new ArrayList<>();

                float i = 0;
                int j = 0;
                for (DataSnapshot snapshot:it_list) {
                    i++;
                    arrayList_result.add(snapshot.getValue().toString());
                    if(j == 4){
                        pressure_data.add(new Entry(i,Float.parseFloat(snapshot.getValue().toString())));
                    }

                    if(j == 5){
                        temperature_data.add(new Entry(i,Float.parseFloat(snapshot.getValue().toString())));
                    }
                    j++;
                }
                j=0;

                if(temperature_history.isEmpty() || pressure_history.isEmpty()) {
                    lineChart.setVisibility(View.GONE);
                }
                else {
                    //temperature line
                    for(int z = 0; z < temperature_history.size(); z++) {
                        temperature_data.add(new Entry(z+1, temperature_history.get(z)));
                    }
                    final LineDataSet ldSet = new LineDataSet(temperature_data, "Temperature");
                    ldSet.setColor(Color.RED);

                    //pressure line
                    for(int z = 0; z < pressure_history.size(); z++) {
                        pressure_data.add(new Entry(z+1, pressure_history.get(z)));
                    }
                    final LineDataSet pressureSet = new LineDataSet(pressure_data, "Pressure");
                    ldSet.setColor(Color.BLACK);

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(ldSet);
                    dataSets.add(pressureSet);

                    LineData lineData = new LineData(dataSets);

                    lineChart.setData(lineData);

                    //change color of the background of the barchart
                    int customChartBg = Color.rgb(222,229,255);
                    lineChart.setBackgroundColor(customChartBg);

                    lineChart.notifyDataSetChanged();
                    lineChart.setVisibility(View.VISIBLE);
                    lineChart.invalidate();
                }

                if (!(counter%2==0)){
                    altitude_history.add(Float.parseFloat(arrayList_result.get(0)));
                    co2_history.add(Float.parseFloat(arrayList_result.get(1)));
                    gas_history.add(Float.parseFloat(arrayList_result.get(2)));
                    humidity_history.add(Float.parseFloat(arrayList_result.get(3)));
                    pressure_history.add(Float.parseFloat(arrayList_result.get(4)));
                    temperature_history.add(Float.parseFloat(arrayList_result.get(5)));
                    tVOC_history.add(Float.parseFloat(arrayList_result.get(7)));
                }
                 if(counter%2==0 && counter!=0) {
                    StatisticsHelper statisticsHelper = new StatisticsHelper(altitude_history,humidity_history,temperature_history,co2_history,gas_history,pressure_history,tVOC_history);
                    globalHelper = statisticsHelper;
                     altitude_history.clear();
                     humidity_history.clear();
                     temperature_history.clear();
                     co2_history.clear();
                     gas_history.clear();
                     pressure_history.clear();
                     tVOC_history.clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (user == null){
            openLogin();
        } else {
            greetingText.setText("Good day, " + user.getEmail());
            String uid = user.getUid();

            // Retrieve the user's information from Firebase
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child("id");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Set the text of the TextViews to the user's name and dob
                        /*String name = snapshot.child("name").getValue(String.class);
                        String dob = snapshot.child("dob").getValue(String.class);
                        nameText.setText(name);
                        dobText.setText(dob);

                         */
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
                }
            });
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this is going to be signing out the current user
                FirebaseAuth.getInstance().signOut();
                openLogin();
            }

        });


        generalSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileSettings();
            }

        });

        airQualityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAirQuality();
            }

        });

        userProfileGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserProfile();
            }
         });

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStatistics();
            }
        });

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter >= 1) {
                    openStatistics();
                } else {
                    //Toast.makeText(HomePage.this, "You cannot access the statistics page yet. In order to access the statistics page, you must start the stopwatch.", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
                    builder.setTitle("Access to Statistics Analysis Denied");
                    builder.setMessage("In order to access the Statistics Analysis page, you must start the stopwatch, and stop it after the desired amount of time.");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


        startStopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(counter%2 == 0){
                    if (counter==0) { // This is for the first time starting the stopwatch
                        runStopwatch();
                        functioning = true;
                        startStopButton.setText(R.string.Stop_stopwatch);
                        startStopButton.setBackgroundColor(Color.RED); // set the background color to red
                        counter++;
                    }
                    else{ // This is for all the subsequent times starting the stopwatch
                        runStopwatch();
                        functioning = true;
                        seconds = 0;
                        startStopButton.setText(R.string.Stop_stopwatch);
                        startStopButton.setBackgroundColor(Color.RED); // set the background color to red
                        counter++;
                    }
                }
                else{
                    startStopButton.setText(R.string.Start_stopwatch);
                    functioning = false;
                    handler.removeCallbacks(runnable);
                    timeElapsed = (String)stopwatch_tv.getText();
                    startStopButton.setBackgroundColor(Color.BLUE); // set the background color to green
                    counter++;
                }
            }
        });
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("functioning", functioning);
        outState.putBoolean("wasFunctioning", wasFunctioning);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasFunctioning = functioning;
        functioning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasFunctioning){
            functioning = true;
        }
    }

    private void runStopwatch(){
        handler.postDelayed(runnable,0);
    }

    public Runnable runnable = new Runnable() {
        //final TextView STOPWATCH_TV = (TextView) findViewById(R.id.stopwatch_tv);
        @Override
        public void run() {
            int sec = seconds%60;
            int min = (seconds%3600)/60;
            int hrs = seconds/3600;

            String timeString = String.format(Locale.getDefault(), "%d:%02d:%02d", hrs, min, sec);
            stopwatch_tv.setText(timeString);
            if (functioning){
                seconds=seconds+1;
            }
            handler.postDelayed(this,1000);
        }
    };



    public void openStatistics() {
        //TODO: @Asha I want the users to not be able to open up the statistics page until after they've captured a certain interval of time with the stopwatch. We can make the statistics button unclickable until after they stop the timer for the 1st time. We can make it change color.
        Intent intent = new Intent(getApplicationContext(), StatisticsPage.class);
        intent.putExtra("StatisticsHelper", globalHelper);
        startActivity(intent);
    }
    public void openLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
        finish();
    }
    public void openProfileSettings() {
        Intent intent = new Intent(getApplicationContext(), UserProfileSettings.class);
        startActivity(intent);
    }
    public void openAirQuality() {
        Intent intent = new Intent(getApplicationContext(), airQualityAnalytics.class);
        startActivity(intent);
    }
    public void openUserProfile() {
        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
        startActivity(intent);
    }

}