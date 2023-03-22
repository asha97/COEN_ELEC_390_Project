package com.example.coen_elec_390_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;



public class TestNotifActivity extends AppCompatActivity {
    private EditText etToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notif);

        etToken = findViewById(R.id.etToken);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        System.out.println(token);
                        Toast.makeText(TestNotifActivity.this, "this device reg token is: " + token, Toast.LENGTH_SHORT).show();

                        etToken.setText(token);
                    }
                });
    }
}