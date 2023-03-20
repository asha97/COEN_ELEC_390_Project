package com.example.coen_elec_390_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
    private TextView nameText;
    private TextView dateText;
    private TextView locationText;
    private DatabaseReference userRef;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Display the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get references to the UI elements
        nameText = findViewById(R.id.nameText);
        dateText = findViewById(R.id.dateText);
        locationText = findViewById(R.id.locationText);

        // Get the current user's ID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        // Get a reference to the user's data in the database
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        // Retrieve the user's data from the database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get the user's data from the snapshot
                    String name = snapshot.child("name").getValue().toString();
                    String dateOfBirth = snapshot.child("dob").getValue().toString();
                    String location = snapshot.child("location").getValue().toString();
                    // Display the user's data in the UI
                    nameText.setText(name);
                    dateText.setText(dateOfBirth);
                    locationText.setText(location);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("UserProfile", "loadUser:onCancelled", error.toException());
            }
        });

        // Set the user ID based on the number of existing users in the database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Set the user ID to the number of existing users plus one
                userId = (int) snapshot.getChildrenCount() + 1;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("UserProfile", "getUserCount:onCancelled", error.toException());
            }
        });

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
