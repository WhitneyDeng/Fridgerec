package com.example.fridgerec;

import android.content.Context;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import okhttp3.Headers;

public class SpoonacularClient {
  public static final String TAG = "SpoonacularClient";
  public static final String BASE_PATH = "https://api.spoonacular.com";


  public static void setAutocompleteAdapter(Context context, String query) {
    String autocompleteEndptPath = "/food/ingredients/autocomplete";

    AsyncHttpClient client = new AsyncHttpClient();
    RequestParams params = new RequestParams();
    params.put("apiKey", context.getString(R.string.spoonacular_api_key));
    params.put("query", query);
    params.put("metaInformation", true);

    client.get(BASE_PATH + autocompleteEndptPath, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json) {
        Log.i(TAG, "[autocomplete query onSuccess]");
        // TODO: extract food objects from result
        // TODO: attach new adapter
      }

      @Override
      public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
        Log.e(TAG, String.format("[autocomplete query onFailure] status code: %d | response: %s", statusCode, response));
      }
    });
  }
}
