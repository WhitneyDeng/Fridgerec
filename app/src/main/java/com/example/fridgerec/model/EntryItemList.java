package com.example.fridgerec.model;

import android.util.Log;

import com.example.fridgerec.interfaces.LithoUIChangeHandler;
import com.example.fridgerec.util.FoodNameComparator;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EntryItemList {
  public enum SortFilter {
    NONE,
    SORT_FOOD_NAME, SORT_FOOD_GROUP, SORT_EXPIRE_DATE, SORT_SOURCE_DATE,
    FILTER_FOOD_GROUP, FILTER_EXPIRE_BEFORE, FILTER_EXPIRE_AFTER, FILTER_SOURCED_BEFORE, FILTER_SOURCED_AFTER
  }

  public static final String TAG = "EntryItemList";

  public EntryItemList() {
  }

  public static void queryEntryItems(SortFilter sortFilterParam, String containerList, LithoUIChangeHandler lithoUIChangeHandler) {
    makeQuery(sortFilterParam,
        configParseQuery(sortFilterParam, containerList),
        lithoUIChangeHandler);
  }

  private static ParseQuery<EntryItem> configParseQuery(SortFilter sortFilterParam, String containerList) {
    ParseQuery<EntryItem> query = new ParseQuery<EntryItem>(EntryItem.class);

    query.whereEqualTo(EntryItem.KEY_USER, ParseUser.getCurrentUser());
    query.whereEqualTo(EntryItem.KEY_CONTAINER_LIST, containerList);
    query.include(EntryItem.KEY_FOOD);   // include User data of each Post class in response

    //TODO: chain query param
    switch (sortFilterParam)
    {
      case SORT_FOOD_GROUP:
        break;
      case SORT_FOOD_NAME:
        //TODO: implement local sorting function
        break;
      case SORT_EXPIRE_DATE:
        query.orderByAscending(EntryItem.KEY_EXPIRE_DATE);
        break;
      case SORT_SOURCE_DATE:
        query.orderByAscending(EntryItem.KEY_SOURCE_DATE);
        break;
      case FILTER_EXPIRE_BEFORE:
        break;
      case FILTER_EXPIRE_AFTER:
        break;
      case FILTER_SOURCED_BEFORE:
        break;
      case FILTER_SOURCED_AFTER:
        break;
      case FILTER_FOOD_GROUP:
        break;
      case NONE:
      default:
    }
    return query;
  }

  private static void makeQuery(SortFilter sortFilterParam, ParseQuery<EntryItem> query, LithoUIChangeHandler lithoUIChangeHandler) {
    query.findInBackground(new FindCallback<EntryItem>() {
      @Override
      public void done(List<EntryItem> queryResult, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with post query", e);
          return;
        }
        Log.i(TAG, "Post query success");
        Log.i(TAG, "queryResult" + queryResult.toString());

        postQueryProcess(queryResult, sortFilterParam);

        //TODO: set inventoryList of corresponding ViewModel
        lithoUIChangeHandler.setupLithoView(sortFilterParam, queryResult);
      }
    });
  }

  private static void postQueryProcess(List<EntryItem> queryResult, SortFilter sortFilterParam) {
    switch (sortFilterParam) {
      case SORT_FOOD_NAME:
        Collections.sort(queryResult, new FoodNameComparator());
        break;
    }
  }

  public static HashMap<String, List<EntryItem>> filterFoodGroup(List<EntryItem> entryItems)  {
    HashMap<String, List<EntryItem>> foodGroupMap = new HashMap<>();
    for (EntryItem entryItem : entryItems) {
      String foodGroup = entryItem.getFood().getFoodGroup();
      List<EntryItem> foodGroupList = foodGroupMap.getOrDefault(foodGroup, new ArrayList<EntryItem>());
      foodGroupList.add(entryItem);
      foodGroupMap.put(foodGroup, foodGroupList);
    }
    return foodGroupMap;
  }
}
