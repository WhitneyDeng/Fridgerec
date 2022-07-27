package com.example.fridgerec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fridgerec.model.Settings;

public class ReminderReceiver extends BroadcastReceiver {
  public static final String TAG = ReminderReceiver.class.getName();
  // TODO: save notification channel id
  public static final String REMINDER_CHANNEL_NAME = "consume soon reminder";
  public static final String REMINDER_CHANNEL_ID = String.format("%s-%s", ReminderReceiver.class.getPackage().getName(), REMINDER_CHANNEL_NAME);
  public static final int REMINDER_NOTIFICATION_ID = 8;

  // TODO: show notification
  @Override
  public void onReceive(Context context, Intent intent) {
    // TODO: query items to expire & sourced
    int expireOffset = intent.getIntExtra(Settings.KEY_NOTIFICATION_EXPIRE_DATE_OFFSET, Settings.DEFAULT_NOTIFICATION_DATE_OFFSET);
    int sourceOffset = intent.getIntExtra(Settings.KEY_NOTIFICATION_SOURCE_DATE_OFFSET, Settings.DEFAULT_NOTIFICATION_DATE_OFFSET);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentTitle("consume these foods soon!");
    //TODO: set content
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    notificationManager.notify(REMINDER_NOTIFICATION_ID, builder.build());
  }
  // TODO: save notificaiton request code
}
