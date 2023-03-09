package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

public class ConnectionSettings extends AppCompatActivity {

    protected Button connectWiFiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_settings);

        //this is going to be displaying the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        connectWiFiBtn = findViewById(R.id.connectWiFiButton);


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