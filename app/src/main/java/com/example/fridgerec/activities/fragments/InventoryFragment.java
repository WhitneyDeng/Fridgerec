package com.example.fridgerec.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fridgerec.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryFragment} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends Fragment {
  private AppBarConfiguration appBarConfiguration;
  private NavController navController;

  private RecyclerView rvInventoryList;
  private Toolbar toolbar;
  private FloatingActionButton fab;

  public InventoryFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_inventory, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = Navigation.findNavController(view);

    rvInventoryList = view.findViewById(R.id.rvInventoryList);
    fab = view.findViewById(R.id.fab);
    toolbar = view.findViewById(R.id.toolbar);

    setupToolbar();

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Navigation.findNavController(view).navigate(R.id.action_inventoryFragment_to_inventoryCreationFragment);
      }
    });
  }

  private void setupToolbar() {
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
  }
}