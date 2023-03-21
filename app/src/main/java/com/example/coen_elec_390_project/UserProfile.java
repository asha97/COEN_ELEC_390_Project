package com.example.coen_elec_390_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class UserProfile extends AppCompatActivity {

    // Declare class variables
    private TextView nameText1;
    private TextView dateText1;
    private TextView locationText1;
    private TextView bmi1;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Display the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get references to the UI elements
        nameText1 = findViewById(R.id.nameText);
        dateText1 = findViewById(R.id.dateText);
        locationText1 = findViewById(R.id.locationText);
        bmi1 = findViewById(R.id.userBMI);

        //authenticate user and get user by ID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Obtain the user's UID
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child(uid); // Reference the "users" node and the specific user's data using their UID
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String dob = dataSnapshot.child("doB").getValue(String.class);
                String location = dataSnapshot.child("location").getValue(String.class);
                int height = dataSnapshot.child("height").getValue(Integer.class);
                double weight = dataSnapshot.child("weight").getValue(Double.class);

                nameText1.setText(name);
                dateText1.setText(dob);
                locationText1.setText(location);

                // Calculate and display BMI
                double bmiValue = weight / Math.pow(height / 100.0, 2);
                bmi1.setText(String.format("%.1f", bmiValue));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });




/*

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String dob = dataSnapshot.child("dob").getValue(String.class);
                        String location = dataSnapshot.child("location").getValue(String.class);
                        int height = dataSnapshot.child("height").getValue(Integer.class);
                        double weight = dataSnapshot.child("weight").getValue(Double.class);

                        nameText1.setText(name);
                        dateText1.setText(dob);
                        locationText1.setText(location);

                        // Calculate and display BMI
                        double bmiValue = weight / Math.pow(height / 100.0, 2);
                        bmi1.setText(String.format("%.1f", bmiValue));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("UserProfile", "Failed to read user data", databaseError.toException());
                }
            });
        }
        else {
            // User is not signed in
            Toast.makeText(this, "Please sign in to view your profile", Toast.LENGTH_SHORT).show();
            finish();
        }


 */

        // Set up the button to allow the user to change their profile information
        Button changeProfileInfo = findViewById(R.id.changeProfileInfo);
        changeProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the EditProfile activity to allow the user to change their profile information
                Intent intent = new Intent(UserProfile.this, UserProfileSettings.class);
                startActivity(intent);
            }
        });
    }

    // Handle the back button press
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
