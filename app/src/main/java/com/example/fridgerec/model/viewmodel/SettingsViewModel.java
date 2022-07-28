package com.example.fridgerec.model.viewmodel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fridgerec.ParseClient;
import com.example.fridgerec.ReminderReceiver;
import com.example.fridgerec.interfaces.ParseCallback;
import com.example.fridgerec.model.Settings;
import com.parse.ParseException;

import java.util.Calendar;
import java.util.TimeZone;

public class SettingsViewModel extends ViewModel implements ParseCallback {
  public static final String TAG = "SettingsViewModel";
  private Settings userSettings;
  private MutableLiveData<Boolean> parseOperationSuccess;
  private ParseException parseException;

  public Settings getUserSettings() {
    if (userSettings == null) {
      userSettings = ParseClient.getCurrentUserSettings();
    }
    return userSettings;
  }

  public void saveUserSettings() {
    ParseClient.saveCurrentUserSettings(userSettings, this);
  }

  @Override
  public MutableLiveData<Boolean> getParseOperationSuccess() {
    if (parseOperationSuccess == null) {
      parseOperationSuccess = new MutableLiveData<>();
    }
    return parseOperationSuccess;
  }

  @Override
  public ParseException getParseException() {
    return parseException;
  }

  @Override
  public void setParseException(ParseException e) {
    parseException = e;
  }

  public void setRecurringReminder(Context context) {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarmManager.setInexactRepeating(AlarmManager.RTC, getNotificationTimeInMillis(), AlarmManager.INTERVAL_DAY, getReminderPendingIntent(context));

    Toast.makeText(context, "food consumption reminder set", Toast.LENGTH_SHORT).show();
  }

  public void cancelRecurringReminder(Context context) {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarmManager.cancel(getReminderPendingIntent(context));

    Toast.makeText(context, "food consumption reminder cancelled", Toast.LENGTH_SHORT).show();
  }

  private PendingIntent getReminderPendingIntent(Context context) {
    Intent consumptionReminder = new Intent(context, ReminderReceiver.class);
    consumptionReminder.putExtra(Settings.KEY_NOTIFICATION_EXPIRE_DATE_OFFSET, getUserSettings().getNotificationExpireDateOffset());
    consumptionReminder.putExtra(Settings.KEY_NOTIFICATION_SOURCE_DATE_OFFSET, getUserSettings().getNotificationSourceDateOffset());

    PendingIntent recurringConsumptionReminder = PendingIntent.getBroadcast(context, ReminderReceiver.REMINDER_NOTIFICATION_ID, consumptionReminder, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);
    return recurringConsumptionReminder;
  }

  private long getNotificationTimeInMillis() {
    Calendar notificationTime = Calendar.getInstance();
    notificationTime.setTimeZone(TimeZone.getDefault());
    notificationTime.set(Calendar.HOUR_OF_DAY, getUserSettings().getNotificationHour());
    notificationTime.set(Calendar.MINUTE, getUserSettings().getNotificationMinute());

    return notificationTime.getTimeInMillis();
  }
}
