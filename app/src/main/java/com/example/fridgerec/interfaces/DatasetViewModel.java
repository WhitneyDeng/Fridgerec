package com.example.fridgerec.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.EntryItemQuery;

import java.util.HashMap;
import java.util.List;

public interface DatasetViewModel {
  public MutableLiveData<HashMap<EntryItemQuery.SortFilter, Object>> getSortFilterParams();
  public MutableLiveData<List<EntryItem>> getList();
  public MutableLiveData<HashMap<String, List<EntryItem>>> getMap();
  public MutableLiveData<Boolean> getInDeleteMode();
  public MutableLiveData<List<EntryItem>> getCheckedItems();
}
