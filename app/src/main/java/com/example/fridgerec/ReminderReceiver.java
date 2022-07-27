package com.example.fridgerec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.model.Settings;

import java.util.List;
import java.util.Locale;

public class ReminderReceiver extends BroadcastReceiver {
  public static final String TAG = ReminderReceiver.class.getName();
  public static final String REMINDER_CHANNEL_NAME = "consume soon reminder";
  public static final String REMINDER_CHANNEL_ID = String.format("%s-%s", ReminderReceiver.class.getPackage().getName(), REMINDER_CHANNEL_NAME);
  public static final int REMINDER_NOTIFICATION_ID = 8;

  @Override
  public void onReceive(Context context, Intent intent) {
    int expireOffset = intent.getIntExtra(Settings.KEY_NOTIFICATION_EXPIRE_DATE_OFFSET, Settings.DEFAULT_NOTIFICATION_DATE_OFFSET);
    int sourceOffset = intent.getIntExtra(Settings.KEY_NOTIFICATION_SOURCE_DATE_OFFSET, Settings.DEFAULT_NOTIFICATION_DATE_OFFSET);

    Log.d(TAG, "onReceive notification ping");

    Runnable configNotificationRunnable = new Runnable() {
      @Override
      public void run() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("consume these foods soon!")
            .setContentTitle(
                getFoodConsumptionReminderContentString(
                    ParseClient.queryExpireSoonEntryItems(expireOffset),
                    ParseClient.querySourceLongAgoEntryItems(sourceOffset),
                    ParseClient.queryExpiredEntryItems()));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(REMINDER_NOTIFICATION_ID, builder.build());
      }
    };
    Thread configNotificationThread = new Thread(configNotificationRunnable);
    configNotificationThread.start();
  }

  private String getFoodConsumptionReminderContentString(List<EntryItem> expiringSoonList, List<EntryItem> sourcedLongAgoList, List<EntryItem> expiredList) {
    StringBuilder reminderContent = new StringBuilder();
    int expiringSoonCount = expiringSoonList == EntryItem.DUMMY_ENTRY_ITEM_LIST ? 0 : expiringSoonList.size();
    int sourcedLongAgoCount = sourcedLongAgoList == EntryItem.DUMMY_ENTRY_ITEM_LIST ? 0 : sourcedLongAgoList.size();
    int expiredCount = expiredList == EntryItem.DUMMY_ENTRY_ITEM_LIST ? 0 : expiredList.size();

    reminderContent.append(String.format(Locale.getDefault(), "%d item(s) expiring soon\n", expiringSoonCount));
    reminderContent.append(String.format(Locale.getDefault(), "%d item(s) sourced long ago\n", sourcedLongAgoCount));
    reminderContent.append(String.format(Locale.getDefault(), "%d item(s) expired\n", expiredCount));

    Log.d(TAG, "notification content: " + reminderContent.toString());

    return reminderContent.toString();
  }
}
