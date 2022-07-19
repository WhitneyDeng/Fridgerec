package com.example.fridgerec.interfaces;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.EntryItemQuery;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface DatasetViewModel {
  public String getContainerList();
  public MutableLiveData<HashMap<EntryItemQuery.SortFilter, Object>> getSortFilterParams();
  public MutableLiveData<List<EntryItem>> getList();
  public MutableLiveData<HashMap<String, List<EntryItem>>> getMap();
  public MutableLiveData<Boolean> getInDeleteMode();
  public List<EntryItem> getCheckedItemsList();
  public HashSet<EntryItem> getCheckedItemsSet();
  public MutableLiveData<Boolean> getParseOperationSuccess();
  public EntryItem getSelectedEntryItem();
  public void setSelectedEntryItem(EntryItem e);
  public MutableLiveData<Boolean> getInEditMode();
  public ParseException getParseException();
  public void setParseException(ParseException e);
  public void refreshDataset();
  public void saveEntryItem(EntryItem entryItem, FragmentActivity activity);
  public void enterReadMode();
  public void deleteCheckedItems();
}
