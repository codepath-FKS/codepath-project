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
        ParseQuery<Pet> query = ParseQuery.getQuery(Pet.class);
        query.whereEqualTo(Pet.KEY_OWNER, getCurrentUser());
        query.getFirstInBackground(new GetCallback<Pet>() {
            public void done(Pet pet, ParseException e) {
                if (e == null) {
                    int hlth = pet.getInt(Pet.KEY_HEALTH);
                    hlth = (hlth + newHealth) < 0 ? 0 : hlth + newHealth;
                    pet.put(Pet.KEY_HEALTH, hlth);
                    pet.saveInBackground();
                } else {
                    Log.e("Buddy fragment", "confusion");
                    Log.e("Buddy Fragment", e.toString());
                }
            }
        });
    }

    public static void addPoints(ParseUser user, int newPoints){
        ParseQuery<Pet> query = ParseQuery.getQuery(Pet.class);
        query.whereEqualTo(Pet.KEY_OWNER, user);
        query.getFirstInBackground(new GetCallback<Pet>() {
            public void done(Pet pet, ParseException e) {
                if (e == null) {
                    int pts = pet.getInt(Pet.KEY_POINTS);
                    pet.put(Pet.KEY_POINTS, pts + newPoints);
                    pet.saveInBackground();
                } else {
                    Log.e("Buddy fragment", "confusion");
                    Log.e("Buddy Fragment", e.toString());
                }
            }
        });
    }

    public static void equipBG(int bgID){
        ParseQuery<Pet> query = ParseQuery.getQuery(Pet.class);
        query.whereEqualTo(Pet.KEY_OWNER, getCurrentUser());
        query.getFirstInBackground(new GetCallback<Pet>() {
            public void done(Pet pet, ParseException e) {
                if (e == null) {
                    pet.put(Pet.KEY_BG, bgID);
                    pet.saveInBackground();
                } else {
                    Log.e("Buddy fragment", "confusion");
                    Log.e("Buddy Fragment", e.toString());
                }
            }
        });
    }

    public static void purchase(int purchase, int cost){
        ParseQuery<Pet> query = ParseQuery.getQuery(Pet.class);
        query.whereEqualTo(Pet.KEY_OWNER, getCurrentUser());
        query.getFirstInBackground(new GetCallback<Pet>() {
            public void done(Pet pet, ParseException e) {
                if (e == null) {
                    switch(purchase) {
                        case 0:
                            pet.put(Pet.KEY_FOOD, pet.getInt(Pet.KEY_FOOD) + 1);
                            break;
                        case 1:
                            pet.put(Pet.KEY_FANCY_FOOD, pet.getInt(Pet.KEY_FANCY_FOOD) + 1);
                            break;
                        default:
                            List<Integer> purchases = pet.getList(Pet.KEY_PURCHASES);
                            purchases.add(purchase);
                            pet.put(Pet.KEY_PURCHASES, purchases);
                    }
                    int pts = pet.getInt(Pet.KEY_POINTS);
                    pet.put(Pet.KEY_POINTS, pts - cost);
                    pet.saveInBackground();
                } else {
                    // something went wrong
                    Log.e("User.java", "Error saving edits to task: " + e);
                }
            }
        });

    }



    public static void foodHealthUpdate(int food, int fancyFood, int health){
        ParseQuery<Pet> query = ParseQuery.getQuery(Pet.class);
        query.whereEqualTo(Pet.KEY_OWNER, getCurrentUser());
        query.getFirstInBackground(new GetCallback<Pet>() {
            public void done(Pet pet, ParseException e) {
                if (e == null) {
                    pet.put(Pet.KEY_FOOD, pet.getInt(Pet.KEY_FOOD) + food);
                    pet.put(Pet.KEY_FANCY_FOOD, pet.getInt(Pet.KEY_FANCY_FOOD) + fancyFood);
                    pet.put(Pet.KEY_HEALTH, pet.getInt(Pet.KEY_HEALTH) + health);
                    pet.saveInBackground();
                } else {
                    Log.e("Buddy fragment", "confusion");
                    Log.e("Buddy Fragment", e.toString());
                }
            }
        });
    }


}
