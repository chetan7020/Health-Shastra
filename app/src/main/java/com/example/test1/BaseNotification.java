package com.example.test1;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public abstract class BaseNotification {

    private Context context;
    private String channelId;
    private NotificationManagerCompat notificationManagerCompat;

    public BaseNotification(Context context, String channelId) {
        this.context = context;
        this.channelId = channelId;
        this.notificationManagerCompat = NotificationManagerCompat.from(context);

        // Create a notification channel for Android 8.0 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human-readable title",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Channel description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    // Abstract method to provide notification content (title, message)
    protected abstract NotificationContent getNotificationContent();

    // Method to send the notification
    public void sendNotification(int notificationId, Intent intent) {
        NotificationContent content = getNotificationContent();

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)  // Change this to your app icon
                .setContentTitle(content.title)
                .setContentText(content.message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Send the notification
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(notificationId, builder.build());
    }

    // Helper class to hold the notification title and message
    public static class NotificationContent {
        String title;
        String message;

        public NotificationContent(String title, String message) {
            this.title = title;
            this.message = message;
        }
    }
}
