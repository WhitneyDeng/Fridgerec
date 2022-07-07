package com.example.fridgerec.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;

public class InventoryViewModel extends ViewModel {
  private MutableLiveData<List<EntryItem>> inventoryList;
  private MutableLiveData<HashMap<String, EntryItem>> inventoryMap;
  private MutableLiveData<HashMap<EntryItemList.SortFilter, Object>> sortFilterParams;

  public MutableLiveData<HashMap<EntryItemList.SortFilter, Object>> getSortFilterParams() {
    if (sortFilterParams == null) {
      sortFilterParams = new MutableLiveData<>();
      sortFilterParams.getValue().put(EntryItemList.SortFilter.NONE, null);
    }
    return sortFilterParams;
  }

  public MutableLiveData<List<EntryItem>> getInventoryList() {
    if (inventoryList == null) {
      inventoryList = new MutableLiveData<>();
    }
    return inventoryList;
  }

  public MutableLiveData<HashMap<String, EntryItem>> getInventoryMap() {
    if (inventoryMap == null) {
      inventoryMap = new MutableLiveData<>();
    }
    return inventoryMap;
  }
}
