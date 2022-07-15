package com.example.fridgerec.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.EntryItemQuery;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface DatasetViewModel {
  public MutableLiveData<HashMap<EntryItemQuery.SortFilter, Object>> getSortFilterParams();
  public MutableLiveData<List<EntryItem>> getList();
  public MutableLiveData<HashMap<String, List<EntryItem>>> getMap();
  public MutableLiveData<Boolean> getInDeleteMode();
  public List<EntryItem> getCheckedItemsList();
  public HashSet<EntryItem> getCheckedItemsSet();
  public MutableLiveData<Boolean> getParseOperationSuccess();
  public MutableLiveData<EntryItem> getSelectedEntryItem();
  public Boolean getInEditMode();
  public void setInEditMode(Boolean b);
  public void setParseException(ParseException e);
  public void refreshDataset();

}
