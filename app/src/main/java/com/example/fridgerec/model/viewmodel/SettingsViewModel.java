package com.example.fridgerec.model.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fridgerec.ParseClient;
import com.example.fridgerec.interfaces.ParseCallback;
import com.example.fridgerec.model.Settings;
import com.parse.ParseException;

public class SettingsViewModel extends ViewModel implements ParseCallback {
  private Settings userSettings;
  private MutableLiveData<Boolean> parseOperationSuccess;
  private ParseException parseException;

  public Settings getUserSettings() {
    if (userSettings == null) {
      userSettings = ParseClient.getCurrentUserSettings();
    }
    return userSettings;
  }

  public void saveUserSettings() {
    ParseClient.saveCurrentUserSettings(userSettings, this);
  }

  @Override
  public MutableLiveData<Boolean> getParseOperationSuccess() {
    if (parseOperationSuccess == null) {
      parseOperationSuccess = new MutableLiveData<>();
    }
    return parseOperationSuccess;
  }

  @Override
  public ParseException getParseException() {
    return parseException;
  }

  @Override
  public void setParseException(ParseException e) {
    parseException = e;
  }
}
