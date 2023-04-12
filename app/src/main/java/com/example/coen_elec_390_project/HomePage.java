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

/**
 * This class controls the backend of the Homepage
 * Notable features here are: the linegraph, the stopwatch, and the various button to take us to others activities (profile, profile settings, statistics, analysis)
 * @author David Molina (40111257), Asha Islam (), Pavithra Sivagnanasuntharam()
 */
public class HomePage extends AppCompatActivity {

    FirebaseAuth auth;
    Button logoutButton,startStopButton, notifTest;
    ImageView generalSettings, airQualityBtn, userProfileGo, medicationButton, statsButton;
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
    ArrayList<Entry> co2_data;
    ArrayList<Entry> gas_data;
    ArrayList<Entry> humidity_data;
    ArrayList<Entry> pressure_data;
    ArrayList<Entry> temperature_data;
    ArrayList<Entry> tVOC_data;

    StatisticsHelper globalHelper;

    /**
     * The OnCreate method is where every component of the page is initialized and operated from
     * @param savedInstanceState
     */
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
        ArrayList<Float> co2_history_chart = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser(); //initialize the current user
        reference = FirebaseDatabase.getInstance().getReference().child("Sensor");

        handler = new Handler();
        reference.addValueEventListener(new ValueEventListener() {

            /**
             * On the homepage, we require operations to be done at every data change on the Firebase Database.
             * More specifically, the linegraph needs to be updated at every data change, and the statistics array (history arrays) need to be populated
             * @param dataSnapshot The snapshot of the Firebase Database that is taken at every data change
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> arrayList_result = new ArrayList<String>();
                Iterable<DataSnapshot> it_list = dataSnapshot.getChildren();
                co2_data = new ArrayList<>();
                gas_data = new ArrayList<>();
                humidity_data = new ArrayList<>();
                pressure_data = new ArrayList<>();
                temperature_data = new ArrayList<>();
                tVOC_data = new ArrayList<>();

                float i = 0;
                int j = 0;
                for (DataSnapshot snapshot:it_list) {
                    i++;
                    arrayList_result.add(snapshot.getValue().toString());
                    if(j == 1){
                        float rawCO2 = Float.parseFloat(snapshot.getValue().toString());
                        double changedUnitCO2 = rawCO2 * 0.01;
                        co2_data.add(new Entry(i, (float) changedUnitCO2));
                    }
                    if(j == 2){
                        gas_data.add(new Entry(i,Float.parseFloat(snapshot.getValue().toString())));
                    }
                    if(j == 3){
                        humidity_data.add(new Entry(i,Float.parseFloat(snapshot.getValue().toString())));
                    }
                    if(j == 4){
                        float rawPressure = Float.parseFloat(snapshot.getValue().toString());
                        double changedUnitPressure = rawPressure * 0.001;
                        pressure_data.add(new Entry(i, (float) changedUnitPressure));
                    }
                    if(j == 5){
                        temperature_data.add(new Entry(i,Float.parseFloat(snapshot.getValue().toString())));
                    }
                    if(j == 7){
                        tVOC_data.add(new Entry(i,Float.parseFloat(snapshot.getValue().toString())));
                    }
                    j++;
                }
                j=0;

                if(co2_history.isEmpty() || gas_history.isEmpty() || humidity_history.isEmpty() || pressure_history.isEmpty() || temperature_history.isEmpty() || tVOC_history.isEmpty()) {
                    lineChart.setVisibility(View.GONE);
                }
                else {
                    //CO2 line
                    for(int z = 0; z < co2_history_chart.size(); z++) {
                        co2_data.add(new Entry(z+1, co2_history_chart.get(z)));
                    }
                    final LineDataSet co2Set = new LineDataSet(co2_data, "CO2");
                    co2Set.setColor(Color.GREEN);

                    //humidity line
                    for(int z = 0; z < humidity_history.size(); z++) {
                        humidity_data.add(new Entry(z+1, humidity_history.get(z)));
                    }
                    final LineDataSet humiditySet = new LineDataSet(humidity_data, "Humidity");
                    humiditySet.setColor(Color.BLACK);

                    //temperature line
                    for(int z = 0; z < temperature_history.size(); z++) {
                        temperature_data.add(new Entry(z+1, temperature_history.get(z)));
                    }
                    final LineDataSet temperatureSet = new LineDataSet(temperature_data, "Temp");
                    temperatureSet.setColor(Color.RED);

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(co2Set);
                    dataSets.add(humiditySet);
                    dataSets.add(temperatureSet);

                    LineData lineData = new LineData(dataSets);

                    lineChart.setData(lineData);
                    lineChart.notifyDataSetChanged();
                    //TODO: make the line appear
                    lineChart.setVisibility(View.VISIBLE);
                    lineChart.invalidate();
                }

                if (!(counter%2==0)){
                    altitude_history.add(Float.parseFloat(arrayList_result.get(0)));
                    co2_history.add((float) (Float.parseFloat(arrayList_result.get(1))));
                    co2_history_chart.add((float) (Float.parseFloat(arrayList_result.get(1)) * 0.01));
                    gas_history.add(Float.parseFloat(arrayList_result.get(2)));
                    humidity_history.add(Float.parseFloat(arrayList_result.get(3)));
                    pressure_history.add(Float.parseFloat(arrayList_result.get(4)));
                    temperature_history.add(Float.parseFloat(arrayList_result.get(5)));
                    tVOC_history.add(Float.parseFloat(arrayList_result.get(7)));
                }
                 if(counter%2==0 && counter!=0) {
                    StatisticsHelper statisticsHelper = new StatisticsHelper(altitude_history,humidity_history,temperature_history,co2_history,gas_history,pressure_history,tVOC_history);
                    globalHelper = statisticsHelper;
                }
            }

            /**
             * This methods dictates what happens once there is a data base error
             * @param error the database error
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (user == null){
            openLogin();
        } else {
            greetingText.setText("Good day, " + user.getEmail());
            String uid = user.getUid();

            // TODO: @Pavi @Asha, do we need this method?
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
            /**
             * The logout onClick method
             * Logs out a user when pressing the logout button
             * @param view the view where the button is pressed
             */
            @Override
            public void onClick(View view) {
                //this is going to be signing out the current user
                FirebaseAuth.getInstance().signOut();
                openLogin();
            }

        });


