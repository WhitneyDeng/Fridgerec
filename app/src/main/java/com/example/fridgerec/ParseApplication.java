package com.example.fridgerec;

import android.app.Application;

import com.example.fridgerec.R;
import com.parse.Parse;

public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    // Note: copied from https://www.back4app.com/docs/android/parse-android-sdk
    Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.back4app_app_id))
            .clientKey(getString(R.string.back4app_client_key))
            .server(getString(R.string.back4app_server_url))
            .build());
  }
}