package com.example.fridgerec.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fridgerec.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
  public static final String TAG = "LoginActivity";
  private EditText etUsername;
  private EditText etPassword;
  private Button btnLogin;
  private Button btnSignup;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    if (ParseUser.getCurrentUser() != null) {
      goMainActivity();
    }

    etUsername = findViewById(R.id.etUsername);
    etPassword = findViewById(R.id.etPassword);
    btnLogin = findViewById(R.id.btnLogin);
    btnSignup = findViewById(R.id.btnSignup);

    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }

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

          Toast.makeText(LoginActivity.this, "Issue with Signup: " + e.getMessage(), Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
          return;
        }
        goMainActivity();
        Toast.makeText(LoginActivity.this, "Signup Success", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
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

          Toast.makeText(LoginActivity.this, "Issue with Login: " + e.getMessage(), Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
          return;
        }
        goMainActivity();
        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show(); //todo: change to Snackbar
      }
    });
  }

  private void goMainActivity() {
    Intent i = new Intent(this, MainActivity.class);
    startActivity(i);
    finish();
  }
}