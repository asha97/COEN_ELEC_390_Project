package com.example.coen_elec_390_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private TextView nameText;
    private TextView dateText;
    private TextView locationText;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        nameText = findViewById(R.id.nameText);
        dateText = findViewById(R.id.dateText);
        locationText = findViewById(R.id.locationText);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    String dateOfBirth = snapshot.child("dob").getValue().toString();
                    String location = snapshot.child("location").getValue().toString();

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
}
