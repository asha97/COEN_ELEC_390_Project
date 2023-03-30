package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class SignUpInfo extends AppCompatActivity {

    Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_info);
        signUp = findViewById(R.id.buttonSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void onClick(View v){
                goToSignUp();
            }
        });
    }

    private void goToSignUp(){
        Intent intent = new Intent(this, SignUpPage.class);
        startActivity(intent);
    }
}