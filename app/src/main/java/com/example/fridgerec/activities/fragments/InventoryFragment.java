package com.example.fridgerec.activities.fragments;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.fridgerec.R;
import com.example.fridgerec.activities.MainActivity;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentTree;
import com.facebook.litho.LithoView;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.facebook.litho.widget.Text;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryFragment} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends Fragment {
  private AppBarConfiguration appBarConfiguration;
  private NavController navController;

//  private RecyclerView rvInventoryList;
  private LithoView lvInventoryList;
  private Toolbar toolbar;
  private FloatingActionButton fab;
  private PopupMenu popup;

  private ComponentTree componentTree;

  public InventoryFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_inventory, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = Navigation.findNavController(view);


//    rvInventoryList = view.findViewById(R.id.rvInventoryList);
    lvInventoryList = view.findViewById(R.id.lvInventoryList);
    fab = view.findViewById(R.id.fab);
    toolbar = view.findViewById(R.id.toolbar);

    setupLithoView(view);
    setupToolbar();
    onClickToolbarItem(view);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Navigation.findNavController(view).navigate(R.id.action_inventoryFragment_to_inventoryCreationFragment);
      }
    });
  }

  private void setupLithoView(View view) {
    lvInventoryList.setComponent(
            Text.create(new ComponentContext(view.getContext()))
                    .text("Hello World")
                    .build()
    );
//            RecyclerCollectionComponent.create(lvInventoryList.getComponentContext())
//            .section());

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
    popup = new PopupMenu(getActivity(), view);
    MenuInflater inflater = popup.getMenuInflater();
    inflater.inflate(menu_popup, popup.getMenu());
    popup.show();
  }
}