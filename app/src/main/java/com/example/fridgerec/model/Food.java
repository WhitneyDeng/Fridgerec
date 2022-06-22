package com.example.fridgerec.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Food ")
public class Food extends ParseObject {
  public static final String KEY_ITEM_NAME = "itemName";
  public static final String KEY_FOOD_CATEGORY = "foodCategory";
  public static final String KEY_API_ID = "apiId";
  public static final String KEY_IMAGE = "image";

  public String getItemName() {
    return getString(KEY_ITEM_NAME);
  }

  public void setItemName(String itemName) {
    put(KEY_ITEM_NAME, itemName);
  }

  public String getFoodCategory() {
    return getString(KEY_FOOD_CATEGORY);
  }

  public void setFoodCategory(String foodCategory)
  {
    put(KEY_FOOD_CATEGORY, foodCategory);
  }

  public String getApiId() {
    return getString(KEY_API_ID);
  }

  public void setApiId(String apiId) {
    put(KEY_API_ID, apiId);
  }

  public ParseFile getImage() {
    return getParseFile(KEY_IMAGE);
  }

  public void setImage(ParseFile parseFile)
  {
    put(KEY_IMAGE, parseFile);
  }
}
