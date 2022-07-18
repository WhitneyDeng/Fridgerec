package com.example.fridgerec.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fridgerec.R;
import com.example.fridgerec.databinding.FragmentShoppingCreationBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingCreationFragment} factory method to
 * create an instance of this fragment.
 */
public class ShoppingCreationFragment extends Fragment {
  private NavController navController;
  private AppBarConfiguration appBarConfiguration;
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
    navController = Navigation.findNavController(view);


    setupToolbar();
    onClickToolbarItem();

  }

  private void setupToolbar() {
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
  }

  private void onClickToolbarItem() {
    binding.toolbar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.miSave:
          //TODO: save item
          navController.navigate(R.id.action_shoppingCreationFragment_to_shoppingFragment);
          return true;
        default:
          return false;
      }
    });
  }
}