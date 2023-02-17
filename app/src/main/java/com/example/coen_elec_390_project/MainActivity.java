package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    Button signUpBtn, loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpBtn = findViewById(R.id.signUpButtonMain);
        loginBtn = findViewById(R.id.loginButtonHome);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                goToSignUp();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v){
                goToLogin();
            }
        });
    }

    private void goToLogin(){
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

    private void goToSignUp(){
        Intent intent = new Intent(this, SignUpPage.class);
        startActivity(intent);
    }
}