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
import android.widget.Button;
import android.widget.Toast;

import com.example.fridgerec.R;
import com.example.fridgerec.databinding.FragmentSettingsBinding;
import com.example.fridgerec.model.Settings;
import com.example.fridgerec.model.viewmodel.SettingsViewModel;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
  public static final String TAG = "SettingsFragment";
  private NavController navController;
  private AppBarConfiguration appBarConfiguration;
  private FragmentSettingsBinding binding;
  private SettingsViewModel model;

  private static final String TIME_PICKER_BUTTON_DIVIDER = ":";

  public SettingsFragment(){ }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentSettingsBinding.inflate(getLayoutInflater(), container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    navController = Navigation.findNavController(view);
    model = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);

    setupToolbar();
    onClickTimePickerBtn(binding.btnTimePicker);

    binding.btnLogout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        logout();
        navController.navigate(R.id.action_settingsFragment_to_loginFragment);
      }
    });
  }

  private void onClickTimePickerBtn(Button btnTimePicker) {
    MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
        .setTitleText("Select Notification Time")
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .build();

    btnTimePicker.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        timePicker.show(getChildFragmentManager(), "TIME_PICKER");
      }
    });

    timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String buttonText = configTimePickerBtnString(timePicker.getHour(), timePicker.getMinute());
        btnTimePicker.setText(buttonText);
      }
    });
  }

  private String configTimePickerBtnString(int hour, int minute) {
    return String.format("%02d%s%02d", hour, TIME_PICKER_BUTTON_DIVIDER, minute);
  }

  private void setupToolbar() {
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.inventoryFragment, R.id.shoppingFragment, R.id.settingsFragment).build();
    NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
  }

  private void logout() {
    ParseUser.logOut();
    Toast.makeText(getActivity(), "logout success", Toast.LENGTH_SHORT).show();
  }
}