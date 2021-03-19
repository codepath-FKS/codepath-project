package com.example.codepath_project;

import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

// This class now extends parse user and can be used with static funcs like so, User.getHealth()


@ParseClassName("_User")
public class User extends ParseUser {
    public static final String KEY_POINTS = "points";
    public static final String KEY_PET = "userPet";
    ParseUser user;

    public static int getPoints(ParseUser user) {
        return user.getInt(KEY_POINTS);
    }

    public static void setPet(String pet) {
        getCurrentUser().put(KEY_PET, pet);
    }

    public static void addHealth(int newHealth) {
        ParseQuery<Pet> query = ParseQuery.getQuery("Pet");
        query.getInBackground(getCurrentUser().getString(KEY_PET), (pet, e) -> {
            if (e == null) {
                pet.setHealth(pet.getHealth() + newHealth);
                pet.saveInBackground();
            } else {
                // something went wrong
                Log.e("User.java", "Error saving edits to task: " + e);
            }
        });
    }

    public static void addPoints(ParseUser user){
        ParseQuery<Pet> query = ParseQuery.getQuery("Pet");
        query.getInBackground(user.getString(KEY_PET), (pet, e) -> {
            if (e == null) {
                int pts = pet.getPoints();
                pet.setPoints(pts + 12);
                pet.saveInBackground();
            } else {
                // something went wrong
                Log.e("User.java", "Error saving edits to task: " + e);
            }
        });
    }

//    public void setPoints(ParseUser user, int points){
//        user.put(KEY_POINTS, points);
//    }
//
//    public int getHealth(ParseUser user) {
//        return user.getInt(KEY_HEALTH);
//    }
//
//    public void setHealth(ParseUser user, int health) {
//        Log.e("set", "setting health to something");
//        user.put(KEY_HEALTH, health);
//    }
//
//    public static int getPoints(){
//        return getCurrentUser().getInt(KEY_POINTS);
//    }
//
//
//    public static int getHealth() {
//        return getCurrentUser().getInt(KEY_HEALTH);
//    }
//
//    public static void setHealth(int health) {
//        Log.e("set", "setting health to something");
//        getCurrentUser().put(KEY_HEALTH, health);
//    }

    /*
    public ParseUser getPet(){
        return getParseUser(KEY_PET);
    }

    public void setPoints(ParseUser pet){
        put(KEY_PET, pet);
    }
     */

}
