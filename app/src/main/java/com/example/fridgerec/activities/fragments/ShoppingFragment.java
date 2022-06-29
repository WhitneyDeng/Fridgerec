package com.example.fridgerec.activities.fragments;

import android.os.Bundle;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingFragment} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends Fragment {
  private AppBarConfiguration appBarConfiguration;
  private NavController navController;

  private Toolbar toolbar;
  private PopupMenu popupMenu;
  private FloatingActionButton fab;

  public ShoppingFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_shopping, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    fab = view.findViewById(R.id.fab);
    toolbar = view.findViewById(R.id.toolbar);

    navController = Navigation.findNavController(view);

    setupToolbar();
    onClickToolbarItem(view);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Navigation.findNavController(view).navigate(R.id.action_shoppingFragment_to_shoppingCreationFragment);
      }
    });
  }

  private void setupToolbar() {
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
  }

  private void onClickToolbarItem(View view) {
    toolbar.setOnMenuItemClickListener(item -> {
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