package com.example.fridgerec.fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fridgerec.R;
import com.example.fridgerec.interfaces.DatasetViewModel;
import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.model.Food;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class CreationFragment extends Fragment {
  public static final String TAG = "CreationFragment";
  protected final SimpleDateFormat datepickerFormatter = new SimpleDateFormat("MMM dd, yyyy");

  protected DatasetViewModel model;
  protected NavController navController;
  private AppBarConfiguration appBarConfiguration;

  protected abstract void populateEntryItemDetail(EntryItem entryItem);
  protected abstract void observeParseOperationSuccess();
  protected abstract EntryItem extractData();

  protected void setupToolbar(Toolbar toolbar) {
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

    onClickToolbarItem(toolbar);
  }

  protected void configExposedDropdownMenu(AutoCompleteTextView actv, String[] optionStrings) {
    ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.item_dropdown_foodgroup, optionStrings);
    actv.setAdapter(arrayAdapter);
  }

  protected void onClickDatePickerBtn(Button btn) {
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

  protected void onClickToolbarItem(Toolbar toolbar) {
    toolbar.setOnMenuItemClickListener( item -> {
      switch (item.getItemId()) {
        case R.id.miSave:
          EntryItem entryItem = extractData();
          if (entryItem != EntryItem.DUMMY_ENTRY_ITEM) {
            observeParseOperationSuccess();

            entryItem.setContainerList(EntryItem.CONTAINER_LIST_INVENTORY);
            model.saveEntryItem(entryItem, requireActivity());
          }
          return true;
        default:
          return false;
      }
    });
  }

  protected void populateString(String s, TextInputLayout til) {
    if (s != null) {
      til.getEditText().setText(s);
    }
  }

  protected void populateDate(Date d, Button btn) {
    if (d != null) {
      btn.setText(datepickerFormatter.format(d));
    }
  }

  protected EntryItem getEntryItem() {
    if (Boolean.TRUE.equals(model.getInEditMode().getValue())) {
      return model.getSelectedEntryItem();
    } else {
      return new EntryItem();
    }
  }

  protected Food extractFood(TextInputLayout tilFood, TextInputLayout tilFoodGroup) {
    Food food = new Food();
    //TODO: save set food apiId upone autocomplete selection

    String foodName = extractString(tilFood);
    if (foodName == null) {
      Toast.makeText(getContext(), "error: must enter food", Toast.LENGTH_LONG).show();
      return Food.DUMMY_FOOD;
    }
    food.setFoodName(foodName);

    String foodGroup = extractString(tilFoodGroup);
    if (foodGroup != null) {
      food.setFoodGroup(foodGroup);
    }
    Log.i(TAG, "food group: " + foodGroup);

    return food;
  }

  protected EntryItem extractAmountInfo(EntryItem entryItem, TextInputLayout tilAmount, TextInputLayout tilAmountUnit) {
    try {
      int amount = Integer.parseInt(extractString(tilAmount));

      if (amount < 0) {
        Toast.makeText(getContext(), "error: amount must be non negative", Toast.LENGTH_LONG).show();
        throw new NumberFormatException();
      }

      entryItem.setAmount(amount);

      String amountUnit = extractString(tilAmountUnit);
      if (amountUnit == null) { // implicit: amount is nonempty
        Toast.makeText(getContext(), "error: missing unit", Toast.LENGTH_LONG).show();
        return EntryItem.DUMMY_ENTRY_ITEM;
      }
      entryItem.setAmountUnit(amountUnit);

      Log.i(TAG, "amount: " + amount);
      Log.i(TAG, "amount unit: " + amountUnit);
    } catch (NumberFormatException e) {
      Log.i(TAG, "no amount extracted");
    }
    return entryItem;
  }

  protected EntryItem extractSourceExpireDates(EntryItem entryItem, Button btnSourceDate, Button btnExpireDate) {
    Date sourceDate = extractDate(btnSourceDate);
    if (sourceDate == null) {
      sourceDate = new Date(System.currentTimeMillis());
      Toast.makeText(getContext(), "no source date selected (default to today)", Toast.LENGTH_SHORT).show();
    }
    entryItem.setSourceDate(sourceDate);

    Date expireDate = extractDate(btnExpireDate);
    if (expireDate != null) {
      if (expireDate.before(sourceDate)) {
        Toast.makeText(getContext(), "error: expire date must be after source date", Toast.LENGTH_LONG).show();
        return EntryItem.DUMMY_ENTRY_ITEM;
      }
      entryItem.setExpireDate(expireDate);
    }

    Log.i(TAG, "source date: " + sourceDate);
    Log.i(TAG, "expire date: " + expireDate);
    return entryItem;
  }

  private String extractString(TextInputLayout til) {
    String s = til.getEditText().getText().toString();
    if (s.isEmpty()) {
      return null;
    }
    return s;
  }

  private Date extractDate(Button btnDatepicker) {
    try {
      return datepickerFormatter.parse(btnDatepicker.getText().toString());
    } catch (ParseException e) {
      Log.e(TAG, "unable to extract date from: " + btnDatepicker.getText().toString());
      e.printStackTrace();
      return null;
    }
  }
}
