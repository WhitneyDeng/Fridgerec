package com.example.fridgerec.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.Objects;

@ParseClassName("EntryItem")
public class EntryItem extends ParseObject {
  public static final String KEY_USER = "user";
  public static final String KEY_FOOD = "food";
  public static final String KEY_AMOUNT = "amount";
  public static final String KEY_AMOUNT_UNIT = "amountUnit";
  public static final String KEY_EXPIRE_DATE = "expireDate";
  public static final String KEY_SOURCE_DATE = "sourceDate";
  public static final String KEY_MAIN_FRESHNESS_METRIC = "mainFreshnessMetric";
  public static final String KEY_CONTAINER_LIST = "containerList";

  public static final String MAIN_FRESHNESS_METRIC_EXPIRE_DATE = "EXPIRE_DATE";
  public static final String MAIN_FRESHNESS_METRIC_SOURCE_DATE = "SOURCE_DATE";

  public static final String CONTAINER_LIST_INVENTORY = "INVENTORY";
  public static final String CONTAINER_LIST_SHOPPING = "SHOPPING";

  public static final EntryItem DUMMY_ENTRY_ITEM = null;

  public ParseUser getUser() {
    return getParseUser(KEY_USER);
  }

  public void setUser(ParseUser parseUser) {
    put(KEY_USER, parseUser);
  }

  public Food getFood() {
    return (Food) getParseObject(KEY_FOOD);
  }

  public void setFood(ParseObject food) {
    put(KEY_FOOD, food);
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

  public Date getExpireDate() {
    return getDate(KEY_EXPIRE_DATE);
  }

  public void setExpireDate(Date date) {
    put(KEY_EXPIRE_DATE, date);
  }

  public Date getSourceDate() {
    return getDate(KEY_SOURCE_DATE);
  }

  public void setSourceDate(Date date) {
    put(KEY_SOURCE_DATE, date);
  }

  public String getMainFreshnessMetric() {
    return getString(KEY_MAIN_FRESHNESS_METRIC);
  }

  public void setMainFreshnessMetric(String mainFreshnessMetric) {
    put(KEY_MAIN_FRESHNESS_METRIC, mainFreshnessMetric);
  }

  public String getContainerList() {
    return getString(KEY_CONTAINER_LIST);
  }

  public void setContainerList(String containerList) {
    put(KEY_CONTAINER_LIST, containerList);
  }

  public static boolean compareContent(EntryItem p, EntryItem n) {
    return Food.compareContent(p.getFood(), n.getFood())
        && p.getAmount() == n.getAmount()
        && Objects.equals(p.getAmountUnit(), n.getAmountUnit())
        && Objects.equals(p.getExpireDate(), n.getExpireDate())
        && Objects.equals(p.getSourceDate(), n.getSourceDate())
        && Objects.equals(p.getContainerList(), n.getContainerList());
  }
}
