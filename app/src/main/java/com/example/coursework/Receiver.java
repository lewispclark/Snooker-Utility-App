package com.example.coursework;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

/**
 * Receiver shows notification when broadcast signal is received
 */
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    /**
     * Show notification to the user, alert them that they should come back to the app
     *
     * @param context of the notification, passed in by the receiver
     */
    public void showNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        @SuppressLint("InlinedApi") PendingIntent pi = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE);
//        Create notification, configure custom settings
        //noinspection deprecation
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo_foreground))
                .setColor(context.getResources().getColor(R.color.red_ball))
                .setContentTitle("Hey, come back!")
                .setContentText("The practice table is waiting for you!");
        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Send notification
        mNotificationManager.notify(1, mBuilder.build());
    }
}