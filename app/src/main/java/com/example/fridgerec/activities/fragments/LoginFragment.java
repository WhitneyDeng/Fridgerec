package com.example.fridgerec.activities.fragments;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fridgerec.R;
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
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_login, container, false);
  }

  public static final String TAG = "LoginActivity";
  private EditText etUsername;
  private EditText etPassword;
  private Button btnLogin;
  private Button btnSignup;

  private NavController navController;

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    navController = Navigation.findNavController(view);

    if (ParseUser.getCurrentUser() != null) {
      goMainActivity();
    }


    etUsername = view.findViewById(R.id.etUsername);
    etPassword = view.findViewById(R.id.etPassword);
    btnLogin = view.findViewById(R.id.btnLogin);
    btnSignup = view.findViewById(R.id.btnSignup);

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i(TAG, "onClick login button");
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        loginUser(username, password);
      }
    });

    btnSignup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i(TAG, "onClick sign up button");
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
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

          Toast.makeText(getActivity(), "Issue with Signup: " + e.getMessage(), Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
          return;
        }
        //todo: create Settings object for new user
        goMainActivity();
        Toast.makeText(getActivity(), "Signup Success", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
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

          Toast.makeText(getActivity(), "Issue with Login: " + e.getMessage(), Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
          return;
        }
        goMainActivity();
        Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
      }
    });
  }

  private void goMainActivity() {
    navController.navigate(R.id.action_loginFragment_to_inventoryFragment);
  }
}