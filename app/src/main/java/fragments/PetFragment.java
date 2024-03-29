package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.codepath_project.MainActivity;
import com.example.codepath_project.ParseApplication;
import com.example.codepath_project.Pet;
import com.example.codepath_project.R;
import com.example.codepath_project.StoreItem;
import com.example.codepath_project.Task;
import com.example.codepath_project.User;
import com.google.android.material.chip.Chip;
import com.mackhartley.roundedprogressbar.RoundedProgressBar;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.util.ArrayList;
import java.util.List;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codepath_project.SettingsActivity;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetFragment extends Fragment {
    public static final String TAG ="PetFragment";
    private RoundedProgressBar healthBar;
    private ImageButton btnFood;
    private ImageButton btnFancyFood;
    private Chip tvFoodCount;
    private Chip tvFancyFoodCount;
    private ImageView ivBackground;
    private ImageButton btnSettings;
    private ImageButton btnStore;
    private TextView tvCoinCount;
    private FragmentManager fragmentManager;
    private List<Integer> purchases;


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

        ivBackground = view.findViewById(R.id.ivBackground);
        healthBar = view.findViewById(R.id.healthBar);
        btnStore = view.findViewById(R.id.btnStore);
        tvCoinCount = view.findViewById(R.id.tvCoinCount);
        btnSettings = view.findViewById(R.id.btnSettings);
        btnFood = view.findViewById(R.id.btnFood);
        btnFancyFood = view.findViewById(R.id.btnFancyFood);
        tvFoodCount = view.findViewById(R.id.tvFoodCount);
        tvFancyFoodCount = view.findViewById(R.id.tvFancyFoodCount);

        // Todo: make this a func on the user class so that the callback is an argument and don't have duplicate code
        ParseQuery<Pet> query = ParseQuery.getQuery(Pet.class);
        query.whereEqualTo(Pet.KEY_OWNER, User.getCurrentUser());
        query.getFirstInBackground(new GetCallback<Pet>() {
            public void done(Pet pet, ParseException e) {
                if (e == null) {
                    tvCoinCount.setText(String.valueOf(pet.getInt(Pet.KEY_POINTS)));
                    healthBar.setProgressPercentage(pet.getInt(Pet.KEY_HEALTH), true);
                    purchases = pet.getList(Pet.KEY_PURCHASES);
                    List<StoreItem> backgrounds = StoreItem.storeItemData();
                    ivBackground.setImageResource(backgrounds.get(pet.getInt(Pet.KEY_BG)).getRes());
                    tvFoodCount.setText("x " +String.valueOf(pet.getInt(Pet.KEY_FOOD)));
                    tvFancyFoodCount.setText("x " +String.valueOf(pet.getInt(Pet.KEY_FANCY_FOOD)));

                } else {
                    // something went wrong
                    Log.e("PetFragment.java", "Error saving edits to task: " + e);
                }
            }
        });



        // Live Querying for changes on the User's pet, to make real-time updates
        ParseLiveQueryClient parseLiveQueryClient = ParseApplication.getClient();
        if (parseLiveQueryClient != null) {
            ParseQuery<ParseObject> parseQuery = new ParseQuery("Pet");
            parseQuery.whereEqualTo("owner", User.getCurrentUser());
            SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE, new SubscriptionHandling.HandleEventCallback<ParseObject>() {
                @Override
                public void onEvent(ParseQuery<ParseObject> query, final ParseObject object) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            int newHealth = object.getInt(Pet.KEY_HEALTH);
                            int newPoints = object.getInt(Pet.KEY_POINTS);
                            healthBar.setProgressPercentage(newHealth, true);
                            tvCoinCount.setText(String.valueOf(newPoints));
                            purchases = object.getList(Pet.KEY_PURCHASES);
                            List<StoreItem> backgrounds = StoreItem.storeItemData();
                            ivBackground.setImageResource(backgrounds.get(object.getInt(Pet.KEY_BG)).getRes());
                            tvFoodCount.setText("x " +String.valueOf(object.getInt(Pet.KEY_FOOD)));
                            tvFancyFoodCount.setText("x " +String.valueOf(object.getInt(Pet.KEY_FANCY_FOOD)));


                        }
                    });
                }
            });
        }



        final Context activityItem = getActivity();
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activityItem, SettingsActivity.class);
                activityItem.startActivity(i);

            }
        });

        btnStore.setOnClickListener(v -> {
            Fragment fragment = new StoreFragment().newInstance(Integer.parseInt((String) tvCoinCount.getText()), (ArrayList<Integer>) purchases);
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("pet_fragment").commit();

        });

        btnFancyFood.setOnClickListener(v -> {
            User.foodHealthUpdate(0, -1, 10);
        });

        btnFood.setOnClickListener(v -> {
            User.foodHealthUpdate(-1, 0, 5);
        });

    }





}
