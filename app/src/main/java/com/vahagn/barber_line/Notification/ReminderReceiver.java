package com.vahagn.barber_line.Notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.vahagn.barber_line.R;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "budget_channel_id";

        String name = intent.getStringExtra("reminder_name");
        boolean isDefault = intent.getBooleanExtra("default_reminder", false);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Не забудь!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 250, 500});

        if (isDefault) {
            builder.setContentText("Запиши сегодняшние траты.");
        } else if (name != null) {
            builder.setContentText("Пришло время для траты: " + name);
        } else {
            builder.setContentText("Что-то важное, проверь приложение!");
        }

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}

