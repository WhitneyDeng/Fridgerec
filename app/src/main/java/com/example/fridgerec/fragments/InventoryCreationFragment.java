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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.fridgerec.R;
import com.example.fridgerec.databinding.FragmentInventoryCreationBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryCreationFragment} factory method to
 * create an instance of this fragment.
 */
public class InventoryCreationFragment extends Fragment {
  public static final String TAG = "InventoryCreationFragmemt";

  private Toolbar toolbar;
  private NavController navController;
  private AppBarConfiguration appBarConfiguration;

  private FragmentInventoryCreationBinding binding;

  public InventoryCreationFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentInventoryCreationBinding.inflate(getLayoutInflater(), container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = Navigation.findNavController(view);

    toolbar = binding.toolbar;

    setupToolbar();
    configExposedDropdownMenu(binding.actvFoodGroup, getResources().getStringArray(R.array.foodGroupStrings));
    configExposedDropdownMenu(binding.actvAmountUnit, getResources().getStringArray(R.array.amountUnitStrings));
    onClickToolbarItem();
  }

  private void configExposedDropdownMenu(AutoCompleteTextView actv, String[] optionStrings) {
    ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.item_dropdown_foodgroup, optionStrings);
    actv.setAdapter(arrayAdapter);
  }

  private void setupToolbar() {
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
  }

  private void onClickToolbarItem() {
    toolbar.setOnMenuItemClickListener( item -> {
      switch (item.getItemId()) {
        case R.id.miSave:
          extractData();
          navController.navigate(R.id.action_inventoryCreationFragment_to_inventoryFragment);
          return true;
        default:
          return false;
      }
    });
  }

  private void extractData() {
    Log.i(TAG, "amount unit: " + extractAmountUnitSelection());
  }

  private String extractAmountUnitSelection() {
    String selectedAmountUnit = binding.tilAmountUnit.getEditText().getText().toString();
    if (selectedAmountUnit.isEmpty()) {
      Toast.makeText(getContext(), "no food group selected", Toast.LENGTH_LONG).show();
      return null;
    }
    return selectedAmountUnit;
  }
}