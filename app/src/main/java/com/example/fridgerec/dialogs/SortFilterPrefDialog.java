package com.example.fridgerec.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fridgerec.EntryItemQuery;
import com.example.fridgerec.R;
import com.example.fridgerec.databinding.DialogSortFilterParamsBinding;
import com.example.fridgerec.databinding.FragmentInventoryBinding;
import com.example.fridgerec.model.viewmodel.InventoryViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.HashMap;

public class SortFilterPrefDialog extends DialogFragment{
  public static final String TAG = "SortFilterPrefDialog";

  private HashMap<String, EntryItemQuery.SortFilter> sortChipParamMap;
  private HashMap<String, EntryItemQuery.SortFilter> filterChipParamMap;

  private DialogSortFilterParamsBinding binding;


  private NavController navController;

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
    configChipMaps();
    onClickToolbarItem();
    onClickDatePickerBtn(binding.btnExpireBefore);
    onClickDatePickerBtn(binding.btnExpireAfter);
    onClickDatePickerBtn(binding.btnSourcedBefore);
    onClickDatePickerBtn(binding.btnSourcedAfter);
    //TODO: set default date picker to today
    //TODO: toggle visibility of linear layouts
  }

  private void onClickDatePickerBtn(Button btn) {
    MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select Date")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build();

    btn.setOnClickListener((new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        datePicker.show(getChildFragmentManager(), "DATE_PICKER");
      }
    }));
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

  private void configToolbar() {
    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

    binding.toolbar.setNavigationOnClickListener(v -> dismiss());
    binding.toolbar.setTitle("Sort and Filter");
    binding.toolbar.setOnMenuItemClickListener(item -> {
      dismiss();
      return true;
    });
  }

  private void onClickToolbarItem() {
    binding.toolbar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.miSave:
          InventoryViewModel model = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);
          model.getSortFilterParams().setValue(getSelectedParams());
          navController.navigateUp();
          return true;
        default:
          return false;
      }
    });
  }

  private HashMap<EntryItemQuery.SortFilter, Object> getSelectedParams() {
    HashMap<EntryItemQuery.SortFilter, Object> sortFilterParams = new HashMap<>();
    for (Integer id : binding.cgSort.getCheckedChipIds()) {
      String chipLabel = ((Chip) binding.cgSort.findViewById(id)).getText().toString();
      sortFilterParams.put(sortChipParamMap.get(chipLabel), null);
    }

    for (Integer id : binding.cgFilter.getCheckedChipIds()) {
      String chipLabel = ((Chip) binding.cgSort.findViewById(id)).getText().toString();
      EntryItemQuery.SortFilter sortFilterParam = sortChipParamMap.get(chipLabel);

      Object val = null;
      switch (sortFilterParam) {
        case FILTER_EXPIRE_BEFORE:
          //TODO: extract date
          break;
        case FILTER_EXPIRE_AFTER:
          //TODO: extract date
          break;
        case FILTER_SOURCED_BEFORE:
          //TODO: extract date
          break;
        case FILTER_SOURCED_AFTER:
          //TODO: extract date
          break;
        case FILTER_FOOD_GROUP:
          //TODO: extract selection
          break;
        default:
          Log.e(TAG, "filter chip not recognised");
      }
      sortFilterParams.put(sortFilterParam, val);
    }
    return sortFilterParams;
  }

  private void configChipMaps() {
    sortChipParamMap = new HashMap<>();
    sortChipParamMap.put(getResources().getString(R.string.chip_food_name), EntryItemQuery.SortFilter.SORT_FOOD_NAME);
    sortChipParamMap.put(getResources().getString(R.string.chip_food_group), EntryItemQuery.SortFilter.SORT_FOOD_GROUP);
    sortChipParamMap.put(getResources().getString(R.string.chip_source_date), EntryItemQuery.SortFilter.SORT_SOURCE_DATE);
    sortChipParamMap.put(getResources().getString(R.string.chip_expire_date), EntryItemQuery.SortFilter.SORT_EXPIRE_DATE);

    filterChipParamMap = new HashMap<>();
    filterChipParamMap.put(getResources().getString(R.string.chip_expire_before), EntryItemQuery.SortFilter.FILTER_EXPIRE_BEFORE);
    filterChipParamMap.put(getResources().getString(R.string.chip_expire_after), EntryItemQuery.SortFilter.FILTER_EXPIRE_AFTER);
    filterChipParamMap.put(getResources().getString(R.string.chip_sourced_before), EntryItemQuery.SortFilter.FILTER_SOURCED_BEFORE);
    filterChipParamMap.put(getResources().getString(R.string.chip_sourced_after), EntryItemQuery.SortFilter.FILTER_SOURCED_AFTER);
    filterChipParamMap.put(getResources().getString(R.string.chip_food_group), EntryItemQuery.SortFilter.FILTER_FOOD_GROUP);
  }
}