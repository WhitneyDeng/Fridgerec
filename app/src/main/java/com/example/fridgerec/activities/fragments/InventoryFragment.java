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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.fridgerec.R;
import com.example.fridgerec.activities.MainActivity;
//import com.example.fridgerec.activities.lithoSpecs.ListItem;
//import com.example.fridgerec.activities.lithoSpecs.ListSection;
import com.example.fridgerec.activities.lithoSpecs.FoodGroupsSection;
import com.example.fridgerec.activities.lithoSpecs.ListSection;
import com.example.fridgerec.activities.lithoSpecs.ListSectionSpec;
import com.example.fridgerec.model.EntryItem;
import com.example.fridgerec.model.EntryItemList;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentTree;
import com.facebook.litho.LithoView;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.facebook.litho.widget.Text;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
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

  private LithoView lvInventoryList;
  private Toolbar toolbar;
  private FloatingActionButton fab;
  private PopupMenu popup;

  private EntryItemList entryItemList;

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

    lvInventoryList = view.findViewById(R.id.lvInventoryList);
    fab = view.findViewById(R.id.fab);
    toolbar = view.findViewById(R.id.toolbar);

    entryItemList = new EntryItemList();

    testQuery();

    setupLithoView(view, EntryItemList.SortFilter.NONE);
    setupToolbar();
    onClickToolbarItem(view);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Navigation.findNavController(view).navigate(R.id.action_inventoryFragment_to_inventoryCreationFragment);
      }
    });
  }

  private void setupLithoView(View view, EntryItemList.SortFilter sortFilterParam) {
    ComponentContext c = new ComponentContext(view.getContext());
    Component component = Text.create(c).text("sort/filter param not recognised").build();

    switch (sortFilterParam)
    {
      case SORT_FOOD_GROUP:
        HashMap<String, List<EntryItem>> foodGroupMap =
            (HashMap<String, List<EntryItem>>)
                entryItemList.queryEntryItems(sortFilterParam, EntryItem.CONTAINER_LIST_INVENTORY);

        component = RecyclerCollectionComponent.create(c)
            .section(
                FoodGroupsSection.create(new SectionContext(c))
                    .foodGroupMap(foodGroupMap)
                    .build())
            .build();
        break;
      default:
        ArrayList<EntryItem> entryItems = (ArrayList<EntryItem>)
            entryItemList.queryEntryItems(sortFilterParam, EntryItem.CONTAINER_LIST_INVENTORY);

        component = RecyclerCollectionComponent.create(c)
            .disablePTR(true)
            .section(
                ListSection.create(new SectionContext(c))
                    .foodCategoryHeaderTitle(ListSectionSpec.NO_HEADER)
                    .entryItems(entryItems)
                    .build())
            .build();
    }

    //todo: switch section item depending on sorting
    lvInventoryList.setComponent(component);
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

  private void testQuery() {

    ParseQuery<EntryItem> query = ParseQuery.getQuery(EntryItem.class);

    ArrayList<EntryItem> entryItems = new ArrayList<>();
    query.whereEqualTo(EntryItem.KEY_USER, ParseUser.getCurrentUser());
    query.whereEqualTo(EntryItem.KEY_CONTAINER_LIST, EntryItem.CONTAINER_LIST_INVENTORY);
    query.include(EntryItem.KEY_FOOD);   // include User data of each Post class in response

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