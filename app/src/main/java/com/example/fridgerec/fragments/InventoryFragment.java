package com.example.fridgerec.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fridgerec.R;
import com.example.fridgerec.databinding.FragmentInventoryBinding;
import com.example.fridgerec.fragments.basefragments.ListBaseFragment;
import com.example.fridgerec.model.viewmodel.InventoryViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryFragment} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends ListBaseFragment {
  public static final String TAG = "InventoryFragment";
  private FragmentInventoryBinding binding;

  public InventoryFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentInventoryBinding.inflate(getLayoutInflater(), container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupListFragment(new ViewModelProvider(requireActivity()).get(InventoryViewModel.class), view);
    model.enterReadMode();

    onClickFab(binding.fab);
    setupToolbar(binding.toolbar);
    setupObservers(binding.lvInventoryList);

    model.refreshDataset();
  }

  protected boolean onContextualMenuItemClicked(ActionMode mode, MenuItem item) {
    switch (item.getItemId()) {
      case R.id.mi_check:
      case R.id.mi_delete:
        Toast.makeText(getContext(),"removing " + model.getCheckedItemsSet().size() + " item(s)", Toast.LENGTH_LONG).show();
        model.deleteCheckedItems();
        mode.finish();
        return true;
    }
    return false;
  }

  @Override
  protected void navigateToCreation() {
    navController.navigate(R.id.action_inventoryFragment_to_inventoryCreationFragment);
  }
}