package com.example.fridgerec.fragments.basefragments;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fridgerec.EntryItemQuery;
import com.example.fridgerec.R;
import com.example.fridgerec.interfaces.DatasetViewModel;
import com.example.fridgerec.lithoSpecs.FoodGroupsSection;
import com.example.fridgerec.lithoSpecs.ListSection;
import com.example.fridgerec.lithoSpecs.ListSectionSpec;
import com.example.fridgerec.model.EntryItem;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;

public abstract class ListBaseFragment extends Fragment {
  public static final String TAG = "ListFragment";
  protected DatasetViewModel model;
  protected NavController navController;
  protected View fragmentView;

  protected abstract boolean onContextualMenuItemClicked(ActionMode mode, MenuItem item);
  protected abstract void navigateToCreation();

  protected void setupListFragment(DatasetViewModel viewModel, View view) {
    this.model = viewModel;
    this.fragmentView = view;
    this.navController = Navigation.findNavController(view);
  }

  protected void setupToolbar(Toolbar toolbar) {
    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    onClickToolbarItem(toolbar);
  }

  protected void setupObservers(LithoView lv) {
    observeRecyclerDataset(lv);
    observeSortFilterParams();
    observeInDeleteMode();
    observeInEditMode();
    //TODO: add observer ParseOperationSuccess?
  }

  private void observeInEditMode() {
    final Observer<Boolean> inEditModeObserver = new Observer<Boolean>() {
      @Override
      public void onChanged(Boolean inEditMode) {
        if (inEditMode) {
          Log.i(TAG, "item selected: " + model.getSelectedEntryItem().getFood().getFoodName());
          navigateToCreation();
        }
      }
    };
    model.getInEditMode().observe(getViewLifecycleOwner(), inEditModeObserver);
  }

  private void observeInDeleteMode() {
    final Observer<Boolean> inDeleteModeObserver = new Observer<Boolean>() {
      @Override
      public void onChanged(Boolean inDeleteMode) {
        Log.i(TAG, "inDeleteMode changed to: " + inDeleteMode);
        if (inDeleteMode) {
          fragmentView.startActionMode(configContextualMenuCallback());
        } else {
          model.refreshDataset();
          //TODO: success or failed delete;
        }
      }
    };
    model.getInDeleteMode().observe(getViewLifecycleOwner(), inDeleteModeObserver);
  }

  private void observeSortFilterParams() {
    final Observer<HashMap<EntryItemQuery.SortFilter, Object>> sortFilterParamsObserver = new Observer<HashMap<EntryItemQuery.SortFilter, Object>>() {
      @Override
      public void onChanged(HashMap<EntryItemQuery.SortFilter, Object> sortFilterObjectHashMap) {
        model.refreshDataset();

        Log.i(TAG, "sort & filter params changed");
      }
    };
    model.getSortFilterParams().observe(getViewLifecycleOwner(), sortFilterParamsObserver);
  }

  private void observeRecyclerDataset(LithoView lv) {
    final Observer<List<EntryItem>> listObserver = new Observer<List<EntryItem>>() {
      @Override
      public void onChanged(List<EntryItem> entryItems) {
        setupLithoRecycler(model.getList().getValue(), lv);

        Log.i(TAG, "list changed");
      }
    };

    model.getList().observe(getViewLifecycleOwner(), listObserver);

    final Observer<HashMap<String, List<EntryItem>>> mapObserver = new Observer<HashMap<String, List<EntryItem>>>() {
      @Override
      public void onChanged(HashMap<String, List<EntryItem>> stringListHashMap) {
        setupSectionedLithoRecycler(model.getMap().getValue(), lv);

        Log.i(TAG, "list changed (sort by food group)");
      }
    };

    model.getMap().observe(getViewLifecycleOwner(), mapObserver);
  }

  protected void onClickFab(FloatingActionButton fab) {
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navigateToCreation();
      }
    });
  }

  private void setupSectionedLithoRecycler(HashMap<String, List<EntryItem>> foodGroupMap, LithoView lv) {
    final ComponentContext c = new ComponentContext(fragmentView.getContext());
    Component component = RecyclerCollectionComponent.create(c)
        .section(
            FoodGroupsSection.create(new SectionContext(c))
                .foodGroupMap(foodGroupMap)
                .viewModel(model)
                .build())
        .build();

    lv.setComponentAsync(component);
  }

  private void setupLithoRecycler(List<EntryItem> entryItems, LithoView lv) {
    final ComponentContext c = new ComponentContext(fragmentView.getContext());
    Component component = RecyclerCollectionComponent.create(c)
        .disablePTR(true)
        .section(
            ListSection.create(new SectionContext(c))
                .entryItems(entryItems)
                .viewModel(model)
                .build())
        .build();

    lv.setComponentAsync(component);
  }

  private void onClickToolbarItem(Toolbar toolbar) {
    toolbar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.miSortFilter:
          navController.navigate(R.id.sortFilterPrefDialog);
          return true;
        default:
          return false;
      }
    });
  }

  private ActionMode.Callback configContextualMenuCallback() {
    return new ActionMode.Callback() {
      @Override
      public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
        mode.setTitle("Select Items");
        return true;
      }

      @Override
      public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
      }

      @Override
      public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return onContextualMenuItemClicked(mode, item);
      }

      @Override
      public void onDestroyActionMode(ActionMode mode) {
        model.getInDeleteMode().setValue(false);
      }
    };
  }
}
