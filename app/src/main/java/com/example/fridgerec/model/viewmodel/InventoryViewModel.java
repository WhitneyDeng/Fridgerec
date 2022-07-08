package com.example.fridgerec.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fridgerec.EntryItemQuery;
import com.example.fridgerec.interfaces.DatasetViewModel;
import com.example.fridgerec.model.EntryItem;

import java.util.HashMap;
import java.util.List;

public class InventoryViewModel extends ViewModel implements DatasetViewModel {
  private MutableLiveData<Boolean> refresh;
  private MutableLiveData<List<EntryItem>> inventoryList;
  private MutableLiveData<HashMap<String, List<EntryItem>>> inventoryMap;
  private MutableLiveData<HashMap<EntryItemQuery.SortFilter, Object>> sortFilterParams;

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
    }
    return inventoryList;
  }

  public MutableLiveData<HashMap<String, List<EntryItem>>> getMap() {
    if (inventoryMap == null) {
      inventoryMap = new MutableLiveData<>();
    }
    return inventoryMap;
  }
}
