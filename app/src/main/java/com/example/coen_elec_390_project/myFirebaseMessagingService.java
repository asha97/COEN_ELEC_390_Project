package com.example.coen_elec_390_project;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * This class is responsible for the notifications
 * It interacts with notifications with the Firebase Messaging service.
 * @author David Molina (40111257), Asha Islam (40051511), Pavithra Sivagnanasuntharam(40117356)
 */
public class myFirebaseMessagingService extends FirebaseMessagingService{
    private static final String CHANNEL_ID = "my_channel_id";
    private static final int notificationId = 1;


    /**
     * This enables users to have an in-app notification.
     * This works while the app runs in the backround, or in the foreground
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
    }



}
