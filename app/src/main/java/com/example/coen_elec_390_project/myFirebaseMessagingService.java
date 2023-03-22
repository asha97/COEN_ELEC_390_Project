package com.example.coen_elec_390_project;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class myFirebaseMessagingService extends FirebaseMessagingService{
    //in app notification


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        System.out.println("From: " + remoteMessage.getFrom());


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        sendNotification(remoteMessage.getFrom(), remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String from, String body){
        new Handler(Looper.getMainLooper()).post( new Runnable() {
            @Override
            public void run(){
                Toast.makeText(myFirebaseMessagingService.this.getApplicationContext(), from + " -> " + body, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
