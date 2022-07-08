package com.example.fridgerec.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fridgerec.R;

public class SortFilterPrefDialog extends DialogFragment{
  public static final String TAG = "SortFilterPrefDialog";

  private Toolbar toolbar;
  private NavController navController;
  private SortFilterPrefDialogListener listener;

  public interface SortFilterPrefDialogListener {
    public void onDialogPositiveClick(DialogFragment dialog);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_sort_filter_pref, container, false);
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);

  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = NavHostFragment.findNavController(this);
    toolbar = view.findViewById(R.id.toolbar);

    configureToolbar();

    //TODO: set default date picker to today
    //TODO: toggle visibility of linear layouts
  }

  @Override
  public void onStart() {
    super.onStart();
    Dialog dialog = getDialog();
    if (dialog != null) {
      int width = ViewGroup.LayoutParams.MATCH_PARENT;
      int height = ViewGroup.LayoutParams.MATCH_PARENT;
      dialog.getWindow().setLayout(width, height);
    }
  }

  private void configureToolbar() {
    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

    toolbar.setNavigationOnClickListener(v -> dismiss());
    toolbar.setTitle("Sort and Filter");
    toolbar.setOnMenuItemClickListener(item -> {
      dismiss();
      return true;
    });
  }

  private void onClickToolbarItem() {
    toolbar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.miSave:

          return true;
        default:
          return false;
      }
    });
  }
}
