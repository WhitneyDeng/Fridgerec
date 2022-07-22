package com.example.fridgerec.interfaces;

import android.widget.AutoCompleteTextView;

import com.example.fridgerec.model.Food;

import java.util.List;

public interface FoodAutocompleteSearchCallback {
  public void setAutocompleteTextViewAdapter(AutoCompleteTextView actv, List<Food> suggestions);
  public void onSuggestionSelected(Food selectedFood);
}
