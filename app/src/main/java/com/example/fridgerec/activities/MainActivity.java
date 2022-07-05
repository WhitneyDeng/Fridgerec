package com.example.fridgerec.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.fridgerec.R;
import com.example.fridgerec.activities.fragments.InventoryFragment;
import com.example.fridgerec.activities.fragments.SettingsFragment;
import com.example.fridgerec.activities.fragments.ShoppingFragment;
import com.example.fridgerec.model.Food;
import com.example.fridgerec.model.EntryItem;
import com.facebook.soloader.SoLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  public static final String TAG = "MainActivity";

  private NavHostFragment navHostFragment;
  private NavController navController;

  private BottomNavigationView bottomNavigationBar;
  private AppBarConfiguration appBarConfiguration;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SoLoader.init(this, false);
    setContentView(R.layout.activity_main);

    setupBottomNav();

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

//    query();
  }

  private void setupBottomNav() {
    bottomNavigationBar = findViewById(R.id.bottomNavigationBar);
    navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    navController = navHostFragment.getNavController();
    NavigationUI.setupWithNavController(bottomNavigationBar, navController);
  }

  private void saveShoppingItem(List<Food> foods) {

    EntryItem shoppingItem = new EntryItem();
    shoppingItem.setFood(foods.get(0));
    shoppingItem.setUser(ParseUser.getCurrentUser());

    shoppingItem.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e != null) {
          Log.e(TAG, "error while saving shopping item", e);
          Toast.makeText(MainActivity.this, "error while saving shopping item", Toast.LENGTH_SHORT).show();
          return; //note: not in tutorial (remove this line to clear interface even on failure to save post)
        }
        Log.i(TAG, "shopping item saved successfully");
      }
    });
  }

  private void query() {
//    Log.i(TAG, "User Notification time:" + ParseUser.getCurrentUser().getInt("notificationTime"));

    ParseQuery<EntryItem> query = ParseQuery.getQuery(EntryItem.class);
    query.findInBackground(new FindCallback<EntryItem>() {
      @Override
      public void done(List<EntryItem> entryItems, ParseException e) {
        if (e != null)
        {
          Log.e(TAG, "Issue with getting users", e);
          return;
        }
        for (EntryItem entryItem : entryItems)
        {
          Log.i(TAG, "entry Item expiration date: " + entryItem.getExpireDate());
        }
      }
    });

//    ParseQuery<Food> query = ParseQuery.getQuery(Food.class);
//    query.findInBackground(new FindCallback<Food>() {
//      @Override
//      public void done(List<Food> foods, ParseException e) {
//        if (e != null)
//        {
//          Log.e(TAG, "Issue with getting posts", e);
//          return;
//        }
//        for (Food food : foods)
//        {
//          Log.i(TAG, "Food: " + food.getItemName());
//        }
//        saveShoppingItem(foods);
//      }
//    });
  }
}