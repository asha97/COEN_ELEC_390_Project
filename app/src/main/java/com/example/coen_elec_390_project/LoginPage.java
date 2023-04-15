package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * the user is going to be asked to enter their credentials in order to log into their personal account
 */
public class LoginPage extends AppCompatActivity {

    EditText enterTextEmail, enterTextPassword;
    Button loginButton, regButton;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    /**
     * after authentication, the user is going to be lead to the home page
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
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        enterTextEmail = findViewById(R.id.userEnterEmail);
        enterTextPassword = findViewById(R.id.userEnterPassword);
        loginButton = findViewById(R.id.loginbtnPage);
        progressBar = findViewById(R.id.progressBar);
        regButton = findViewById(R.id.registerButton);

        /**
         * this is going to be prompting the user to go to the registration page
         * if they do not have an account yet
         */
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpPage.class);
                startActivity(intent);
                finish();

            }

        });

        /**
         * this is going to be prompting the user to enter their credentials in order to be lead to the home page
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we want to see the progression of the sign up
                progressBar.setVisibility(View.VISIBLE);

                String email, password;
                email = String.valueOf(enterTextEmail.getText());
                password = String.valueOf(enterTextPassword.getText());

                //check if the email or password fields are empty or not
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginPage.this, "Please enter a valid e-mail", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginPage.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                    return;
                }

                /**
                 * checking if the authentication process is successful or not
                 * if it is successful, then the user is going to be prompted to the home page
                 */
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);


                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login successful.",
                                            Toast.LENGTH_SHORT).show();
                                    //if the login is successful, you are going to be opening the home page
                                    openHomePage();

                                } else {

                                    Toast.makeText(LoginPage.this, "Authentication failed.",
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
}