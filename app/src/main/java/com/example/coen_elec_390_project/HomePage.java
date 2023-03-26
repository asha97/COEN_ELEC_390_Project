package com.example.coen_elec_390_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    FirebaseAuth auth;
    Button logoutButton,startStopButton, notifTest;
    ImageView generalSettings, airQualityBtn, userProfileGo, medicationButton, statsButton;
    TextView greetingText;
    FirebaseUser user;
    Stopwatch stopwatch;
    long counter = 0;
    DatabaseReference reference;
    ArrayList<Float> temperature_history = new ArrayList<>();
    ArrayList<Float> humidity_history = new ArrayList<>();
    ArrayList<Float> altitude_history = new ArrayList<>();
    ArrayList<Float> co2_history = new ArrayList<>();
    ArrayList<Float> gas_history = new ArrayList<>();
    ArrayList<Float> pressure_history = new ArrayList<>();
    ArrayList<Float> tVOC_history = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        stopwatch = new Stopwatch();
        startStopButton = findViewById(R.id.start_stop_button);
        logoutButton = findViewById(R.id.logoutBtn);
        generalSettings = findViewById(R.id.settingButton);
        airQualityBtn = findViewById(R.id.airQualityData);
        userProfileGo = findViewById(R.id.userProfileAccess);
        medicationButton = findViewById(R.id.logMedication);
        statsButton = findViewById(R.id.statsIcon);
        notifTest = findViewById(R.id.notifTest);

        auth = FirebaseAuth.getInstance();
        greetingText = findViewById(R.id.userDetails);
        user = auth.getCurrentUser(); //initialize the current user
        reference = FirebaseDatabase.getInstance().getReference().child("Sensor");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> arrayList_result = new ArrayList<String>();
                Iterable<DataSnapshot> it_list = dataSnapshot.getChildren();
                for (DataSnapshot snapshot:it_list) {
                    arrayList_result.add(snapshot.getValue().toString());
                }
                if (!(counter%2==0)){
                    //System.out.println("Adding data");
                    altitude_history.add(Float.parseFloat(arrayList_result.get(0)));
                    co2_history.add(Float.parseFloat(arrayList_result.get(1)));
                    gas_history.add(Float.parseFloat(arrayList_result.get(2)));
                    humidity_history.add(Float.parseFloat(arrayList_result.get(3)));
                    pressure_history.add(Float.parseFloat(arrayList_result.get(4)));
                    temperature_history.add(Float.parseFloat(arrayList_result.get(5)));
                    tVOC_history.add(Float.parseFloat(arrayList_result.get(7)));
                }
                else {
                    // do not collect data
                }

                //System.out.println("*******ALTITUDE**********");
                //System.out.println(altitude_history.get(0));
                //System.out.println("*******CO2**********");
                //System.out.println(co2_history.get(0));
                //System.out.println("*******GAS**********");
                //System.out.println(gas_history.get(0));
                //System.out.println("*******HUMIDITY**********");
                //System.out.println(humidity_history.get(0));
                //System.out.println("*******PRESSURE**********");
                //System.out.println(pressure_history.get(0));
                //if(!(temperature_history.isEmpty())) {
                    //System.out.println("*******TEMPERATURE**********");
                    //System.out.println(temperature_history.get(temperature_history.size()-1));
                //}
                //System.out.println("*******TVOC**********");
                //System.out.println(tVOC_history.get(0));
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

        medicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMedication();
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

        notifTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNotif();
            }
        });


        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter%2 == 0){
                    stopwatch.start();
                    startStopButton.setText(R.string.Stop_stopwatch);
                    startStopButton.setBackgroundColor(Color.RED); // set the background color to red
                    counter++;
                }
                else{
                    stopwatch.stop();
                    startStopButton.setText(R.string.Start_stopwatch);
                    startStopButton.setBackgroundColor(Color.BLUE); // set the background color to green
                    counter++;
                    //System.out.println(stopwatch.getElapsedTime()); //This is for developer testing
                    Toast toast = Toast.makeText(getApplicationContext(), stopwatch.getElapsedTime(), Toast.LENGTH_LONG); // This is for demo purposes
                    toast.show();
                }
            }
        });
    }
    public void openNotif() {
        Intent intent = new Intent(getApplicationContext(), TestNotifActivity.class);
        startActivity(intent);
    }
    public void openStatistics() {
        Intent intent = new Intent(getApplicationContext(), StatisticsPage.class);
    /*
        intent.putExtra("altitude_history", altitude_history);
        intent.putExtra("temperature_history", temperature_history);
        intent.putExtra("humidity_history", humidity_history);
        intent.putExtra("co2_history", co2_history);
        intent.putExtra("gas_history", gas_history);
        intent.putExtra("pressure_history", pressure_history);
*/
        startActivity(intent);
    }
    public void openMedication() {
        Intent intent = new Intent(getApplicationContext(), MedicationLogger.class);
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