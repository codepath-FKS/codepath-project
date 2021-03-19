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
import com.royrodriguez.transitionbutton.TransitionButton;

import java.util.List;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "SignupActivity";
    private EditText etUsername;
    private EditText etPassword;
    private TransitionButton btnSignup;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        btnBack = findViewById(R.id.btnBack);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");
                btnSignup.startAnimation();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    btnSignup.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    Toast.makeText(SignupActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                checkUserExists(username, password);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick back button");
                goLoginActivity();
            }
        });

    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with login", e);
                    Toast.makeText(SignupActivity.this, "Issue with Login!", Toast.LENGTH_SHORT).show();
                    return;
                }

                btnSignup.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                    @Override
                    public void onAnimationStopEnd() {
                        goMainActivity();
                        Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void checkUserExists(final String username, final String password) {
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
                        Toast.makeText(SignupActivity.this, "This username is already in use!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

    public void createUser(final String username, final String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.put("points", 0);
        user.put("health", 100);


        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                if (e != null) {

                    Toast.makeText(SignupActivity.this,
                            "Saving user failed.", Toast.LENGTH_SHORT).show();
                    Log.w(TAG,
                            "Error : " + e.getMessage() + ":::" + e.getCode());

                } else {

                    Toast.makeText(SignupActivity.this, "User Saved",
                            Toast.LENGTH_SHORT).show();
                    // idk what im doin tbh
                    Pet pet = new Pet();
                    pet.setName("todo");
                    pet.setHealth(100);
                    pet.setPoints(100);
                    pet.setOwner(user);
                    pet.saveInBackground();
                    User.setPet(pet.getObjectId());
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

    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
