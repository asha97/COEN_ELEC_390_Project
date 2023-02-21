package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;


public class HomePage extends AppCompatActivity {

    FirebaseAuth auth;
    Button logoutButton;
    TextView greetingText;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        auth = FirebaseAuth.getInstance();
        greetingText = findViewById(R.id.userDetails);
        logoutButton = findViewById(R.id.logoutBtn);
        user = auth.getCurrentUser(); //initialize the current user

        //if there is no user, then you are going to redirect the user to the sign up page
        if (user == null){
            openLogin();
        } else {
            greetingText.setText("Good day, " + user.getEmail());
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this is going to be signing out the current user
                FirebaseAuth.getInstance().signOut();
                openLogin();
            }

        });



    }

    //open the sign up page
    public void openLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
        finish();
    }
}