package com.example.codepath_project;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/// DONT USE THIS CLASS FOR ANYTHING LMAO user ParseUser


@ParseClassName("User")
public class User extends ParseObject {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_POINTS = "points";
    //public static final String KEY_PET = "userPet";

    public String getUsername(){
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username){
        put(KEY_USERNAME, username);
    }

    public void setPassword(String password){
        put(KEY_PASSWORD, password);
    }

    public int getPoints(){
        return getInt(KEY_POINTS);
    }

    public void setPoints(int points){
        put(KEY_POINTS, points);
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
