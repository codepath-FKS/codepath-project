package com.example.codepath_project;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

// This class now extends parse user and can be used with static funcs like so, User.getHealth()


@ParseClassName("_User")
public class User extends ParseUser {
    public static final String KEY_POINTS = "points";
    public static final String KEY_HEALTH = "health";
    public static final String KEY_PET = "pet";
    ParseUser user;

    public static int getPoints(ParseUser user){
        return user.getInt(KEY_POINTS);
    }

//    public static int getPoints(){
//        if (getPet() == null){
//            return 0;
//        }
//        return getPet().getPoints();
//    }
//public static void  setPet(Pet pet){
//    getCurrentUser().put(KEY_PET, pet);
//}
//
//    public static Pet getPet(){
//        return (Pet) getCurrentUser().get(KEY_PET);
//    }

    public void setPoints(ParseUser user, int points){
        user.put(KEY_POINTS, points);
    }

    public int getHealth(ParseUser user) {
        return user.getInt(KEY_HEALTH);
    }

    public void setHealth(ParseUser user, int health) {
        Log.e("set", "setting health to something");
        user.put(KEY_HEALTH, health);
    }

    public static int getPoints(){
        return getCurrentUser().getInt(KEY_POINTS);
    }


    public static int getHealth() {
        return getCurrentUser().getInt(KEY_HEALTH);
    }

    public static void setHealth(int health) {
        Log.e("set", "setting health to something");
        getCurrentUser().put(KEY_HEALTH, health);
    }






    /*
    public ParseUser getPet(){
        return getParseUser(KEY_PET);
    }

    public void setPoints(ParseUser pet){
        put(KEY_PET, pet);
    }
     */

}
