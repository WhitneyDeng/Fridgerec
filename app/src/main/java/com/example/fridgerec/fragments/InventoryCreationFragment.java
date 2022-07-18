package com.example.fridgerec.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fridgerec.R;
import com.example.fridgerec.databinding.FragmentInventoryCreationBinding;
import com.example.fridgerec.fragments.basefragments.CreationBaseFragment;
import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.model.Food;
import com.example.fridgerec.model.viewmodel.InventoryViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryCreationFragment} factory method to
 * create an instance of this fragment.
 */
public class InventoryCreationFragment extends CreationBaseFragment {
  public static final String TAG = "InventoryCreationFragmemt";

  private FragmentInventoryCreationBinding binding;
//  private InventoryViewModel model;

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
    setupBaseFragment(new ViewModelProvider(requireActivity()).get(InventoryViewModel.class), view);

    onClickDatePickerBtn(binding.btnSourceDate);
    onClickDatePickerBtn(binding.btnExpireDate);

    if (Boolean.TRUE.equals(model.getInEditMode().getValue())) {
      populateEntryItemDetail(model.getSelectedEntryItem());
    }

    setupToolbar(binding.toolbar);
    configExposedDropdownMenu(binding.actvFoodGroup, getResources().getStringArray(R.array.foodGroupStrings));
    configExposedDropdownMenu(binding.actvAmountUnit, getResources().getStringArray(R.array.amountUnitStrings));
    onClickDatePickerBtn(binding.btnSourceDate);
    onClickDatePickerBtn(binding.btnExpireDate);
  }

  @Override
  protected void populateEntryItemDetail(EntryItem entryItem) {
    populateString(entryItem.getFood().getFoodName(), binding.tilFood);
    populateString(entryItem.getFood().getFoodGroup(), binding.tilFoodGroup);
    populateString(Integer.toString(entryItem.getAmount()), binding.tilAmount);
    populateString(entryItem.getAmountUnit(), binding.tilAmountUnit);
    populateDate(entryItem.getSourceDate(), binding.btnSourceDate);
    populateDate(entryItem.getExpireDate(), binding.btnExpireDate);
    //TODO: Spoonacular integration: if entryItem has apiId, disable foodgroup selection
  }

  @Override
  protected EntryItem extractEntryItem() {
    EntryItem entryItem = getEntryItem();

    Food food = extractFood(binding.tilFood, binding.tilFoodGroup);
    if (food == Food.DUMMY_FOOD) {
      return EntryItem.DUMMY_ENTRY_ITEM;
    }
    entryItem.setFood(food);

    entryItem = extractAmountInfo(entryItem, binding.tilAmount, binding.tilAmountUnit);
    if (entryItem == EntryItem.DUMMY_ENTRY_ITEM) {
      return null;
    }

    return extractSourceExpireDates(entryItem, binding.btnSourceDate, binding.btnExpireDate);
  }

  @Override
  protected void navigateAway() {
    navController.navigate(R.id.action_inventoryCreationFragment_to_inventoryFragment);
  }
}