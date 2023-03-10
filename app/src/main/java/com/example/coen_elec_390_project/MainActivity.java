package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    Button signUpBtn, loginBtn, homePageBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpBtn = findViewById(R.id.signUpButtonHome);
        loginBtn = findViewById(R.id.loginButtonHome);
        homePageBtn = findViewById(R.id.homePageBtn);
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

        homePageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public  void onClick(View v){
                goToHome();
            }
        });

    }

    private void goToHome(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
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