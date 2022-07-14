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
import android.widget.Button;
import android.widget.Toast;

import com.example.fridgerec.EntryItemQuery;
import com.example.fridgerec.R;
import com.example.fridgerec.databinding.FragmentInventoryCreationBinding;
import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.model.Food;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    onClickDatePickerBtn(binding.btnSourceDate);
    onClickDatePickerBtn(binding.btnExpireDate);
    onClickToolbarItem();
  }

  private void configExposedDropdownMenu(AutoCompleteTextView actv, String[] optionStrings) {
    ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.item_dropdown_foodgroup, optionStrings);
    actv.setAdapter(arrayAdapter);
  }

  private void onClickDatePickerBtn(Button btn) {
    MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select Date")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build();

    btn.setOnClickListener((new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        datePicker.show(getChildFragmentManager(), "DATE_PICKER");
      }
    }));

    datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
      @Override
      public void onPositiveButtonClick(Object selection) {
        btn.setText(datePicker.getHeaderText());
      }
    });
  }

  private void setupToolbar() {
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
  }

  private void onClickToolbarItem() {
    toolbar.setOnMenuItemClickListener( item -> {
      switch (item.getItemId()) {
        case R.id.miSave:
          EntryItem entryItem = new EntryItem();
          if (extractData(entryItem)) {
            entryItem.setContainerList(EntryItem.CONTAINER_LIST_INVENTORY);
            EntryItemQuery.saveNewEntry(entryItem);
            navController.navigate(R.id.action_inventoryCreationFragment_to_inventoryFragment);
          }
          return true;
        default:
          return false;
      }
    });
  }

  private boolean extractData(EntryItem entryItem) {
    Food food = new Food();
    //TODO: save set food apiId upone autocomplete selection

    String foodName = extractString(binding.tilFood);
    if (foodName == null) {
      Toast.makeText(getContext(), "error: must enter food", Toast.LENGTH_LONG).show();
      return false;
    }
    food.setFoodName(foodName);

    String foodGroup = extractString(binding.tilFoodGroup);
    if (foodGroup != null) {
      food.setFoodGroup(foodGroup);
    }
    Log.i(TAG, "food group: " + foodGroup);

    entryItem.setFood(food);

    try {
      int amount = Integer.parseInt(extractString(binding.tilAmount));

      if (amount < 0) {
        Toast.makeText(getContext(), "error: amount must be non negative", Toast.LENGTH_LONG).show();
        throw new NumberFormatException();
      }

      String amountUnit = extractString(binding.tilAmountUnit);
      if (amountUnit == null) { // implicit: amount is nonempty
        Toast.makeText(getContext(), "error: missing unit", Toast.LENGTH_LONG).show();
        return false;
      }
      entryItem.setAmountUnit(amountUnit);

      Log.i(TAG, "amount: " + amount);
      Log.i(TAG, "amount unit: " + amountUnit);
    } catch (NumberFormatException e) {
      Log.i(TAG, "no amount extracted");
    }

    Date sourceDate = extractDate(binding.btnSourceDate);
    if (sourceDate == null) {
      sourceDate = new Date(System.currentTimeMillis());
      Toast.makeText(getContext(), "no source date selected (default to today)", Toast.LENGTH_SHORT).show();
    }
    entryItem.setSourceDate(sourceDate);

    Date expireDate = extractDate(binding.btnExpireDate);
    if (expireDate != null) {
      if (expireDate.before(sourceDate)) {
        Toast.makeText(getContext(), "error: expire date must be after source date", Toast.LENGTH_LONG).show();
        return false;
      }
      entryItem.setExpireDate(expireDate);
    }

    Log.i(TAG, "food group: " + foodGroup);
    Log.i(TAG, "source date: " + sourceDate);
    Log.i(TAG, "expire date: " + expireDate);
    return true;
  }

  private String extractString(TextInputLayout til) {
    String s = til.getEditText().getText().toString();
    if (s.isEmpty()) {
      return null;
    }
    return s;
  }

  private Date extractDate(Button btnDatepicker) {
    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
    try {
      return formatter.parse(btnDatepicker.getText().toString());
    } catch (ParseException e) {
      Log.e(TAG, "unable to extract date from: " + btnDatepicker.getText().toString());
      e.printStackTrace();
      return null;
    }
  }
}