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
import com.google.firebase.database.FirebaseDatabase;

/**
 * this is a sign up page where the user is going to be entering their information
 * and the information is going to be stored into the firebase
 *  @author David Molina (40111257), Asha Islam (40051511), Pavithra Sivagnanasuntharam(40117356)
 */

public class SignUpPage extends AppCompatActivity {
    EditText editTextEmail, editTextPassword, editName, editDOB, editLocation, editWeight, editHeight;
    Button regButton, clickingTheLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    //check if the person is already logged in

    /**
     * this is going to prompt the user to the home page
     */
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

        editTextEmail = findViewById(R.id.userEmail);
        editTextPassword = findViewById(R.id.userPassword);
        editName = findViewById(R.id.userName);
        editDOB= findViewById(R.id.userDOB);
        editLocation = findViewById(R.id.userLocation);
        editHeight = findViewById(R.id.userHeight);
        editWeight = findViewById(R.id.userWeight);

        regButton = findViewById(R.id.saveSignUp);
        progressBar = findViewById(R.id.progressBar);
        clickingTheLogin = findViewById(R.id.clickLogin);

        /**
         * this is for the login button
         */
        clickingTheLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
                finish();
            }

        });

        /**
         * this is for the registration button
         */
        regButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Registration onClick button
             * Registers a user
             * @param view the view
             */
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                String email, password, name, dob, location, weight, height;
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                name = editName.getText().toString();
                dob = editDOB.getText().toString();
                location = editLocation.getText().toString();
                weight = editWeight.getText().toString();
                height = editHeight.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUpPage.this, "Please enter a valid e-mail", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpPage.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(name)){
                    Toast.makeText(SignUpPage.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(dob)){
                    Toast.makeText(SignUpPage.this, "Please enter your date of birth", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(location)){
                    Toast.makeText(SignUpPage.this, "Please enter your location", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(weight)){
                    Toast.makeText(SignUpPage.this, "Please enter your weight", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(height)){
                    Toast.makeText(SignUpPage.this, "Please enter your height", Toast.LENGTH_SHORT).show();
                    return;
                }

                /**
                 * this is the authentication process
                 */
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Registration successful.",
                                            Toast.LENGTH_SHORT).show();
                                    User user = new User(name, dob, location, height, weight, email);

                                    FirebaseDatabase.getInstance().getReference("users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .setValue(user);
                                    openHomePage();
                                } else {
                                    Toast.makeText(SignUpPage.this, "Registration failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    //open the home page

    /**
     * this is going to be prompting the user to the home page
     */
    public void openHomePage() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }

}