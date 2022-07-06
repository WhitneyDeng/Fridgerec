package com.example.fridgerec.model;

import android.util.Log;

import com.example.fridgerec.interfaces.LithoUIChangeHandler;
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

  public void queryEntryItems(SortFilter sortFilterParam, String containerList, LithoUIChangeHandler lithoUIChangeHandler) {
    makeQuery(sortFilterParam,
        configParseQuery(sortFilterParam, containerList),
        lithoUIChangeHandler);
  }

  private ParseQuery<EntryItem> configParseQuery(SortFilter sortFilterParam, String containerList) {
    ParseQuery<EntryItem> query = new ParseQuery<EntryItem>(EntryItem.class);

    query.whereEqualTo(EntryItem.KEY_USER, ParseUser.getCurrentUser());
    query.whereEqualTo(EntryItem.KEY_CONTAINER_LIST, containerList);
    query.include(EntryItem.KEY_FOOD);   // include User data of each Post class in response

    //todo: chain query param
    switch (sortFilterParam)
    {
      case SORT_FOOD_GROUP:
        break;
      case SORT_FOOD_NAME:
        //todo: how to sort by fod name
//        query.addAscendingOrder()
        break;
      case SORT_EXPIRE_DATE:
        query.addAscendingOrder(EntryItem.KEY_EXPIRE_DATE);
        break;
      case SORT_SOURCE_DATE:
        query.addAscendingOrder(EntryItem.KEY_SOURCE_DATE);
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
    return query;
  }

  private void makeQuery(SortFilter sortFilterParam, ParseQuery<EntryItem> query, LithoUIChangeHandler lithoUIChangeHandler) {
    query.findInBackground(new FindCallback<EntryItem>() {
      @Override
      public void done(List<EntryItem> queryResult, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with post query", e);
          return;
        }
        Log.i(TAG, "Post query success");
        Log.i(TAG, "queryResult" + queryResult.toString());

        lithoUIChangeHandler.setupLithoView(sortFilterParam, queryResult);
      }
    });
  }

  //todo: sorts entryItems into FoodCategory: ArrayList<EntryItem>
  public HashMap<String, List<EntryItem>> filterFoodGroup(List<EntryItem> entryItems)  {
    return new HashMap<>();
  }
}
