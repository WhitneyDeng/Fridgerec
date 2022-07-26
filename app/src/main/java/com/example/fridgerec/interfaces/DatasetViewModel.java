package com.example.fridgerec.interfaces;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.ParseClient;
import com.example.fridgerec.model.Food;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface DatasetViewModel extends ParseCallback {
  public String getContainerList();
  public MutableLiveData<HashMap<ParseClient.SortFilter, Object>> getSortFilterParams();
  public MutableLiveData<List<EntryItem>> getList();
  public MutableLiveData<HashMap<String, List<EntryItem>>> getMap();
  public List<EntryItem> getCheckedItemsList();
  public HashSet<EntryItem> getCheckedItemsSet();
  public EntryItem getSelectedEntryItem();
  public void setSelectedEntryItem(EntryItem e);
  public MutableLiveData<Boolean> getInDeleteMode();
  public MutableLiveData<Boolean> getInEditMode();
  public void refreshDataset();
  public void saveEntryItem(EntryItem entryItem, FragmentActivity activity);
  public void enterReadMode();
  public void deleteCheckedItems();
  public Food getSelectedFoodSuggestion();
  public void setSelectedFoodSuggestion(Food food);
}
