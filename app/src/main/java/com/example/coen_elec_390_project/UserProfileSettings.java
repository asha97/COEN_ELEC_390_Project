package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

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
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                User user = new User(name, dob, location, height, weight, uid, email);

                // need to change this to update, but need to find code
                user.writeToFirebase();

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
