package com.example.fridgerec.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fridgerec.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryCreationFragment} factory method to
 * create an instance of this fragment.
 */
public class InventoryCreationFragment extends Fragment {

  private Toolbar toolbar;
  private NavController navController;
  private AppBarConfiguration appBarConfiguration;

  public InventoryCreationFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_inventory_creation, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = Navigation.findNavController(view);

    toolbar = view.findViewById(R.id.toolbar);

    setupToolbar();
    onClickToolbarItem();
  }

  private void setupToolbar() {
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
  }

  private void onClickToolbarItem() {
    toolbar.setOnMenuItemClickListener( item -> {
      switch (item.getItemId()) {
        case R.id.miSave:
          //TODO: save item
          navController.navigate(R.id.action_inventoryCreationFragment_to_inventoryFragment);
          return true;
        default:
          return false;
      }
    });
  }
}