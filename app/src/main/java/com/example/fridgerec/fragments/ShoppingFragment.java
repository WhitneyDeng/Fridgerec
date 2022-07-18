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
import com.example.fridgerec.databinding.FragmentShoppingBinding;
import com.example.fridgerec.fragments.basefragments.ListBaseFragment;
import com.example.fridgerec.model.viewmodel.ShoppingViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingFragment} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends ListBaseFragment {
  private FragmentShoppingBinding binding;

  public ShoppingFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentShoppingBinding.inflate(getLayoutInflater(), container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupBaseFragment(new ViewModelProvider(requireActivity()).get(ShoppingViewModel.class), view);
    model.enterReadMode();

    onClickFab(binding.fab);
    setupToolbar(binding.toolbar);
    setupObservers(binding.lvShoppingList);

    model.refreshDataset();
  }

  @Override
  protected boolean onContextualMenuItemClicked(ActionMode mode, MenuItem item) {
    switch (item.getItemId()) {
      case R.id.mi_check:
        // TODO: implement
        return true;
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
    navController.navigate(R.id.action_shoppingFragment_to_shoppingCreationFragment);
  }
}