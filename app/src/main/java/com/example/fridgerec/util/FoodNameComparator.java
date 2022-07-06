package com.example.fridgerec.util;

import com.example.fridgerec.model.EntryItem;

import java.util.Comparator;

public class FoodNameComparator implements Comparator<EntryItem> {
  @Override
  public int compare(EntryItem o1, EntryItem o2) {
    return o1.getFood().getFoodName().compareTo(o2.getFood().getFoodName());
  }
}
