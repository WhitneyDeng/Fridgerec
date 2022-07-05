package com.example.fridgerec.model;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntryItemList {
  public enum SortFilter {
    NONE,
    SORT_FOOD_NAME, SORT_FOOD_GROUP, SORT_EXPIRE_DATE, SORT_SOURCE_DATE,
    FILTER_FOOD_GROUP, FILTER_EXPIRE_DATE, FILTER_SOURCE_DATE
  }

  public static final String TAG = "EntryItemList";

  public EntryItemList() {
  }

  /**
   * @return returns HashMap<String, List<EntryItem>> when sortFilterParam = FILTER_FOOD_GROUP, else returns List<EntryItems>
   */
  public Object queryEntryItems(SortFilter sortFilterParam, String containerList) {
    ParseQuery<EntryItem> query = ParseQuery.getQuery(EntryItem.class);

    ArrayList<EntryItem> entryItems = new ArrayList<>();
    query.whereEqualTo(EntryItem.KEY_USER, ParseUser.getCurrentUser());
    query.whereEqualTo(EntryItem.KEY_CONTAINER_LIST, containerList);
    query.include(EntryItem.KEY_FOOD);   // include User data of each Post class in response

    switch (sortFilterParam)
    {
      case SORT_FOOD_GROUP:
        makeQuery(query, entryItems);
        return filterFoodGroup(entryItems);
      case SORT_FOOD_NAME:
        break;
      case SORT_EXPIRE_DATE:
        break;
      case SORT_SOURCE_DATE:
        break;
      case FILTER_EXPIRE_DATE:
        break;
      case FILTER_SOURCE_DATE:
        break;
      case FILTER_FOOD_GROUP:
        break;
      case NONE:
      default:
    }
    makeQuery(query, entryItems);

    return entryItems;
  }

  private void makeQuery(ParseQuery<EntryItem> query, ArrayList<EntryItem> entryItems) {
    query.findInBackground(new FindCallback<EntryItem>() {
      @Override
      public void done(List<EntryItem> queryResult, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with post query", e);
          return;
        }
        Log.i(TAG, "Post query success");

        entryItems.addAll(queryResult);
      }
    });
  }

  //todo: sorts entryItems into FoodCategory: ArrayList<EntryItem>
  private HashMap<String, List<EntryItem>> filterFoodGroup(List<EntryItem> entryItems)  {
    return new HashMap<>();
  }
}
