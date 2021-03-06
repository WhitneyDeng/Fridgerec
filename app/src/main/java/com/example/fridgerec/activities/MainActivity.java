package com.example.fridgerec.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.fridgerec.R;
import com.example.fridgerec.ReminderReceiver;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.soloader.SoLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  public static final String TAG = "MainActivity";

  private NavHostFragment navHostFragment;
  private NavController navController;

  private BottomNavigationView bottomNavigationBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SoLoader.init(this, false);
    Fresco.initialize(this);
    setContentView(R.layout.activity_main);

    setupBottomNav();
    configNavController();
    createNotificationChannel(ReminderReceiver.REMINDER_CHANNEL_NAME, ReminderReceiver.REMINDER_CHANNEL_ID);
  }

  private void createNotificationChannel(String channelName, String channelId) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

  private void configNavController() {
    navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
      @Override
      public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
        switch (navDestination.getId()) {
          case R.id.loginFragment: case R.id.inventoryCreationFragment: case R.id.shoppingCreationFragment:
            bottomNavigationBar.setVisibility(View.GONE);
            break;
          default:
            bottomNavigationBar.setVisibility(View.VISIBLE);
        }
      }
    });
  }

  private void setupBottomNav() {
    bottomNavigationBar = findViewById(R.id.bottomNavigationBar);
    navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    navController = navHostFragment.getNavController();
    NavigationUI.setupWithNavController(bottomNavigationBar, navController);
  }
}