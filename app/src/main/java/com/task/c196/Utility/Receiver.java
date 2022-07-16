package com.task.c196.Utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.task.c196.R;

public class Receiver extends BroadcastReceiver {
    String channel_id="test";
    static int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("NOTIFICATION HIT");
        createNotificationChannel(context, channel_id);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_baseline_school_24)
                .setContentText(intent.getStringExtra("message"))
                .setContentTitle(intent.getStringExtra("title"));

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(notificationID++, builder.build());
    }

    private void createNotificationChannel(Context context, String channel_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.channel_name);
            String desc = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}