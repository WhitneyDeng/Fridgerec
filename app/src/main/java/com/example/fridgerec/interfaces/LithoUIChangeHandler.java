package com.example.fridgerec.interfaces;

import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.model.EntryItemList;

import java.util.List;

public interface LithoUIChangeHandler {
  public abstract void setupLithoView(EntryItemList.SortFilter sortFilterParam, List<EntryItem> entryItems);
}
