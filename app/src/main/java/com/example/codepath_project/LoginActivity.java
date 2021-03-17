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

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private TransitionButton btnLogin;
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
        btnLogin = findViewById(R.id.btnSignup);
        btnSignup = findViewById(R.id.btnBack);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                btnLogin.startAnimation();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    Toast.makeText(LoginActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginUser(username, password);

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");
                goSignUpActivity();
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
                    btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    Toast.makeText(LoginActivity.this, "Issue with Login!", Toast.LENGTH_SHORT).show();
                    return;
                }

                btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                    @Override
                    public void onAnimationStopEnd() {
                        goMainActivity();
                        Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }


    private void goSignUpActivity() {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
        finish();
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        // finish login acitivty once you've done the navigation
        finish();
    }
}