package com.example.fridgerec.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fridgerec.R;
import com.example.fridgerec.databinding.FragmentShoppingCreationBinding;
import com.example.fridgerec.fragments.basefragments.CreationBaseFragment;
import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.model.Food;
import com.example.fridgerec.model.viewmodel.ShoppingViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingCreationFragment} factory method to
 * create an instance of this fragment.
 */
public class ShoppingCreationFragment extends CreationBaseFragment {
  public static final String TAG = "ShoppingCreationFragment";

  private FragmentShoppingCreationBinding binding;

  public ShoppingCreationFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentShoppingCreationBinding.inflate(getLayoutInflater(), container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupBaseFragment(new ViewModelProvider(requireActivity()).get(ShoppingViewModel.class), view);
    navController = Navigation.findNavController(view);

    if (Boolean.TRUE.equals(model.getInEditMode().getValue())) {
      populateEntryItemDetail(model.getSelectedEntryItem());
    }

    setupToolbar(binding.toolbar);
    onClickFoodChipCloseIcon(binding.cFood, binding.tilFood, binding.tilFoodGroup);
    configExposedDropdownMenu(binding.actvFoodGroup, getResources().getStringArray(R.array.foodGroupStrings));
    configExposedDropdownMenu(binding.actvAmountUnit, getResources().getStringArray(R.array.amountUnitStrings));
    configFoodActv(binding.actvFood);
  }

  @Override
  protected void populateEntryItemDetail(EntryItem entryItem) {
    populateTextInputLayout(entryItem.getFood().getFoodName(), binding.tilFood);
    populateTextInputLayout(entryItem.getFood().getFoodGroup(), binding.tilFoodGroup);
    populateTextInputLayout(Integer.toString(entryItem.getAmount()), binding.tilAmount);
    populateTextInputLayout(entryItem.getAmountUnit(), binding.tilAmountUnit);
  }

  @Override
  protected EntryItem extractEntryItem() {
    EntryItem entryItem = getEntryItem();

    Food food = extractFood();
    if (food == Food.DUMMY_FOOD) {
      return EntryItem.DUMMY_ENTRY_ITEM;
    }
    entryItem.setFood(food);

    return extractAmountInfo(entryItem, binding.tilAmount, binding.tilAmountUnit);
  }

  @Override
  protected void navigateAway() {
    navController.navigate(R.id.action_shoppingCreationFragment_to_shoppingFragment);
  }

  @Override
  public void onSuggestionSelected(Food selectedFood) {
    onSuggestionSelected(selectedFood, binding.tilFoodGroup, binding.cFood, binding.tilFood);
  }
}