package com.example.weatherapp.data;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.weatherapp.R;
import com.example.weatherapp.ui.main.MainActivity;

public class NotificationHelper {

    private static final String NOTIFICATION_CHANNEL = "channel notification";

    public static Notification getNotification(Context context) {

        createNotificationChannel(context);

        return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My notification")
                .setAutoCancel(false)
                .setContentIntent(createPendingIntent(context))
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

    }

    public static void createNotification(Context context){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, getNotification(context));
    }

    private static PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
       return PendingIntent.getActivity(context, 0, intent, 0);

    }

    private static void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test title";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
