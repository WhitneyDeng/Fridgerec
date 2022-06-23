package com.example.fridgerec.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {
  public static final String KEY_NOTIFICATION_TIME = "notificationTime";
}
