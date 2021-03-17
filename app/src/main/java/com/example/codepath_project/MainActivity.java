package com.example.codepath_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import bolts.Task;
import fragments.BuddyFragment;
import fragments.PetFragment;
import fragments.TasksFragment;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            // in this func bellow menuItem is one of the three
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_task:
                        fragment = new TasksFragment();
                        Toast.makeText(getApplicationContext(), "task list", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_pet:
                        fragment = new PetFragment();
                        Toast.makeText(getApplicationContext(), "pet ", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_buddy:
                    default:
                        fragment = new BuddyFragment();
                        Toast.makeText(getApplicationContext(), "buddy", Toast.LENGTH_SHORT).show();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                return true;
            };

        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_pet);
    }

    //https://stackoverflow.com/questions/7992216/android-fragment-handle-back-button-press
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}

