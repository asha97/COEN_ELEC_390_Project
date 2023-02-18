package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.text.TextUtils;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ProgressBar;
import com.google.firebase.auth.FirebaseUser;


public class SignUpPage extends AppCompatActivity {

    EditText editTextName, editTextDOB, editTextCity, editTextEmail, editTextPassword;
    Button regButton;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;


    //check if the person is already logged in

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //if they are already logged in, we are going to be opening the main activity
            openHomePage();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        mAuth = FirebaseAuth.getInstance();

        /*
        editTextName = findViewById(R.id.userNameEnter);
        editTextDOB = findViewById(R.id.userDOB);
        editTextCity = findViewById(R.id.userCity);
        */

        editTextEmail = findViewById(R.id.userEmail);
        editTextPassword = findViewById(R.id.userPassword);
        regButton = findViewById(R.id.saveSignUp);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
                finish();

            }

        });


        regButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //we want to see the progression of the sign up
                progressBar.setVisibility(View.VISIBLE);

                String name, dob, city, email, password;
                name = String.valueOf(editTextName.getText());
                dob = String.valueOf(editTextDOB.getText());
                city = String.valueOf(editTextCity.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                //check if the email or password fields are empty or not
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUpPage.this, "Please enter a valid e-mail", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpPage.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create the user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //set the visibility of the progress bar to gone
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // If sign in succeeds, display a message to the user.
                                    Toast.makeText(SignUpPage.this, "Sign up successful.",
                                            Toast.LENGTH_SHORT).show();
                                    openLogin();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUpPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    //open the home page
    public void openHomePage() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
        finish();
    }

    public void openLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
        finish();
    }
}