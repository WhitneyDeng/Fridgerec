package com.example.fridgerec.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fridgerec.R;

public class SortFilterPrefDialog extends DialogFragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener{
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_sort_filter_pref, container, false);
  }

  @Override
  public void onCancel(DialogInterface dialog) {

  }

  @Override
  public void onDismiss(DialogInterface dialog) {

  }
}
