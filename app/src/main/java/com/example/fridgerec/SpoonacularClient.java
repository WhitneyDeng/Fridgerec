package com.example.fridgerec;

import android.content.Context;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fridgerec.interfaces.SetAutocompleteTextViewAdapterCallback;
import com.example.fridgerec.model.Food;

import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.Headers;

public class SpoonacularClient {
  public static final String TAG = "SpoonacularClient";
  public static final String BASE_PATH = "https://api.spoonacular.com";

  public static void getAutocompleteSuggestions(Context context, String query, AutoCompleteTextView actv, SetAutocompleteTextViewAdapterCallback view) {
    String autocompleteEndptPath = "/food/ingredients/autocomplete";

    AsyncHttpClient client = new AsyncHttpClient();
    RequestParams params = getRequestParamsObj(context);
    params.put("query", query);
    params.put("metaInformation", true);

    client.get(BASE_PATH + autocompleteEndptPath, params, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json) {
        Log.i(TAG, "[autocomplete query onSuccess]");

        JSONArray results = json.jsonArray;
        Log.i(TAG, "results" + results.toString()); //testing

        try {
          view.setAutocompleteTextViewAdapter(actv, Food.fromJsonArray(results));
        } catch (JSONException e) {
          e.printStackTrace();
        }

        // TODO: extract food objects from result
        // TODO: attach new adapter
      }

      @Override
      public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
        Log.e(TAG, String.format("[autocomplete query onFailure] status code: %d | throwable: %s", statusCode, throwable));
      }
    });
  }

  private static RequestParams getRequestParamsObj(Context context) {
    RequestParams params = new RequestParams();
    params.put("apiKey", context.getString(R.string.spoonacular_api_key));
    return params;
  }
}
