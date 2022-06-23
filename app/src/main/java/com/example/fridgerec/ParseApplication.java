package com.example.fridgerec;

import android.app.Application;

import com.example.fridgerec.R;
import com.example.fridgerec.model.Food;
import com.example.fridgerec.model.InventoryItem;
import com.example.fridgerec.model.ShoppingItem;
import com.example.fridgerec.model.User;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    ParseObject.registerSubclass(Food.class);
    ParseObject.registerSubclass(ShoppingItem.class);
    ParseObject.registerSubclass(InventoryItem.class);
    ParseObject.registerSubclass(User.class);

    Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.back4app_app_id))
            .clientKey(getString(R.string.back4app_client_key))
            .server(getString(R.string.back4app_server_url))
            .build());
  }
}