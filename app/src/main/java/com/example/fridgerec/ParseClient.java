package com.example.fridgerec;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.fridgerec.interfaces.DatasetViewModel;
import com.example.fridgerec.interfaces.ParseCallback;
import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.model.Food;
import com.example.fridgerec.model.Settings;
import com.example.fridgerec.util.FoodNameComparator;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseClient {
  public enum SortFilter {
    NONE,
    SORT_FOOD_NAME, SORT_FOOD_GROUP, SORT_EXPIRE_DATE, SORT_SOURCE_DATE,
    FILTER_FOOD_GROUP, FILTER_EXPIRE_BEFORE, FILTER_EXPIRE_AFTER, FILTER_SOURCED_BEFORE, FILTER_SOURCED_AFTER
  }

  public static final String TAG = "ParseClient";

  private static HashMap<SortFilter, Object> sortFilterParams;
  private static String containerList;
  private static DatasetViewModel viewModel;

  public static Settings getCurrentUserSettings() {
    ParseQuery<Settings> query = new ParseQuery<Settings>(Settings.class);

    query.whereEqualTo(Settings.KEY_USER, ParseUser.getCurrentUser());

    try {
      return query.getFirst();
    } catch (ParseException e) {
      e.printStackTrace();
      return Settings.getDefaultSettings();
    }
  }

  public static void saveCurrentUserSettings(Settings settings, ParseCallback settingsViewModel) {
    settings.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e != null) {
          Log.e(TAG, "save current user setting failed: " + e);
          settingsViewModel.setParseException(e);
        }
        settingsViewModel.getParseOperationSuccess().setValue(e == null);
      }
    });
  }

  public static void queryEntryItems(DatasetViewModel vm) {
    viewModel = vm;
    containerList = vm.getContainerList();
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
          String foodGroup = (String) entry.getValue();

          ParseQuery<ParseObject> foodQuery = new ParseQuery<>("Food");
          foodQuery.whereEqualTo(Food.KEY_FOOD_GROUP, foodGroup);

          query.whereMatchesQuery(EntryItem.KEY_FOOD, foodQuery);
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
          Log.e(TAG, "Issue with EntryItem query", e);
          return;
        }
        Log.i(TAG, "EntryItem query success");
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
      String foodGroupString = entryItem.getFood().getFoodGroup();
      foodGroupString = foodGroupString == null ? Food.NO_FOOD_GROUP : foodGroupString;
      String[] foodGroups = foodGroupString.split(";");
      for (String foodGroup : foodGroups) {
        List<EntryItem> foodGroupList = foodGroupMap.getOrDefault(foodGroup, new ArrayList<>());
        foodGroupList.add(entryItem);
        foodGroupMap.put(foodGroup, foodGroupList);
      }
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
  }

  public static void transferToInventory(DatasetViewModel viewModel) {
    List<EntryItem> checkedItems = viewModel.getCheckedItemsList();

    updateTransferredEntry(checkedItems);
    EntryItem.saveAllInBackground(checkedItems, new SaveCallback() {
      @Override
      public void done(ParseException e) {
        viewModel.getInDeleteMode().setValue(false);

        if (e != null) {
          Log.e(TAG, "error while transferring entryItems to inventory", e);
          return;
        }
        Log.i(TAG, "entryItems transferred to inventory successfully");
      }
    });
  }

  private static void updateTransferredEntry(List<EntryItem> checkedItems) {
    for (EntryItem checkedItem : checkedItems) {
      checkedItem.setContainerList(EntryItem.CONTAINER_LIST_INVENTORY);
      checkedItem.setSourceDate(new Date(System.currentTimeMillis()));
    }
  }

  public static void saveNewEntry(EntryItem entryItem, DatasetViewModel viewModel, FragmentActivity activity) {
    saveNewFood(entryItem, viewModel);

    //TODO: observe entry => return entry or DUMMY_ENTRY
  }

  private static void saveNewFood(EntryItem entryItem, DatasetViewModel viewModel) {
    Food food = entryItem.getFood();
    ParseQuery<Food> apiIdQuery = new ParseQuery<>("Food");
    ParseQuery<Food> foodNameQuery = new ParseQuery<>("Food");
    ParseQuery<Food> compoundQuery = ParseQuery.or(Arrays.asList(apiIdQuery, foodNameQuery));

    apiIdQuery.whereMatches(Food.KEY_API_ID, String.format("(%s)", food.getApiId()));
    foodNameQuery.whereMatches(Food.KEY_FOOD_NAME, String.format("(%s)", food.getFoodName()), "i");

    compoundQuery.getFirstInBackground(new GetCallback<Food>() {
      @Override
      public void done(Food existingFood, ParseException e) {
        Log.i(TAG, String.format("check existing food in database: food: %s | exception: %s", existingFood, e));

        if (e != null) {
          Log.e(TAG, "no existing food: " + e);
          food.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
              if (e != null) {
                Log.e(TAG, "new food save failed: " + e);
                viewModel.setParseException(e);
                viewModel.getParseOperationSuccess().setValue(false);
                return;
              }
              entryItem.setFood(food);
              saveEntryItem(entryItem, viewModel);
            }
          });
        } else {
          Log.i(TAG, "existing food found");
          entryItem.setFood(existingFood);
          saveEntryItem(entryItem, viewModel);
        }
      }
    });
  }

  private static void saveEntryItem(EntryItem entryItem, DatasetViewModel viewModel) {
    entryItem.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e != null) {
          Log.i(TAG, "error: new entry item save failed: " + e);
          viewModel.setParseException(e);
          viewModel.getParseOperationSuccess().setValue(false);
          return;
        }
        Log.i(TAG, "new entry item save success");
        viewModel.getParseOperationSuccess().setValue(true);
        viewModel.refreshDataset();
      }
    });
  }
  //TODO: query distinct food groups

  public static List<EntryItem> queryExpireSoonEntryItems(int expireDateOffset) {
    ParseQuery<EntryItem> query = new ParseQuery<EntryItem>(EntryItem.class);

    query.include(EntryItem.KEY_FOOD);
    query.whereEqualTo(EntryItem.KEY_USER, ParseUser.getCurrentUser());
    query.whereEqualTo(EntryItem.KEY_CONTAINER_LIST, EntryItem.CONTAINER_LIST_INVENTORY);

    query.whereExists(EntryItem.KEY_EXPIRE_DATE);
    query.whereLessThanOrEqualTo(EntryItem.KEY_EXPIRE_DATE, offsetDateFromToday(expireDateOffset));
    query.whereGreaterThanOrEqualTo(EntryItem.KEY_EXPIRE_DATE, offsetDateFromToday(0));

    try {
      return query.find();
    } catch (ParseException e) {
      Log.e(TAG, "query failed: " + e);
      return EntryItem.DUMMY_ENTRY_ITEM_LIST;
    }
  }

  public static List<EntryItem> querySourceLongAgoEntryItems(int sourceDateOffset) {
    ParseQuery<EntryItem> query = new ParseQuery<EntryItem>(EntryItem.class);

    query.include(EntryItem.KEY_FOOD);
    query.whereEqualTo(EntryItem.KEY_USER, ParseUser.getCurrentUser());
    query.whereEqualTo(EntryItem.KEY_CONTAINER_LIST, EntryItem.CONTAINER_LIST_INVENTORY);

    query.whereDoesNotExist(EntryItem.KEY_EXPIRE_DATE);
    query.whereLessThanOrEqualTo(EntryItem.KEY_SOURCE_DATE, offsetDateFromToday(-1 * sourceDateOffset));
    try {
      return query.find();
    } catch (ParseException e) {
      Log.e(TAG, "query failed: " + e);
      return EntryItem.DUMMY_ENTRY_ITEM_LIST;
    }
  }

  public static List<EntryItem> queryExpiredEntryItems() {
    ParseQuery<EntryItem> query = new ParseQuery<EntryItem>(EntryItem.class);

    query.include(EntryItem.KEY_FOOD);
    query.whereEqualTo(EntryItem.KEY_USER, ParseUser.getCurrentUser());
    query.whereEqualTo(EntryItem.KEY_CONTAINER_LIST, EntryItem.CONTAINER_LIST_INVENTORY);
    query.whereExists(EntryItem.KEY_EXPIRE_DATE);
    query.whereLessThan(EntryItem.KEY_EXPIRE_DATE, offsetDateFromToday(0));

    try {
      return query.find();
    } catch (ParseException e) {
      Log.d(TAG, "query failed: " + e);
      return EntryItem.DUMMY_ENTRY_ITEM_LIST;
    }
  }

  private static Date offsetDateFromToday(int offset) {
    Calendar offsetDate = Calendar.getInstance();
    offsetDate.add(Calendar.DATE, offset);
    return offsetDate.getTime();
  }
}