        generalSettings.setOnClickListener(new View.OnClickListener() {
            /**
             * The profile settings button onClick method
             * takes a user to the profile settings page once button is clicked
             * @param view the view where the button is pressed
             */
            @Override
            public void onClick(View view) {
                openProfileSettings();
            }

        });

        airQualityBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * The AirQualityAnalytics button onClick method
             * takes a user to the analysis page once button is clicked
             * @param view the view where the button is pressed
             */
            @Override
            public void onClick(View view) {
                openAirQuality();
            }

        });



        userProfileGo.setOnClickListener(new View.OnClickListener() {
            /**
             * The profile button onClick method
             * takes a user to the profile page once button is clicked
             * @param view the view where the button is pressed
             */
            @Override
            public void onClick(View view) {
                openUserProfile();
            }
         });

        statsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * The statistics button onClick method
             * takes a user to the statistics page once button is clicked
             * @param view the view where the button is pressed
             */
            @Override
            public void onClick(View view) {
                openStatistics();
            }
        });
        startStopButton.setOnClickListener(new View.OnClickListener() {

            /**
             * The "Start/Stop" button onClick method
             * Changes the behavior of the "start/stop" button each time it is pressed
             * If the counter is even, it starts the stopwatch, if the counter is odd, it stops the stopwatch
             * Changes the text displayed on the button in consequence ("start" or "Stop")
             * @param view the view where the button is pressed
             */
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


    /**
     * The onSaveInstanceState method
     * Saves the stopwatch seconds, minutes and hours
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("functioning", functioning);
        outState.putBoolean("wasFunctioning", wasFunctioning);
    }

    /**
     * the onPause method
     * Every time the activity is paused, the stopwatch's "WasFunctioning" boolean becomes true and the "functioning" boolean becomes false.
     */
    @Override
    protected void onPause() {
        super.onPause();
        wasFunctioning = functioning;
        functioning = false;
    }

    /**
     * the onResume method
     * Every time the activity is resumed, the stopwatch resumes its functioning and its "functioning" boolean becomes true, if it was false before.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (wasFunctioning){
            functioning = true;
        }
    }

    /**
     * This methods starts the stopwatch
     */
    private void runStopwatch(){
        handler.postDelayed(runnable,0);
    }

    /**
     * This runnable is responsible for the operation of the stopwatch: calculating seconds, minutes and hours and displaying them
     * It is also responsible for incrementing the seconds variable each second
     */
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


    /**
     * This methods creates an Intent to open the statistics page every time the user presses the statistics button
     */
    public void openStatistics() {
        Intent intent = new Intent(getApplicationContext(), StatisticsPage.class);
        intent.putExtra("StatisticsHelper", globalHelper);
        startActivity(intent);
    }

    /**
     * This methods creates an Intent to open the login page every time the user presses the login button
     */
    public void openLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
        finish();
    }

    /**
     * This methods creates an Intent to open the profile settings page every time the user presses the profile settings button
     */
    public void openProfileSettings() {
        Intent intent = new Intent(getApplicationContext(), UserProfileSettings.class);
        startActivity(intent);
    }

    /**
     * This methods creates an Intent to open the air quality analytics page every time the user presses the air quality analytics button
     */
    public void openAirQuality() {
        Intent intent = new Intent(getApplicationContext(), airQualityAnalytics.class);
        startActivity(intent);
    }

    /**
     * This methods creates an Intent to open the user profile page every time the user presses the user profile button
     */
    public void openUserProfile() {
        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
        startActivity(intent);
    }

}