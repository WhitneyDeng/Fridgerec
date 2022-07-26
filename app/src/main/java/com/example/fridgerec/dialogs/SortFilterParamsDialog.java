package com.example.fridgerec.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fridgerec.ParseClient;
import com.example.fridgerec.R;
import com.example.fridgerec.databinding.DialogSortFilterParamsBinding;
import com.example.fridgerec.interfaces.DatasetViewModel;
import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.model.viewmodel.InventoryViewModel;
import com.example.fridgerec.model.viewmodel.ShoppingViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SortFilterParamsDialog extends DialogFragment{
  public static final String TAG = "SortFilterParamsDialog";

  private HashMap<Integer, ParseClient.SortFilter> sortChipParamMap;
  private HashMap<Integer, ParseClient.SortFilter> filterChipParamMap;

  private DialogSortFilterParamsBinding binding;
  private NavController navController;
  private DatasetViewModel model;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = DialogSortFilterParamsBinding.inflate(getLayoutInflater(), container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = NavHostFragment.findNavController(this);

    configToolbar();
    configExposedDropdownMenu();
    onClickToolbarItem();
    configToggleFilterLayoutVisibility();

    if (SortFilterParamsDialogArgs.fromBundle(getArguments()).getContainerList() == EntryItem.CONTAINER_LIST_INVENTORY) {
      model = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);
      onClickDatePickerBtn(binding.btnExpireBefore);
      onClickDatePickerBtn(binding.btnExpireAfter);
      onClickDatePickerBtn(binding.btnSourcedBefore);
      onClickDatePickerBtn(binding.btnSourcedAfter);
    } else {
      model = new ViewModelProvider(requireActivity()).get(ShoppingViewModel.class);
      // TODO: hide expire buttons
      hideChip(binding.cSortSourceDate);
      hideChip(binding.cSortExpireDate);
      hideChip(binding.cFilterSourceBefore);
      hideChip(binding.cFilterSourceAfter);
      hideChip(binding.cFilterExpireBefore);
      hideChip(binding.cFilterExpireAfter);
    }
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

  private void hideChip(Chip chip) {
    chip.setVisibility(View.GONE);
  }

  private void configExposedDropdownMenu() {
    ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.item_dropdown, getResources().getStringArray(R.array.foodGroupStrings));
    binding.actvFoodGroup.setAdapter(arrayAdapter);
  }

  private void configToggleFilterLayoutVisibility() {
    onChipCheckedChanged(binding.cFilterExpireBefore, binding.llFilterExpireBefore);
    onChipCheckedChanged(binding.cFilterExpireAfter, binding.llFilterExpireAfter);
    onChipCheckedChanged(binding.cFilterSourceBefore, binding.llFilterSourcedBefore);
    onChipCheckedChanged(binding.cFilterSourceAfter, binding.llFilterSourcedAfter);
    onChipCheckedChanged(binding.cFilterFoodGroup, binding.llFilterFoodGroup);
  }

  private void onChipCheckedChanged(Chip chip, LinearLayout ll) {
    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          ll.setVisibility(LinearLayout.VISIBLE);
        } else {
          ll.setVisibility(LinearLayout.GONE);
        }
      }
    });
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

  private void configToolbar() {
    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

    binding.toolbar.setNavigationOnClickListener(v -> dismiss());
    binding.toolbar.setTitle("select Sort & Filter");
    binding.toolbar.setOnMenuItemClickListener(item -> {
      dismiss();
      return true;
    });
  }

  private void onClickToolbarItem() {
    binding.toolbar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.miSave:
          model.getSortFilterParams().setValue(getSelectedParams());
          navController.navigateUp();
          return true;
        default:
          return false;
      }
    });
  }

  private HashMap<ParseClient.SortFilter, Object> getSelectedParams() {
    configChipMaps();
    HashMap<ParseClient.SortFilter, Object> sortFilterParams = new HashMap<>();

    for (Integer id : binding.cgSort.getCheckedChipIds()) {
      sortFilterParams.put(sortChipParamMap.get(id), null);
    }

    for (Integer id : binding.cgFilter.getCheckedChipIds()) {
      ParseClient.SortFilter sortFilterParam = filterChipParamMap.get(id);

      Object val = null;
      switch (sortFilterParam) {
        case FILTER_EXPIRE_BEFORE:
          val = extractDate(binding.btnExpireBefore);
          break;
        case FILTER_EXPIRE_AFTER:
          val = extractDate(binding.btnExpireAfter);
          break;
        case FILTER_SOURCED_BEFORE:
          val = extractDate(binding.btnSourcedBefore);
          break;
        case FILTER_SOURCED_AFTER:
          val = extractDate(binding.btnSourcedAfter);
          break;
        case FILTER_FOOD_GROUP:
          val = extractFoodGroupSelection();
          Log.i(TAG, "food group filter selected: " + val);
          break;
        default:
          Log.e(TAG, "filter chip not recognised");
      }

      if (val != null) {
        sortFilterParams.put(sortFilterParam, val);
      }
    }
    return sortFilterParams;
  }

  private String extractFoodGroupSelection() {
    String selectedFoodGroup = binding.tilFoodGroup.getEditText().getText().toString();

    if (selectedFoodGroup.isEmpty()) {
      Toast.makeText(getContext(), "no food group selected", Toast.LENGTH_LONG).show();
      return null;
    }
    return selectedFoodGroup;
  }

  private Date extractDate(Button btnDatepicker) {
    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
    try {
      return formatter.parse(btnDatepicker.getText().toString());
    } catch (ParseException e) {
      Log.e(TAG, "unable to extract date from" + btnDatepicker.getText().toString());
      Toast.makeText(getContext(), "no date selected", Toast.LENGTH_SHORT).show();
      e.printStackTrace();
      return null;
    }
  }

  private void configChipMaps() {
    sortChipParamMap = new HashMap<>();
    sortChipParamMap.put(R.id.cSortFoodName, ParseClient.SortFilter.SORT_FOOD_NAME);
    sortChipParamMap.put(R.id.cSortFoodGroup, ParseClient.SortFilter.SORT_FOOD_GROUP);
    sortChipParamMap.put(R.id.cSortSourceDate, ParseClient.SortFilter.SORT_SOURCE_DATE);
    sortChipParamMap.put(R.id.cSortExpireDate, ParseClient.SortFilter.SORT_EXPIRE_DATE);

    filterChipParamMap = new HashMap<>();
    filterChipParamMap.put(R.id.cFilterExpireBefore, ParseClient.SortFilter.FILTER_EXPIRE_BEFORE);
    filterChipParamMap.put(R.id.cFilterExpireAfter, ParseClient.SortFilter.FILTER_EXPIRE_AFTER);
    filterChipParamMap.put(R.id.cFilterSourceBefore, ParseClient.SortFilter.FILTER_SOURCED_BEFORE);
    filterChipParamMap.put(R.id.cFilterSourceAfter, ParseClient.SortFilter.FILTER_SOURCED_AFTER);
    filterChipParamMap.put(R.id.cFilterFoodGroup, ParseClient.SortFilter.FILTER_FOOD_GROUP);
  }
}