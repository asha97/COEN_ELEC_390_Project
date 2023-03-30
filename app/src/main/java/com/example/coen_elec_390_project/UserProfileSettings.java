package com.example.coen_elec_390_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileSettings extends AppCompatActivity {

    Button saveUser;
    EditText user_name, user_date_of_birth, user_location, user_height, user_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);

        saveUser = findViewById(R.id.saveUserProfile);
        user_name = findViewById(R.id.userNameStng);
        user_date_of_birth = findViewById(R.id.userDOBstng);
        user_location = findViewById(R.id.userLocationstng);
        user_height = findViewById(R.id.userHeightstng);
        user_weight = findViewById(R.id.userWeightstng);

        // Fetch the data from Firebase database for the current user
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        userReference.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));

        //save button
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = user_name.getText().toString();
                String dob = user_date_of_birth.getText().toString();
                String location = user_location.getText().toString();
                String height = user_height.getText().toString();
                String weight = user_weight.getText().toString();

                //make sure that the updates happen to the current user!!!
//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                User userUpdate = new User(name, dob, location, height, weight, email);

                userReference.setValue(userUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(UserProfileSettings.this, "User Profile has been saved successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(UserProfileSettings.this, "Error: The user profile cannot get updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // need to change this to update, but need to find code
//                user.writeToFirebase();

                //open directly once done signing up
                openHomePage();
            }
        });
    }

    public void openHomePage() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
        finish();
    }
}
