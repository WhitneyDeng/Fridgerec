package com.example.fridgerec.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("ShoppingItem")
public class ShoppingItem extends ParseObject {
  public static final String KEY_USER = "user";
  public static final String KEY_AMOUNT = "amount";
  public static final String KEY_AMOUNT_UNIT = "amountUnit";
  public static final String KEY_FOOD = "food";

  public ParseUser getUser() {
    return getParseUser(KEY_USER);
  }

  public void setUser(ParseUser parseUser) {
    put(KEY_USER, parseUser);
  }

  public int getAmount() {
    return getInt(KEY_AMOUNT);
  }

  public void setAmount(int amount) {
    put(KEY_AMOUNT, amount);
  }

  public String getAmountUnit() {
    return getString(KEY_AMOUNT_UNIT);
  }

  public void setAmountUnit(String amountUnit) {
    put(KEY_AMOUNT_UNIT, amountUnit);
  }

  public ParseObject getFood() {
    return getParseObject(KEY_FOOD);
  }

  public void setFood(ParseObject food)
  {
    put(KEY_FOOD, food);
  }
}
