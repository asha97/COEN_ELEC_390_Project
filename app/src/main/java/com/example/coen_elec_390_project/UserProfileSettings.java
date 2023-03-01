package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileSettings extends AppCompatActivity {
    //this class is going to be used in order to update the information about the user
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
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = user_name.getText().toString();
                String dob = user_date_of_birth.getText().toString();
                String location = user_location.getText().toString();
                float height = Float.parseFloat(user_height.getText().toString());
                float weight = Float.parseFloat(user_weight.getText().toString());

                //setting the user in a user object
                User user = new User(name, dob, location, height, weight);
                //this is going to be storing the data from the user sign into firebase
                user.writeToFirebase();
                //opening the home page
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