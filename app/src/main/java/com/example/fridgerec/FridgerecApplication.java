package com.example.fridgerec;

import android.app.Application;

import com.example.fridgerec.model.Food;
import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.model.Settings;
import com.parse.Parse;
import com.parse.ParseObject;

public class FridgerecApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    ParseObject.registerSubclass(Food.class);
    ParseObject.registerSubclass(EntryItem.class);
    ParseObject.registerSubclass(Settings.class);

    Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.back4app_app_id))
            .clientKey(getString(R.string.back4app_client_key))
            .server(getString(R.string.back4app_server_url))
            .build());
  }
}