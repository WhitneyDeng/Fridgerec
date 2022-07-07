package com.example.fridgerec.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fridgerec.R;
import com.example.fridgerec.model.EntryItemList;

public class SortFilterPrefDialog extends DialogFragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener{
  private Toolbar toolbar;
  private NavController navController;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_sort_filter_pref, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = NavHostFragment.findNavController(this);
    toolbar = view.findViewById(R.id.toolbar);

    configureToolbar();
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

  @Override
  public void onCancel(DialogInterface dialog) {

  }

  @Override
  public void onDismiss(DialogInterface dialog) {

  }
}
