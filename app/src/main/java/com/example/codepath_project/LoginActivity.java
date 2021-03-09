package com.example.codepath_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

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

        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnLogout);

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
                Log.i(TAG, "onClick signup button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "There is no password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkUserExists(username, password);
            }
        });

    }

    private void loginUser(String username, String password){
        Log.i(TAG, "Attempting to login user " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "issue with login", e);
                    Toast.makeText(LoginActivity.this, "Issue with Login!", Toast.LENGTH_SHORT).show();
                    return;
                }

                goMainActivity();
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserExists(final String username, final String password){
        Log.i(TAG, "Checking if " + username + " is in use");

        ParseQuery<ParseObject> query = new ParseQuery<>("User");
        query.whereEqualTo("username", username);
        // this is for doing in background
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> users, ParseException e) {
                if (e == null) {
                    if (users.size() == 0) {
                        // if username doesn't exist in the database
                        Log.i(TAG, "username is free");
                        createUser(username, password);
                    } else {
                        // if username exists in the database
                        Toast.makeText(LoginActivity.this, "This username is already in use!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

    public void createUser(final String username, final String password){
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.put("points", 0);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                if (e != null) {

                    Toast.makeText(LoginActivity.this,
                            "Saving user failed.", Toast.LENGTH_SHORT).show();
                    Log.w(TAG,
                            "Error : " + e.getMessage() + ":::" + e.getCode());

                } else {

                    Toast.makeText(LoginActivity.this, "User Saved",
                            Toast.LENGTH_SHORT).show();
                    loginUser(username, password);
                    /*Do some things here if you want to.*/

                }

            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        // finish login acitivty once you've done the navigation
        finish();
    }
}