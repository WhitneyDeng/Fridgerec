package com.example.fridgerec.fragments;

import android.os.Bundle;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fridgerec.R;
import com.example.fridgerec.databinding.FragmentInventoryBinding;
import com.example.fridgerec.litho.FoodGroupsSection;
import com.example.fridgerec.litho.ListSection;
import com.example.fridgerec.litho.ListSectionSpec;
import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.EntryItemQuery;
import com.example.fridgerec.model.viewmodel.InventoryViewModel;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryFragment} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends Fragment {
  public static final String TAG = "InventoryFragment";
  private AppBarConfiguration appBarConfiguration;
  private NavController navController;
  private FragmentInventoryBinding binding;

  private View fragmentView;
  private PopupMenu popup;

  private InventoryViewModel model;

  public InventoryFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentInventoryBinding.inflate(getLayoutInflater(), container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = Navigation.findNavController(view);

    fragmentView = view;

    model = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);

    onClickFab();
    setupToolbar();
    onClickToolbarItem(view);


    setupObservers();

    EntryItemQuery.queryEntryItems(model,
        EntryItem.CONTAINER_LIST_INVENTORY);
  }

  private void setupObservers() {
    observeRecyclerDataset();
    observeSortFilterParams();
    //TODO: add observer for refresh (to reload litho recycler)
  }

  private void observeSortFilterParams() {
    final Observer<HashMap<EntryItemQuery.SortFilter, Object>> sortFilterParamsObserver = new Observer<HashMap<EntryItemQuery.SortFilter, Object>>() {
      @Override
      public void onChanged(HashMap<EntryItemQuery.SortFilter, Object> sortFilterObjectHashMap) {
        EntryItemQuery.queryEntryItems(model,
            EntryItem.CONTAINER_LIST_INVENTORY);

        Log.i(TAG, "sort & filter params changed");
      }
    };
    model.getSortFilterParams().observe(getViewLifecycleOwner(), sortFilterParamsObserver);
  }

  private void observeRecyclerDataset() {
    final Observer<List<EntryItem>> inventoryListObserver = new Observer<List<EntryItem>>() {
      @Override
      public void onChanged(List<EntryItem> entryItems) {
        setupLithoRecycler(model.getList().getValue());

        Log.i(TAG, "inventory list changed");
      }
    };

    model.getList().observe(getViewLifecycleOwner(), inventoryListObserver);

    final Observer<HashMap<String, List<EntryItem>>> inventoryMapObserver = new Observer<HashMap<String, List<EntryItem>>>() {
      @Override
      public void onChanged(HashMap<String, List<EntryItem>> stringListHashMap) {
        setupSectionedLithoRecycler(model.getMap().getValue());

        Log.i(TAG, "inventory list changed (sort by food group)");
      }
    };

    model.getMap().observe(getViewLifecycleOwner(), inventoryMapObserver);
  }

  private void onClickFab() {
    binding.fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navController.navigate(R.id.action_inventoryFragment_to_inventoryCreationFragment);
      }
    });
  }

  private void setupSectionedLithoRecycler(HashMap<String, List<EntryItem>> foodGroupMap) {
    final ComponentContext c = new ComponentContext(fragmentView.getContext());
    Component component = RecyclerCollectionComponent.create(c)
        .section(
            FoodGroupsSection.create(new SectionContext(c))
                .foodGroupMap(foodGroupMap)
                .build())
        .build();

    binding.lvInventoryList.setComponentAsync(component);
  }

  private void setupLithoRecycler(List<EntryItem> entryItems) {
    final ComponentContext c = new ComponentContext(fragmentView.getContext());
    Component component = RecyclerCollectionComponent.create(c)
        .disablePTR(true)
        .section(
            ListSection.create(new SectionContext(c))
                .foodCategoryHeaderTitle(ListSectionSpec.NO_HEADER) //TODO: take advantage of optional header
                .entryItems(entryItems)
                .build())
        .build();

    binding.lvInventoryList.setComponentAsync(component);
  }

  private void setupToolbar() {
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
  }

  private void onClickToolbarItem(View view) {
    binding.toolbar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.miSortFilter:
          navController.navigate(R.id.action_inventoryFragment_to_sortFilterPrefDialog );
          return true;
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

  //TODO: delete (this is for testing only)
  private void testQuery() {

    ParseQuery<EntryItem> query = ParseQuery.getQuery(EntryItem.class);

    ArrayList<EntryItem> entryItems = new ArrayList<>();
    query.whereEqualTo(EntryItem.KEY_USER, ParseUser.getCurrentUser())
        .whereEqualTo(EntryItem.KEY_CONTAINER_LIST, EntryItem.CONTAINER_LIST_INVENTORY)
        .include(EntryItem.KEY_FOOD);   // include User data of each Post class in response

    query.findInBackground(new FindCallback<EntryItem>() {
      @Override
      public void done(List<EntryItem> queryResult, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with post query", e);
          return;
        }
        Log.i(TAG, "Post query success");
        Log.i(TAG, "queryResult" + queryResult.toString());

        entryItems.addAll(queryResult);
      }
    });
  }
}