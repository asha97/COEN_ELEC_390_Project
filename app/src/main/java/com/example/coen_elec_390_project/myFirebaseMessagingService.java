package com.example.coen_elec_390_project;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class myFirebaseMessagingService extends FirebaseMessagingService{

    private static final String CHANNEL_ID = "my_channel_id";
    private static final int notificationId = 1;

   //this is to have an in-app notification

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        sendNotification(title, message);
    }

    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
       // notificationManager.notify(notificationId, builder.build());
    }
}
