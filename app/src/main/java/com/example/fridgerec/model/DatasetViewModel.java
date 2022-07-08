package com.example.fridgerec.model;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.List;

public interface DatasetViewModel {
  public MutableLiveData<HashMap<EntryItemQuery.SortFilter, Object>> getSortFilterParams();
  public MutableLiveData<List<EntryItem>> getList();
  public MutableLiveData<HashMap<String, List<EntryItem>>> getMap();
}
