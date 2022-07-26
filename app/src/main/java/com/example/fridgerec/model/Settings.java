package com.example.fridgerec.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Settings")
public class Settings extends ParseObject {
  public static final String TAG = "Settings";

  public static final String KEY_USER = "user";
  public static final String KEY_NOTIFICATION_TIME = "notificationTime";
  public static final String KEY_NOTIFICATION_EXPIRE_DATE_OFFSET = "notificationExpireDateOffset";
  public static final String KEY_NOTIFICATION_SOURCE_DATE_OFFSET = "notificationSourceDateOffset";
  public static final String KEY_NOTIFICATION_ENABLED = "notificationEnabled";

  public static Settings getDefaultSettings() {
    Settings settings = new Settings();
    settings.setNotificationTime(8,0);
    settings.setNotificationExpireDateOffset(7);
    settings.setNotificationSourceDateOffset(7);
    settings.setNotificationEnabled(false);
    return settings;
  }

  public ParseUser getUser() {
    return getParseUser(KEY_USER);
  }

  public void setUser(ParseUser parseUser) {
    put(KEY_USER, parseUser);
  }

  private int getNotificationTime() {
    return getInt(KEY_NOTIFICATION_TIME);
  }

  public int getNotificationHour() {
    return getNotificationTime() / 100;
  }

  public int getNotificationMinute() {
    return getNotificationTime() % 100;
  }

  public void setNotificationTime(int hour, int minute) {
    if (hour < 23 && minute < 60) {
      put(KEY_NOTIFICATION_TIME, hour*100 + minute);
    } else {
      Log.e(TAG, "set notification time failed: hour must be <23, minute must be <60");
    }
  }

  public int getNotificationExpireDateOffset() {
    return getInt(KEY_NOTIFICATION_EXPIRE_DATE_OFFSET);
  }

  public void setNotificationExpireDateOffset(int offset) {
    put(KEY_NOTIFICATION_EXPIRE_DATE_OFFSET, offset);
  }

  public int getNotificationSourceDateOffset() {
    return getInt(KEY_NOTIFICATION_SOURCE_DATE_OFFSET);
  }

  public void setNotificationSourceDateOffset(int offset) {
    put(KEY_NOTIFICATION_SOURCE_DATE_OFFSET, offset);
  }

  public boolean getNotificationEnabled() {
    return getBoolean(KEY_NOTIFICATION_ENABLED);
  }

  public void setNotificationEnabled(boolean b) {
    put(KEY_NOTIFICATION_ENABLED, b);
  }
}
