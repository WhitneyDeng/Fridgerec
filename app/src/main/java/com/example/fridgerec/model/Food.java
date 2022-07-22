package com.example.fridgerec.model;

import com.parse.ParseClassName;import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ParseClassName("Food")
public class Food extends ParseObject {
  public static final String KEY_FOOD_NAME = "foodName";
  public static final String KEY_FOOD_GROUP = "foodGroup";
  public static final String KEY_API_ID = "apiId";
  public static final String KEY_IMAGE = "imageUrl";

  public static final Food DUMMY_FOOD = null;
  public static final String NO_FOOD_GROUP = "(no food group assigned)";

  public static final String IMAGE_SIZE = "250x250";
  public static final String IMAGE_BASEPATH = "https://spoonacular.com/cdn/ingredients_" + IMAGE_SIZE + "/";

  public Food() {}

  // TODO: remove hard coded json key (to adapt to different APIs)
  public Food(JSONObject jsonObject) throws JSONException {
    setFoodName(jsonObject.getString("name"));
    setFoodGroup(jsonObject.getString("aisle"));
    setApiId(Integer.toString(jsonObject.getInt("id")));
    setImageUrl(IMAGE_BASEPATH + jsonObject.getString("image"));
  }

  public static List<Food> fromJsonArray(JSONArray foodsJsonArray) throws JSONException {
    List<Food> foods = new ArrayList<>();
    for (int i = 0; i < foodsJsonArray.length(); i++) {
      foods.add(new Food(foodsJsonArray.getJSONObject(i)));
    }
    return foods;
  }

  public String getFoodName() {
    return getString(KEY_FOOD_NAME);
  }

  public void setFoodName(String itemName) {
    put(KEY_FOOD_NAME, itemName);
  }

  public String getFoodGroup() {
    return getString(KEY_FOOD_GROUP);
  }

  public void setFoodGroup(String foodCategory)
  {
    put(KEY_FOOD_GROUP, foodCategory);
  }

  public String getApiId() {
    return getString(KEY_API_ID);
  }

  public void setApiId(String apiId) {
    put(KEY_API_ID, apiId);
  }

  public String getImageUrl() {
    return getString(KEY_IMAGE);
  }

  public void setImageUrl(String parseFile)
  {
    put(KEY_IMAGE, parseFile);
  }

  public static boolean compareContent(Food p, Food n) {
    return Objects.equals(p.getFoodName(), n.getFoodName())
        && Objects.equals(p.getFoodGroup(), n.getFoodGroup())
        && Objects.equals(p.getApiId(), n.getApiId())
        && Objects.equals(p.getImageUrl(), n.getImageUrl());
  }

  public String toString() {
    return getFoodName();
  }
}
