package com.example.codepath_project;

import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.function.Function;

// This class now extends parse user and can be used with static funcs like so, User.getHealth()


@ParseClassName("_User")
public class User extends ParseUser {
    public static final String KEY_POINTS = "points";
    public static final String KEY_PET = "userPet";
    ParseUser user;

    public static void setPet(String pet) {
        getCurrentUser().put(KEY_PET, pet);
    }

    public static void addHealth(int newHealth) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Pet");
        query.getInBackground(getCurrentUser().getString(KEY_PET), (pet, e) -> {
            if (e == null) {
                int hlth = pet.getInt(Pet.KEY_HEALTH);
                hlth = (hlth + newHealth) < 0 ? 0 : hlth + newHealth;
                pet.put(Pet.KEY_HEALTH, hlth);
                pet.saveInBackground();
            } else {
                // something went wrong
                Log.e("User.java", "Error saving edits to task: " + e);
            }
        });
    }

    public static void addPoints(ParseUser user, int newPoints){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Pet");
        query.getInBackground(user.getString(KEY_PET), (pet, e) -> {
            if (e == null) {
                int pts = pet.getInt(Pet.KEY_POINTS);
                pet.put(Pet.KEY_POINTS, pts + newPoints);
                pet.saveInBackground();
            } else {
                // something went wrong
                Log.e("User.java", "Error saving edits to task: " + e);
            }
        });
    }

    public static void equipBG(int bgID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Pet");
        query.getInBackground(getCurrentUser().getString(KEY_PET), (pet, e) -> {
            if (e == null) {
                pet.put(Pet.KEY_BG, bgID);
                pet.saveInBackground();
            } else {
                // something went wrong
                Log.e("User.java", "Error saving edits to task: " + e);
            }
        });
    }

    public static void purchase(int purchase, int cost){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Pet");
        query.getInBackground(getCurrentUser().getString(KEY_PET), (pet, e) -> {
            if (e == null) {
                List<Integer> purchases = pet.getList(Pet.KEY_PURCHASES);
                purchases.add(purchase);
                pet.put(Pet.KEY_PURCHASES, purchases);
                int pts = pet.getInt(Pet.KEY_POINTS);
                pet.put(Pet.KEY_POINTS, pts - cost);
                pet.saveInBackground();
            } else {
                // something went wrong
                Log.e("User.java", "Error saving edits to task: " + e);
            }
        });
    }


    //Todo: make function to update purchase list




}
