package com.example.coen_elec_390_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HealthInfo extends AppCompatActivity {

    public TextView bmiDisplay, stepDisplay;
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_info);

        bmiDisplay = findViewById(R.id.bmiText);
        stepDisplay = findViewById(R.id.stepsText);

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get the currently logged-in user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        // Get the user's ID from the currently logged-in user
        userId = mCurrentUser.getUid();

        // Add a ValueEventListener to listen for changes in the user's data
        mDatabase.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the user's height and weight from the snapshot
                String height = dataSnapshot.child("height").getValue(String.class);
                String weight = dataSnapshot.child("weight").getValue(String.class);

                // Display the height and weight in the BMI display TextView
                bmiDisplay.setText("Height: " + height + " cm\nWeight: " + weight + " kg");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
