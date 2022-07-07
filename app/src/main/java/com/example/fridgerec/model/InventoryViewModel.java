package com.example.fridgerec.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;

public class InventoryViewModel extends ViewModel {
  private MutableLiveData<List<EntryItem>> inventoryList;
  private MutableLiveData<HashMap<String, EntryItem>> inventoryMap;
  private MutableLiveData<HashMap<EntryItemList.SortFilter, Object>> sortFilterParams;

  //TODO: tracks selecdted sort & filter params
  //TODO: tracks inventoryList or inventoryMap

  //TODO:
}
