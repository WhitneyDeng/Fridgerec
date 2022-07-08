package com.example.fridgerec.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fridgerec.R;
import com.example.fridgerec.databinding.FragmentLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

  public LoginFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentLoginBinding.inflate(getLayoutInflater(), container, false);
    return binding.getRoot();
  }

  public static final String TAG = "LoginActivity";
  private FragmentLoginBinding binding;

  private NavController navController;


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    navController = Navigation.findNavController(view);

    if (ParseUser.getCurrentUser() != null) {
      goMainActivity();
    }

    binding.btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i(TAG, "onClick login button");
        String username = binding.etUsername.getText().toString();
        String password = binding.etPassword.getText().toString();
        loginUser(username, password);
      }
    });

    binding.btnSignup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i(TAG, "onClick sign up button");
        String username = binding.etUsername.getText().toString();
        String password = binding.etPassword.getText().toString();
        signupUser(username, password);
      }
    });
  }

  private void signupUser(String username, String password) {
    Log.i(TAG, "Attempting to signup user" + username);
    ParseUser user = new ParseUser();

    user.setUsername(username);
    user.setPassword(password);

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with signup", e);

          Toast.makeText(getActivity(), "Issue with Signup: " + e.getMessage(), Toast.LENGTH_SHORT).show(); //TODO: change to Snackbar
          return;
        }
        //TODO: create Settings object for new user
        goMainActivity();
        Toast.makeText(getActivity(), "Signup Success", Toast.LENGTH_SHORT).show(); //TODO: change to Snackbar
      }
    });
  }



  private void loginUser(String username, String password) {
    Log.i(TAG, "Attempting to login user " + username);
    ParseUser.logInInBackground(username, password, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with login", e);

          Toast.makeText(getActivity(), "Issue with Login: " + e.getMessage(), Toast.LENGTH_SHORT).show(); //TODO: change to Snackbar
          return;
        }
        goMainActivity();
        Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show(); //TODO: change to Snackbar
      }
    });
  }

  private void goMainActivity() {
    navController.navigate(R.id.action_loginFragment_to_inventoryFragment);
  }
}