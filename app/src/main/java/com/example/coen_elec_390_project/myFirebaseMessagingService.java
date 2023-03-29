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



    //this is going to be sending the notification when the ppm is above 200
    public void sendNotification(String title, String message) {
        // check if the notification is about CO2 and its value is greater than 200
        if (title.equals("CO2 (ppm)") && Integer.parseInt(message) > 200) {
            // create a notification builder for the CO2 alert
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText("CO2 particle is above 200 ppm, be careful!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.icon_notif); // replace with your own warning icon

            // show the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, builder.build());
        }
    }


    /*
    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
       // notificationManager.notify(notificationId, builder.build());
    }

     */
}

//---------------------------TEST NOTIF CODE----------------------------------------------------

/*
@Override
public void onMessageReceived (@NonNull RemoteMessage remoteMessage) {
    String title = remoteMessage.getNotification().getTitle();
    String text = remoteMessage - getNotification().getBody();
    final String CHANNEL_ID = "HEADS_UP _NOTIFICATION";
    NotificationChannel channel = new NotificationChannel(
            CHANNEL_ID,
            "Heads Up Notification",
            NotificationManager.IMPORTANCE_HIGH
    );
    getSystemService(NotificationManager.class).createNotificationChannel(channel);
    Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.icon_notif)
            .setAutoCancel(true);
    NotificationManagerCompat.from(this).notify(1, notification.build());
    super.onMessageReceived(remoteMessage);
}
 */