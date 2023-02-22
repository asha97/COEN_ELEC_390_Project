package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class UserProfileSettings extends AppCompatActivity {
    //this class is going to be used in order to update the information about the user
    Button saveUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);
        saveUser = findViewById(R.id.saveUserProfile);

        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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