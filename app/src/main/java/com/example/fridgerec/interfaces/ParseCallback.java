package com.example.fridgerec.interfaces;

import androidx.lifecycle.MutableLiveData;

import com.parse.ParseException;

public interface ParseCallback {
  public MutableLiveData<Boolean> getParseOperationSuccess();
  public ParseException getParseException();
  public void setParseException(ParseException e);
}
