package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

/**
 * this is the main page, the user is going to be landing on this page when they first open the application
 * the user is going to be prompted to either the login or sign-up page
 */
public class MainActivity extends AppCompatActivity {
    Button signUpBtn, loginBtn, homePageBtn;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * the user could pick to go to sign-up or login
         */
        signUpBtn = findViewById(R.id.signUpButtonHome);
        loginBtn = findViewById(R.id.loginButtonHome);

        //going to the specific pages
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
        Intent intent = new Intent(this, SignUpInfo.class);
        startActivity(intent);
    }
}