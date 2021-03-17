package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.codepath_project.MainActivity;
import com.example.codepath_project.R;
import com.mackhartley.roundedprogressbar.RoundedProgressBar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import android.widget.ImageButton;
import com.example.codepath_project.SettingsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetFragment extends Fragment {
    public static final String TAG ="PetFragment";
    private RoundedProgressBar healthBar;
    private Button btnIncrease;
    private Button btnDecrease;
    private ImageButton btnSettings;
    private ImageButton btnStore;
    private FragmentManager fragmentManager;


    public PetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentManager = (getActivity()).getSupportFragmentManager();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        healthBar = view.findViewById(R.id.healthBar);
        btnIncrease = view.findViewById(R.id.btnIncrease);
        btnDecrease = view.findViewById(R.id.btnDecrease);
        btnStore = view.findViewById(R.id.btnStore);

        btnIncrease.setOnClickListener(view1 -> {
            double current = healthBar.getProgressPercentage();
            if (current == 0){
                current = 15.0;
            }
            healthBar.setProgressPercentage(current*2.0,true);
        });


        btnDecrease.setOnClickListener(view12 -> {
            double current = healthBar.getProgressPercentage();
            healthBar.setProgressPercentage(current*.5,true);
        });


        btnSettings = view.findViewById(R.id.btnSettings);

        final Context activityItem = getActivity();
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activityItem, SettingsActivity.class);
                activityItem.startActivity(i);

            }
        });

        btnStore.setOnClickListener(v -> {

            Fragment fragment = new StoreFragment();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("pet_fragment").commit();;

        });




    }



}
