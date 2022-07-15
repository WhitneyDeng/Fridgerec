package com.example.fridgerec.model.viewmodel;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fridgerec.EntryItemQuery;
import com.example.fridgerec.interfaces.DatasetViewModel;
import com.example.fridgerec.model.EntryItem;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class InventoryViewModel extends ViewModel implements DatasetViewModel {
  public static final String TAG = "InventoryViewModel";
  private MutableLiveData<Boolean> refresh;
  private MutableLiveData<List<EntryItem>> inventoryList;
  private MutableLiveData<HashMap<String, List<EntryItem>>> inventoryMap;
  private MutableLiveData<HashMap<EntryItemQuery.SortFilter, Object>> sortFilterParams;

  private MutableLiveData<Boolean> inDeleteMode;
  private List<EntryItem> checkedItemsList;
  private HashSet<EntryItem> checkedItemsSet;

  private MutableLiveData<Boolean> parseOperationSuccess;
  private ParseException parseException;

  private Boolean inEditMode;
  private MutableLiveData<EntryItem> selectedEntryItem;

  public MutableLiveData<HashMap<EntryItemQuery.SortFilter, Object>> getSortFilterParams() {
    if (sortFilterParams == null) {
      sortFilterParams = new MutableLiveData<>();

      HashMap<EntryItemQuery.SortFilter, Object> map = new HashMap<>();
      map.put(EntryItemQuery.SortFilter.NONE, null);
      sortFilterParams.setValue(map);
    }
    return sortFilterParams;
  }

  public MutableLiveData<List<EntryItem>> getList() {
    if (inventoryList == null) {
      inventoryList = new MutableLiveData<>();
      inventoryList.setValue(new ArrayList<>());
    }
    return inventoryList;
  }

  public MutableLiveData<HashMap<String, List<EntryItem>>> getMap() {
    if (inventoryMap == null) {
      inventoryMap = new MutableLiveData<>();
      inventoryMap.setValue(new HashMap<>());
    }
    return inventoryMap;
  }

  public MutableLiveData<Boolean> getInDeleteMode() {
    if (inDeleteMode == null) {
      inDeleteMode = new MutableLiveData<>();
      inDeleteMode.setValue(false);
    }
    return inDeleteMode;
  }

  @Override
  public List<EntryItem> getCheckedItemsList() {
    if (checkedItemsList == null) {
      checkedItemsList = new ArrayList<>();
    }
    checkedItemsList.clear();
    checkedItemsList.addAll(checkedItemsSet);

    checkedItemsSet.clear();

    Log.i(TAG, "checkedItemsList: " + checkedItemsList.toString()); //TODO: testing
    return checkedItemsList;
  }

  @Override
  public HashSet<EntryItem> getCheckedItemsSet() {
    if (checkedItemsSet == null) {
      checkedItemsSet = new HashSet<>();
    }
    return checkedItemsSet;
  }

  public void refreshDataset() {
    Log.i(TAG, "refreshing inventory dataset");
    EntryItemQuery.queryEntryItems(this,
        EntryItem.CONTAINER_LIST_INVENTORY);
  }

  public MutableLiveData<EntryItem> getSelectedEntryItem() {
    if (selectedEntryItem == null) {
      selectedEntryItem = new MutableLiveData<>();
      selectedEntryItem.setValue(EntryItem.DUMMY_ENTRY_ITEM);
    }
    return selectedEntryItem;
  }

  public Boolean getInEditMode() {
    return inEditMode;
  }

  public void setInEditMode(Boolean b) {
    inEditMode = b;
  }

  public void deleteCheckedItems() {
    EntryItemQuery.deleteEntryItems(this);
  }

  public void saveEntryItem(EntryItem entryItem, FragmentActivity activity) {
    entryItem.setContainerList(EntryItem.CONTAINER_LIST_INVENTORY);
    entryItem.setUser(ParseUser.getCurrentUser());
    EntryItemQuery.saveNewEntry(entryItem, this, activity);
  }

  public MutableLiveData<Boolean> getParseOperationSuccess() {
    if (parseOperationSuccess == null) {
      parseOperationSuccess = new MutableLiveData<>();
    }
    return parseOperationSuccess;
  }

  public ParseException getParseException() {
    return parseException;
  }

  public void setParseException(ParseException e) {
    parseException = e;
  }
}
