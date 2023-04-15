package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

/**
 * activity which acts as a preamble for the sign up page
 */
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

    /**
     * this is going to the sign-up page
     */
    private void goToSignUp(){
        Intent intent = new Intent(this, SignUpPage.class);
        startActivity(intent);
    }
}