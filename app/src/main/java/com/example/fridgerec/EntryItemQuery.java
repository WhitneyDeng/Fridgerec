package com.example.fridgerec;

import android.util.Log;
import android.widget.Toast;

import com.example.fridgerec.interfaces.DatasetViewModel;
import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.util.FoodNameComparator;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryItemQuery {
  public enum SortFilter {
    NONE,
    SORT_FOOD_NAME, SORT_FOOD_GROUP, SORT_EXPIRE_DATE, SORT_SOURCE_DATE,
    FILTER_FOOD_GROUP, FILTER_EXPIRE_BEFORE, FILTER_EXPIRE_AFTER, FILTER_SOURCED_BEFORE, FILTER_SOURCED_AFTER
  }

  public static final String TAG = "EntryItemQuery";

  private static HashMap<SortFilter, Object> sortFilterParams;
  private static String containerList;
  private static DatasetViewModel viewModel;

  public static void queryEntryItems(DatasetViewModel vm, String cl) {
    viewModel = vm;
    containerList = cl;
    sortFilterParams = vm.getSortFilterParams().getValue();

    makeQuery(configParseQuery());
  }

  private static ParseQuery<EntryItem> configParseQuery() {
    ParseQuery<EntryItem> query = new ParseQuery<EntryItem>(EntryItem.class);

    query.whereEqualTo(EntryItem.KEY_USER, ParseUser.getCurrentUser());
    query.whereEqualTo(EntryItem.KEY_CONTAINER_LIST, containerList);
    query.include(EntryItem.KEY_FOOD);   // include User data of each Post class in response

    //TODO: chain query param
    for (Map.Entry<SortFilter, Object> entry : sortFilterParams.entrySet()) {
      switch (entry.getKey())
      {
        case SORT_FOOD_GROUP:
          break;
        case SORT_FOOD_NAME:
          break;
        case SORT_EXPIRE_DATE:
          query.orderByAscending(EntryItem.KEY_EXPIRE_DATE);
          break;
        case SORT_SOURCE_DATE:
          query.orderByAscending(EntryItem.KEY_SOURCE_DATE);
          break;
        case FILTER_EXPIRE_BEFORE:
          query.whereLessThan(EntryItem.KEY_EXPIRE_DATE,
              (Date) entry.getValue());
          break;
        case FILTER_EXPIRE_AFTER:
          query.whereGreaterThan(EntryItem.KEY_EXPIRE_DATE,
              (Date) entry.getValue());
          break;
        case FILTER_SOURCED_BEFORE:
          query.whereLessThan(EntryItem.KEY_SOURCE_DATE,
              (Date) entry.getValue());
          break;
        case FILTER_SOURCED_AFTER:
          query.whereGreaterThan(EntryItem.KEY_SOURCE_DATE,
              (Date) entry.getValue());
          break;
        case FILTER_FOOD_GROUP:
          String foodGroup = (String) entry.getValue(); //TODO: select multiple food group => becomes List<String>
          //TODO: implement
          break;
        case NONE:
        default:
      }
    }
    return query;
  }

  private static void makeQuery(ParseQuery<EntryItem> query) {
    query.findInBackground(new FindCallback<EntryItem>() {
      @Override
      public void done(List<EntryItem> queryResult, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with post query", e);
          return;
        }
        Log.i(TAG, "Post query success");
        Log.i(TAG, "queryResult" + queryResult.toString());

        postQueryProcess(queryResult);
      }
    });
  }

  private static void postQueryProcess(List<EntryItem> queryResult) {

    if (sortFilterParams.containsKey(SortFilter.SORT_FOOD_NAME)) {
      Collections.sort(queryResult, new FoodNameComparator());
    }
    if (sortFilterParams.containsKey(SortFilter.SORT_FOOD_GROUP)) {
      viewModel.getMap()
          .setValue(
              filterFoodGroup(queryResult));
      return;
    }
    viewModel.getList().setValue(queryResult);
  }

  private static HashMap<String, List<EntryItem>> filterFoodGroup(List<EntryItem> entryItems)  {
    HashMap<String, List<EntryItem>> foodGroupMap = new HashMap<>();
    for (EntryItem entryItem : entryItems) {
      String foodGroup = entryItem.getFood().getFoodGroup();
      List<EntryItem> foodGroupList = foodGroupMap.getOrDefault(foodGroup, new ArrayList<>());
      foodGroupList.add(entryItem);
      foodGroupMap.put(foodGroup, foodGroupList);
    }
    return foodGroupMap;
  }

  public static void deleteEntryItems(DatasetViewModel viewModel) {
    EntryItem.deleteAllInBackground(viewModel.getCheckedItemsList(), new DeleteCallback() {
      @Override
      public void done(ParseException e) {
        viewModel.getInDeleteMode().setValue(false);

        if (e != null) {
          Log.e(TAG, "error while deleting entryItems", e);
          return;
        }
        Log.i(TAG, "entryItems deleted successfully");
      }
    });
    // TODO: get checkedItems
    // TODO: delete items
      // TODO: toggle inDeleteMode
  }

  //TODO: query distinct food groups
}
