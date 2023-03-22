package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class MedicationLogger extends AppCompatActivity {
    private EditText medicationName, frequency;
    private Button saveButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_biometrics);

        //this is going to be displaying the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        medicationName = findViewById(R.id.medicationName);
        frequency = findViewById(R.id.frequencyMed);
        saveButton = findViewById(R.id.saveButton);

        //firebase authentication to store data
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = currentUser.getUid(); //get the user's unique ID
        userRef = database.getReference("users/").child(userId);

        //saving info on click of button into db
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedicationInformation();
            }
        });
    }

    //info saving
    private void saveMedicationInformation() {
        String medication = medicationName.getText().toString().trim();
        String freq = frequency.getText().toString().trim();

        if (!medication.isEmpty() && !freq.isEmpty()) {
            int frequency = Integer.parseInt(freq);
            MedicationInformation medinfo = new MedicationInformation(medication, frequency);
            userRef.child("medications").push().setValue(medinfo); //store the medication information under the user's unique ID
            Toast.makeText(MedicationLogger.this, "Information saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MedicationLogger.this, "Invalid/No Entry. Please Try Again!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //go back to main activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
