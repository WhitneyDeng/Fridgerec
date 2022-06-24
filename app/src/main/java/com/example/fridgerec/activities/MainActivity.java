package com.example.fridgerec.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fridgerec.R;
import com.example.fridgerec.activities.fragments.InventoryFragment;
import com.example.fridgerec.activities.fragments.SettingsFragment;
import com.example.fridgerec.activities.fragments.ShoppingFragment;
import com.example.fridgerec.model.Food;
import com.example.fridgerec.model.EntryItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  public static final String TAG = "MainActivity";

  private BottomNavigationView bottomNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    bottomNavigationView = findViewById(R.id.bottomNavigation);

    final FragmentManager fragmentManager = getSupportFragmentManager();

    bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
          case R.id.action_inventory:
            fragment = new InventoryFragment();
            break;
          case R.id.action_shopping:
            fragment = new ShoppingFragment();
            break;
          case R.id.action_settings:
          default:
            fragment = new SettingsFragment();
            break;
        }
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        return true;
      }
    });
    // default bottom nav selection
    bottomNavigationView.setSelectedItemId(R.id.action_inventory);

    query();
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

//  // Menu icons are inflated just as they were with actionbar
//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.menu_main, menu);
//
//    return true;
//  }
//
//  @Override
//  public boolean onOptionsItemSelected(MenuItem item) {
//    // Handle action bar item clicks here. The action bar will
//    // automatically handle clicks on the Home/Up button, so long
//    // as you specify a parent activity in AndroidManifest.xml.
//    int id = item.getItemId();
//
//    switch(id)
//    {
//      case R.id.miLogout:
//        Toast.makeText(this, "Logout selected", Toast.LENGTH_SHORT).show();
//        logout();
//      default:
//    }
//    return super.onOptionsItemSelected(item);
//  }

//  private void logout()
//  {
//    ParseUser.logOut();
//    goLoginActivity();
////        ParseUser currentUser = ParseUser.getCurrentUser();   //todo: update the current user by calling the ParseUser's getCurrentUser() how does this change anything?
//  }
//
//  private void goLoginActivity()
//  {
//    Intent i = new Intent(this, LoginActivity.class);
//    startActivity(i);
//    finish();
//  }
}