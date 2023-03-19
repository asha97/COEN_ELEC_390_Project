package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserProfile extends AppCompatActivity {

    Button healthBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        healthBtn = findViewById(R.id.HealthInfoBtn);

        healthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HealthInfo();
            }
        });
    }

    public void HealthInfo() {
        Intent intent = new Intent(getApplicationContext(), HealthInfo.class);
        startActivity(intent);
    }
}