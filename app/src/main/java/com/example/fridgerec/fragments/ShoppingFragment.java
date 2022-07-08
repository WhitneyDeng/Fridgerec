package com.example.fridgerec.fragments;

import android.os.Bundle;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fridgerec.R;
import com.example.fridgerec.databinding.FragmentShoppingBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingFragment} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends Fragment {
  private AppBarConfiguration appBarConfiguration;
  private NavController navController;
  private FragmentShoppingBinding binding;


  private PopupMenu popupMenu;

  public ShoppingFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentShoppingBinding.inflate(getLayoutInflater(), container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);


    navController = Navigation.findNavController(view);

    setupToolbar();
    onClickToolbarItem(view);

    binding.fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Navigation.findNavController(view).navigate(R.id.action_shoppingFragment_to_shoppingCreationFragment);
      }
    });
  }

  private void setupToolbar() {
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
  }

  private void onClickToolbarItem(View view) {
    binding.toolbar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.miFilter:
          showPopup(view.findViewById(R.id.miFilter), R.menu.menu_popup_filter);
          return true;
        case R.id.miSort:
          showPopup(view.findViewById(R.id.miSort), R.menu.menu_popup_sort);
          return true;
        default:
          return false;
      }
    });
  }

  private void showPopup(View view, @MenuRes int menu_popup) {
    popupMenu = new PopupMenu(getActivity(), view);
    MenuInflater inflater = popupMenu.getMenuInflater();
    inflater.inflate(menu_popup, popupMenu.getMenu());
    popupMenu.show();
  }
}